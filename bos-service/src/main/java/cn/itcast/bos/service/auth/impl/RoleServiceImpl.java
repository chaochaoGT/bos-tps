package cn.itcast.bos.service.auth.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FunctionDao;
import cn.itcast.bos.dao.auth.MenuDao;
import cn.itcast.bos.dao.auth.RoleDao;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Menu;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.service.auth.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private FunctionDao functionDao;
	@Autowired
	private MenuDao menuDao;

	@Override
	public void save(Role model, String menuids, String[] functionIds) {
		// role role_function role_menu 三表操作中间表循环记录添加 == 集合对象添加
		model = roleDao.saveAndFlush(model);
		// 权限 role_function JPA 托管对象
		if (functionIds != null && functionIds.length != 0) {
			Set<Function> functions = model.getFunctions();
			for (String fid : functionIds) {
				// Function function = functionDao.findOne(fid);// 如果不查询可以吗?
				Function function = new Function();// 如果不查询可以吗?
				function.setId(fid);
				functions.add(function);// 中间表的添加
			}
		}

		// 菜单
		if (StringUtils.isNotBlank(menuids)) {
			Set<Menu> menus = model.getMenus();
			for (String mid : menuids.split(",")) {
				// Menu menu = menuDao.findOne(mid);// 如果不查询可以吗?
				Menu menu = new Menu();
				menu.setId(mid);
				menus.add(menu);// 中间表的添加
			}
		}

	}

	@Override
	public Page<Role> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return roleDao.findAll(pageRequest);
	}

	@Override
	public List<Role> ajaxList() {
		// TODO Auto-generated method stub
		return roleDao.findAll();
	}

	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleDao.findAll();
	}

	@Override
	public List<Role> findRolesByUserId(Integer id) {
		// TODO Auto-generated method stub
		List<Role> list = roleDao.findRolesByUserId(id);
		for (Role role : list) {
			Hibernate.initialize(role.getFunctions());
		}
		return list;
	}

}
