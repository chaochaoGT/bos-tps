package cn.itcast.bos.service.auth.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.MenuDao;
import cn.itcast.bos.domain.auth.Menu;
import cn.itcast.bos.service.auth.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> ajaxListHasSonMenus() {
		// TODO Auto-generated method stub
		return menuDao.ajaxListHasSonMenus();
	}

	@Override
	public Page<Menu> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		Page<Menu> all = menuDao.findAll(pageRequest);
		List<Menu> list = all.getContent();
		for (Menu menu : list) {
			Hibernate.initialize(menu.getMenu());
		}
		return all;
	}

	@Override
	public void save(Menu model) {
		// TODO Auto-generated method stub
		menuDao.save(model);
	}

	@Override
	public List<Menu> ajaxList() {
		// TODO Auto-generated method stub
		return menuDao.findAll();
	}

	@Override
	public List<Menu> findMenuByUserId(Integer id) {
		// TODO Auto-generated method stub
		return menuDao.findMenuByUserId(id);
	}

}
