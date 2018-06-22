
package com.jeremy.modules.gen.dao;

import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.common.persistence.CrudDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.gen.entity.GenTable;

/**
 * 业务表DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTableDao extends CrudDao<GenTable> {
	
}
