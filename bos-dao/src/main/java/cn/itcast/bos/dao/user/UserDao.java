package cn.itcast.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.itcast.bos.domain.user.User;

public interface UserDao extends JpaRepository<User, Integer> {
	// 1: JPQL 语句 直接书写在方法上 使用注解完成
	@Query("from User where email = ?1 and password = ?2")
	public User login(String username, String password);

	// 2: 登录 不写语句 根据方法名称 自动 生成 低层 sql 语句
	public User findByEmailAndPassword(String email, String password);

	// 3
	public User login1(String username, String password);

	// 4 :sql语句
	@Query(nativeQuery = true, value = "select * from t_user where email = ?1 and password = ?2")
	public User login2(String username, String password);

	// 5: 占位符查询 更新 修改 update xxx set x x=- :email mxmx w? xx = ? where id = ?
	@Query("from User where email = :email and password = :pwd")
	public User login3(@Param("email") String username, @Param("pwd") String password);

	public User findByTelephone(String telephone);

	// jpa 修改 结合 注解@Modifying
	@Modifying
	@Query("update User set password = ?2 where telephone = ?1")
	public void gobackpassword(String telephone, String password);

	public User findByEmail(String email);

}
