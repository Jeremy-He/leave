package com.jeremy.modules.oa.dao;

import com.jeremy.common.persistence.CrudDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.oa.entity.Holidays;

import java.util.Date;

/**
 * 法定节假日配置DAO接口
 * @author Jeremy
 * @version 2018-06-21
 */
@MyBatisDao
public interface HolidaysDao extends CrudDao<Holidays> {

    Holidays getByDate(Date date);
	
}