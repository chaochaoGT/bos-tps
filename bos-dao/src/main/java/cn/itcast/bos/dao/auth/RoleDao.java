package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.auth.Role;

//  Role  多表查询  必须要是  fetch 迫切连接  结果集封装单一泛型对象中  非迫切查询 结果集封装 Object[]
//  根据用户id查询用户对应角色   user_role 用户id
public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
	@Query("from Role r inner join fetch r.users u  where u.id = ?1")
	public List<Role> findRolesByUserId(Integer id);

	// sql select r.* from roles r , user_role ur where r.id = ur.rid and ur.uid = ?

}
