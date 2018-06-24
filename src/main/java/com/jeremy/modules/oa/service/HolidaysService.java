package com.jeremy.modules.oa.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jeremy.common.utils.DateUtils;
import com.jeremy.common.utils.TimeUtils;
import com.jeremy.modules.oa.dao.LeaveConfigDao;
import com.jeremy.modules.oa.dao.LeaveDao;
import com.jeremy.modules.oa.entity.Leave;
import com.jeremy.modules.sys.dao.UserDao;
import com.jeremy.modules.sys.entity.User;
import com.jeremy.modules.sys.service.SystemService;
import com.jeremy.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.persistence.Page;
import com.jeremy.common.service.CrudService;
import com.jeremy.modules.oa.entity.Holidays;
import com.jeremy.modules.oa.dao.HolidaysDao;
import org.springframework.util.CollectionUtils;

/**
 * 法定节假日配置Service
 *
 * @author Jeremy
 * @version 2018-06-21
 */
@Service
@Transactional(readOnly = true)
public class HolidaysService extends CrudService<HolidaysDao, Holidays> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private LeaveDao leaveDao;

	@Autowired
	private LeaveConfigDao leaveConfigDao;

	public Holidays get(String id) {
		return super.get(id);
	}

	public List<Holidays> findList(Holidays holidays) {
		return super.findList(holidays);
	}

	public Page<Holidays> findPage(Page<Holidays> page, Holidays holidays) {
		return super.findPage(page, holidays);
	}

	@Transactional(readOnly = false)
	public void save(Holidays holidays) {
		super.save(holidays);
	}

	@Transactional(readOnly = false)
	public void delete(Holidays holidays) {
		super.delete(holidays);
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
		List<Leave> leaveList = leaveDao.findList(condition);
		if (!CollectionUtils.isEmpty(leaveList)) {
			return 0;
		}
		int seniority = DateUtils.pastYears(user.getEntryDate());
		Integer leaveDays = leaveConfigDao.getLeaveDays(seniority);
		return leaveDays == null ? 0 : leaveDays;
	}

	/**
	 * 计算假期结束日期
	 * @param leave
	 * @return
	 */
	public Date countLeaveEndDate(Leave leave) {
		Date startDate = leave.getStartTime();
		Date endDate = DateUtils.addDays(startDate, leave.getApplyLeaveDays() - 1);
		if (leave.getCountType() == 0) {
			return endDate;
		}
		Date countTime = startDate;
		while (DateUtils.getDistanceOfTwoDate(countTime, endDate) >= 0) {
			Holidays holidays = dao.getByDate(countTime);
			String week = DateUtils.getWeek(countTime);
			if (holidays == null) {
				if (Arrays.asList("星期六", "星期日").contains(week)) {
					endDate = DateUtils.addDays(endDate, 1);
				}
			} else {
				if (holidays.getType() == 0) {
					endDate = DateUtils.addDays(endDate, 1);
				}
			}
			countTime = DateUtils.addDays(countTime, 1);
		}
		return endDate;
	}

}