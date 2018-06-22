
package com.jeremy.modules.sys.dao;

import com.jeremy.common.persistence.TreeDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.common.persistence.TreeDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
