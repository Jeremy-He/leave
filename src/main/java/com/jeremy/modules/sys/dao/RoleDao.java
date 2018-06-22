
package com.jeremy.modules.sys.dao;

import com.jeremy.common.persistence.CrudDao;
import com.jeremy.common.persistence.annotation.MyBatisDao;
import com.jeremy.modules.sys.entity.Role;
import org.apache.ibatis.annotations.Param;

/**
 * 角色DAO接口
 * @author ThinkGem
 * @version 2013-12-05
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);

	public Role getDataScope(@Param("userId") String userId, @Param("permission") String permission);

}
