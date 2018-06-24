package com.jeremy.modules.oa.service;

import java.util.Date;
import java.util.List;

import com.jeremy.common.utils.DateUtils;
import com.jeremy.modules.oa.dao.LeaveDao;
import com.jeremy.modules.oa.entity.Leave;
import com.jeremy.modules.sys.dao.UserDao;
import com.jeremy.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.persistence.Page;
import com.jeremy.common.service.CrudService;
import com.jeremy.modules.oa.entity.LeaveConfig;
import com.jeremy.modules.oa.dao.LeaveConfigDao;
import org.springframework.util.CollectionUtils;

/**
 * 年休假规则Service
 * @author jeremy
 * @version 2018-06-17
 */
@Service
@Transactional(readOnly = true)
public class LeaveConfigService extends CrudService<LeaveConfigDao, LeaveConfig> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private LeaveDao leaveDao;

	public LeaveConfig get(String id) {
		return super.get(id);
	}
	
	public List<LeaveConfig> findList(LeaveConfig leaveConfig) {
		return super.findList(leaveConfig);
	}
	
	public Page<LeaveConfig> findPage(Page<LeaveConfig> page, LeaveConfig leaveConfig) {
		return super.findPage(page, leaveConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(LeaveConfig leaveConfig) {
		super.save(leaveConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(LeaveConfig leaveConfig) {
		super.delete(leaveConfig);
	}

	/**
	 * 获取剩余年休假天数
	 * @param userId
	 * @return
	 */
	public int getLeftHolidays(String userId) {
		User user = userDao.get(userId);
		Leave condition = new Leave();
		condition.setCreateDateStart(user.getEntryDate());
		condition.setCreateDateEnd(DateUtils.addYears(user.getEntryDate(), 1));
		// 审核通过
		condition.setStatus(2);
		// 年假
		condition.setLeaveType("0");
		List<Leave> leaveList = leaveDao.findList(condition);
		if (!CollectionUtils.isEmpty(leaveList)) {
			return 0;
		}
		int seniority = DateUtils.pastYears(user.getEntryDate());
		Integer leaveDays = dao.getLeaveDays(seniority);
		return leaveDays == null ? 0 : leaveDays;
	}

}