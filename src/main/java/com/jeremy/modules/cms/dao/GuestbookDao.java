
package com.jeremy.modules.cms.dao;

import com.jeremy.common.persistence.CrudDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.cms.entity.Guestbook;

/**
 * 留言DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface GuestbookDao extends CrudDao<Guestbook> {

}
