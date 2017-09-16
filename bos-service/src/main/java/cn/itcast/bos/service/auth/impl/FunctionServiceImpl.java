package cn.itcast.bos.service.auth.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FunctionDao;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.service.auth.FunctionService;

@Service
@Transactional
public class FunctionServiceImpl implements FunctionService {
	@Autowired
	private FunctionDao functionDao;

	@Override
	public Page<Function> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return functionDao.findAll(pageRequest);
	}

	@Override
	public void save(Function model) {
		// TODO Auto-generated method stub
		functionDao.save(model);
	}

	@Override
	public List<Function> findAll() {
		// TODO Auto-generated method stub
		return functionDao.findAll();
	}
}
