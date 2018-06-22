
package com.jeremy.modules.oa.dao;

import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.common.persistence.CrudDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.oa.entity.OaNotify;

/**
 * 通知通告DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OaNotifyDao extends CrudDao<OaNotify> {
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify);
	
}