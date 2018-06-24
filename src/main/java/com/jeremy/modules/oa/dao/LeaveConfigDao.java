package com.jeremy.modules.oa.dao;

import com.jeremy.common.persistence.CrudDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.oa.entity.LeaveConfig;

/**
 * 年休假规则DAO接口
 * @author jeremy
 * @version 2018-06-17
 */
@MyBatisDao
public interface LeaveConfigDao extends CrudDao<LeaveConfig> {

    Integer getLeaveDays(int seniority);
}