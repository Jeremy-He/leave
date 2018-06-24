package com.jeremy.modules.oa.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeremy.common.persistence.ActEntity;
import com.jeremy.common.utils.excel.annotation.ExcelField;
import com.jeremy.common.utils.excel.fieldtype.DateType;
import com.jeremy.modules.act.entity.Act;
import com.jeremy.modules.sys.entity.Office;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeremy.common.persistence.DataEntity;
import com.jeremy.common.utils.StringUtils;
import com.jeremy.modules.sys.entity.User;
import com.jeremy.modules.sys.utils.DictUtils;

/**
 * 请假Entity
 * @author liuj
 * @version 2013-04-05
 */
public class Leave extends ActEntity<Leave> {
	
	private static final long serialVersionUID = 1L;
	private String reason; 	// 请假原因
	private String processInstanceId; // 流程实例编号
	private Date startTime;	// 请假开始日期
	private Date endTime;	// 请假结束日期
	private Date realityStartTime;	// 实际开始时间
	private Date realityEndTime;	// 实际结束时间
	private String leaveType;	// 假种
	private Integer applyLeaveDays; //申请请假天数
	private Integer giveLeaveDays; //批准请假天数
	private Integer realityLeaveDays; //实际请假天数
	private Integer status;  //状态
	private Integer countType; //计算方式

	private String ids;
	private Date createDateStart;
	private Date createDateEnd;
	private Integer isNeedParentAudit;
	private Office office;
	private Integer isCount;

	//-- 临时属性 --//
	// 流程任务
	private Task task;
	private Map<String, Object> variables;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;
	// 流程定义
	private ProcessDefinition processDefinition;

	public Leave() {
		super();
	}

	public Leave(String id){
		super();
	}

	@ExcelField(title="申请人", type=1, align=2, sort=2)
	public String getApplyUserName() {
		return createBy.getName();
	}

	@ExcelField(title="请假类型", type=1, align=2, sort=1, dictType = "oa_leave_type")
	public String getLeaveType() {
		return leaveType;
	}

	@ExcelField(title="申请时间", type=1, align=2, sort=3, fieldType = DateType.class)
	public Date getCreateDate() {
		return super.getCreateDate();
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	public String getLeaveTypeDictLabel() {
		return DictUtils.getDictLabel(leaveType, "oa_leave_type", "");
	}

	@ExcelField(title="请假原因", type=1, align=2, sort=13)
	@Length(min=1, max=255)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@ExcelField(title="开始时间", type=1, align=2, sort=4, fieldType = DateType.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@ExcelField(title="结束时间", type=1, align=2, sort=5, fieldType = DateType.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@ExcelField(title="实际开始时间", type=1, align=2, sort=10, fieldType = DateType.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}

	@ExcelField(title="实际结束时间", type=1, align=2, sort=11, fieldType = DateType.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}
	
	public User getUser() {
		return createBy;
	}
	
	public void setUser(User user) {
		this.createBy = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getIds() {
		List<String> idList = Lists.newArrayList();
		if (StringUtils.isNotBlank(ids)){
			String ss = ids.trim().replace("　", ",").replace(" ",",").replace("，", ",").replace("'", "");
			for(String s : ss.split(",")) {
//				if(s.matches("\\d*")) {
					idList.add("'"+s+"'");
//				}
			}
		}
		return StringUtils.join(idList, ",");
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Date getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	@ExcelField(title="申请天数", type=1, align=2, sort=7)
	public Integer getApplyLeaveDays() {
		return applyLeaveDays;
	}

	public void setApplyLeaveDays(Integer applyLeaveDays) {
		this.applyLeaveDays = applyLeaveDays;
	}

	@ExcelField(title="批准天数", type=1, align=2, sort=8)
	public Integer getGiveLeaveDays() {
		return giveLeaveDays;
	}

	public void setGiveLeaveDays(Integer giveLeaveDays) {
		this.giveLeaveDays = giveLeaveDays;
	}

	@ExcelField(title="实际请假天数", type=1, align=2, sort=12)
	public Integer getRealityLeaveDays() {
		return realityLeaveDays;
	}

	public void setRealityLeaveDays(Integer realityLeaveDays) {
		this.realityLeaveDays = realityLeaveDays;
	}

	@ExcelField(title="状态", type=1, align=2, sort=14, dictType = "oa_leave_status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ExcelField(title="是否排除休假日", type=1, align=2, sort=6, dictType = "yes_no")
	public Integer getCountType() {
		return countType;
	}

	public void setCountType(Integer countType) {
		this.countType = countType;
	}

	public Integer getIsNeedParentAudit() {
		return isNeedParentAudit;
	}

	public void setIsNeedParentAudit(Integer isNeedParentAudit) {
		this.isNeedParentAudit = isNeedParentAudit;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Integer getIsCount() {
		return isCount;
	}

	public void setIsCount(Integer isCount) {
		this.isCount = isCount;
	}
}


