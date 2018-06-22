package com.jeremy.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.persistence.Page;
import com.jeremy.common.service.CrudService;
import com.jeremy.modules.oa.entity.LeaveConfig;
import com.jeremy.modules.oa.dao.LeaveConfigDao;

/**
 * 年休假规则Service
 * @author jeremy
 * @version 2018-06-17
 */
@Service
@Transactional(readOnly = true)
public class LeaveConfigService extends CrudService<LeaveConfigDao, LeaveConfig> {

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
	
}