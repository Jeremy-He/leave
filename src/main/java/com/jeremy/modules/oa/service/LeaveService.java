package com.jeremy.modules.oa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jeremy.modules.act.service.ActTaskService;
import com.jeremy.modules.act.utils.ProcessDefCache;
import com.jeremy.modules.oa.dao.LeaveDao;
import com.jeremy.modules.oa.entity.Leave;
import com.jeremy.modules.oa.entity.TestAudit;
import com.jeremy.modules.sys.dao.UserDao;
import com.jeremy.modules.sys.entity.Office;
import com.jeremy.modules.sys.entity.User;
import com.jeremy.modules.sys.utils.UserUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.persistence.Page;
import com.jeremy.common.service.BaseService;
import com.jeremy.common.utils.Collections3;
import com.jeremy.common.utils.StringUtils;
import com.jeremy.modules.act.utils.ActUtils;
import com.jeremy.modules.oa.dao.LeaveDao;
import com.jeremy.modules.oa.entity.Leave;

/**
 * 请假Service
 * @author liuj
 * @version 2013-04-05
 */
@Service
@Transactional(readOnly = true)
public class LeaveService extends BaseService {

	private static final String THE_TOP_OFFICE_ID = "2f27a634f63a496dbd45229027eed400";

	@Autowired
	private LeaveDao leaveDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private UserDao userDao;

	/**
	 * 获取流程详细及工作流参数
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public Leave get(String id) {
		Leave leave = leaveDao.get(id);
		if (leave != null && StringUtils.isNotBlank(leave.getProcessInstanceId())) {
			Map<String,Object> variables;
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
			if(historicProcessInstance!=null) {
				variables = Collections3.extractToMap(historyService.createHistoricVariableInstanceQuery().processInstanceId(historicProcessInstance.getId()).list(), "variableName", "value");
			} else {
				variables = runtimeService.getVariables(runtimeService.createProcessInstanceQuery().processInstanceId(leave.getProcessInstanceId()).active().singleResult().getId());
			}
			leave.setVariables(variables);
		}
		return leave;
	}
	
	/**
	 * 启动流程
	 */
	@Transactional(readOnly = false)
	public void save(Leave leave, Map<String, Object> variables) {
		
		// 保存业务数据
		if (StringUtils.isBlank(leave.getId())){
			leave.preInsert();
			leave.setStatus(1);
			leaveDao.insert(leave);
		}else{
			leave.preUpdate();
			leaveDao.update(leave);
		}
		logger.debug("save entity: {}", leave);
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(leave.getCurrentUser().getLoginName());
		// 启动流程
		String businessKey = "oa_leave:" + leave.getId();
		variables.put("type", "leave");
		variables.put("busId", businessKey);
		Office currentOffice = leave.getCurrentUser().getOffice();
		String auditOffice = THE_TOP_OFFICE_ID.equals(currentOffice.getParentId()) ? currentOffice.getId() : currentOffice.getParentId();
		variables.put("auditOffice", auditOffice);
		variables.put("officeGrade", currentOffice.getGrade());
		variables.put("directorGeneralAuditor", getDirectorGeneralAuditor());
		variables.put("title", "【" + leave.getCurrentUser().getOffice().getName() + "】" + leave.getCurrentUser().getName() + "的请假申请");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActUtils.PD_LEAVE[0], businessKey, variables);
		leave.setProcessInstance(processInstance);
		
		// 更新流程实例ID
		leave.setProcessInstanceId(processInstance.getId());
		leaveDao.updateProcessInstanceId(leave);
		
		logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", ActUtils.PD_LEAVE[0], businessKey, processInstance.getId(), variables);
		
	}

	private String getDirectorGeneralAuditor() {
		User conditionUser = new User();
		Office conditionOffice = new Office();
		conditionOffice.setId("1");
		conditionUser.setOffice(conditionOffice);
		List<User> userList = userDao.findUserByOfficeId(conditionUser);
		StringBuilder directorGeneralAuditor = new StringBuilder();
		User currentUser = UserUtils.getUser();
		userList.forEach(user -> {
			if (!user.getLoginName().equals(currentUser.getLoginName())) {
				if (directorGeneralAuditor.length() != 0) {
					directorGeneralAuditor.append(",");
				}
				directorGeneralAuditor.append(user.getLoginName());
			}
		});
		return directorGeneralAuditor.toString();
	}

	/**
	 * 查询待办任务
	 * @param userId 用户ID
	 * @return
	 */
	public List<Leave> findTodoTasks(String userId) {
		
		List<Leave> results = new ArrayList<Leave>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(ActUtils.PD_LEAVE[0]).taskAssignee(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(ActUtils.PD_LEAVE[0]).taskCandidateUser(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 合并
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
			String businessKey = processInstance.getBusinessKey().split(":")[1];
			Leave leave = leaveDao.get(businessKey);
			leave.setTask(task);
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId((processInstance.getProcessDefinitionId())).singleResult());
			results.add(leave);
		}
		return results;
	}

	public Page<Leave> find(Page<Leave> page, Leave leave) {

		//dataScopeFilter(leave, "id=o.id", "id=a.create_by", "oa:leave:view");
		leave.setCreateBy(UserUtils.getUser());
		leave.setPage(page);
		page.setList(leaveDao.findList(leave));
		
		for(Leave item : page.getList()) {
			String processInstanceId = item.getProcessInstanceId();
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
			item.setTask(task);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if(historicProcessInstance!=null) {
				item.setHistoricProcessInstance(historicProcessInstance);
				item.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
			} else {
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
				if (processInstance != null){
					item.setProcessInstance(processInstance);
					item.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
				}
			}
		}
		return page;
	}

	/**
	 * 审核审批保存
	 */
	@Transactional(readOnly = false)
	public void auditSave(Leave leave) {

		boolean isPass = "yes".equals(leave.getAct().getFlag());

		// 设置意见
		leave.getAct().setComment((isPass?"[同意] ":"[驳回] ")+leave.getAct().getComment());

		leave.preUpdate();

		if (!isPass) {
			// 审核拒绝
			leave.setStatus(4);
		}

		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("isPass", isPass);
		if (isPass && leave.getIsNeedParentAudit() == 0) {
			// 不需要上级审核，直接跳到销假环节
			actTaskService.getProcessEngine().getTaskService().addComment(leave.getAct().getTaskId(), leave.getAct().getProcInsId(), leave.getAct().getComment());
			actTaskService.jumpTask(leave.getAct().getProcInsId(), leave.getAct().getTaskId(), "reportBack", vars);
		} else {
			actTaskService.complete(leave.getAct().getTaskId(), leave.getAct().getProcInsId(), leave.getAct().getComment(), vars);
		}
		if ("reportBack".equals(actTaskService.getTaskByProcInsId(leave.getAct().getProcInsId()).getTaskDefinitionKey())) {
			// 审核通过
			leave.setStatus(2);
		}
		leaveDao.update(leave);
	}

	@Transactional(readOnly = false)
	public void reportBack(Leave leave) {
		// 设置意见
		leave.getAct().setComment("销假成功");
		leave.preUpdate();
		leave.setStatus(3);
		leaveDao.updateRealityTime(leave);
		actTaskService.complete(leave.getAct().getTaskId(), leave.getAct().getProcInsId(), leave.getAct().getComment(), null);
	}

	public Page<Leave> count(Page<Leave> page, Leave leave) {
		dataScopeFilter(leave, "id=o.id", "id=a.create_by", "oa:leave:view");
		leave.setPage(page);
		leave.setIsCount(1);
		page.setList(leaveDao.findList(leave));
		return page;
	}
}
