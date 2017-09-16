package cn.itcast.bos.service.user.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.user.UserDao;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.user.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		userDao.save(user);
	}

	@Override
	public void delete(User user) {
		// 托管对象 OID 唯一标识
		userDao.delete(user);
	}

	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		return userDao.findOne(id);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}

	@Override // spring data jpa 支持: sql 面向对象查询 jpql 无语句查询 纯代码生成语句 Sepecification orm
	public User findUserByUsernameAndPassword(String username, String password) {
		// 1:登录 根据用户名和密码查询.. spring data jpa 提供大量封装 查询实现 JPQL完成登录查询
		// return userDao.login(username, password);
		// 2:不书写任何语句 spring data jpa 根据dao 方法名自动生成对应sql 语句
		// return userDao.findByEmailAndPassword(username, password);
		// 3: 优化操作 1 命名查询 将所有hql语句 全部集中到 实体类上
		// return userDao.login1(username, password);
		// 4: sql语句
		// return userDao.login2(username, password);
		// 5 占位符查询
		return userDao.login3(username, password);

	}

	@Override
	public User findUserByTelephone(String telephone) {
		// TODO Auto-generated method stub
		return userDao.findByTelephone(telephone);
	}

	@Override
	public void gobackpassword(String telephone, String password) {
		// TODO Auto-generated method stub
		userDao.gobackpassword(telephone, password);
	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.findByEmail(email);
	}

	@Override
	public void save(User model, String[] roleIds) {
		// TODO Auto-generated method stub
		model = userDao.saveAndFlush(model);
		// 权限 role_function JPA 托管对象
		if (roleIds != null && roleIds.length != 0) {
			Set<Role> roles = model.getRoles();
			for (String rid : roleIds) {
				Role role = new Role();// 如果不查询可以吗?
				role.setId(rid);
				roles.add(role);// 中间表的添加
			}
		}
	}

	@Override
	public Page<User> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return userDao.findAll(pageRequest);
	}

}
