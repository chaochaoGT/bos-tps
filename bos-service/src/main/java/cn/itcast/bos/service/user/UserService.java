package cn.itcast.bos.service.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.user.User;

public interface UserService {
	public void save(User user);

	public void delete(User user);

	public User findUserById(Integer id);

	public List<User> findAll();

	// 业务 登录
	public User findUserByUsernameAndPassword(String username, String password);

	public User findUserByTelephone(String telephone);

	public void gobackpassword(String telephone, String password);

	public User findUserByEmail(String email);

	public void save(User model, String[] roleIds);

	public Page<User> pageQuery(PageRequest pageRequest);

}
