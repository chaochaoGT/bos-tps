package cn.itcast.bos.service.bc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.StaffDao;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.service.bc.StaffService;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
	@Autowired
	private StaffDao staffDao;

	@Override
	public Staff validTelephone(String telephone) {
		// TODO Auto-generated method stub
		return staffDao.findByTelephone(telephone);
	}

	@Override
	public void save(Staff model) {
		// TODO Auto-generated method stub
		staffDao.save(model);
	}

	@Override
	public Page<Staff> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		// Hibernate.initialize(延迟对象); 延迟对象立刻查询 发送SQL语句
		return staffDao.findAll(pageRequest);
	}

	@Override
	public Page<Staff> pageQuery(PageRequest pageRequest, Specification<Staff> spec) {
		// TODO Auto-generated method stub
		return staffDao.findAll(spec, pageRequest);
	}

	@Override
	public Staff findOne(String id) {
		// TODO Auto-generated method stub
		return staffDao.findOne(id);
	}

	@Override
	public List<Staff> ajaxListInUse() {
		// TODO Auto-generated method stub
		return staffDao.ajaxListInUse();
	}

}
