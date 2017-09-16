package cn.itcast.bos.service.bc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.StandardDao;
import cn.itcast.bos.domain.bc.Standard;
import cn.itcast.bos.service.bc.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardDao standardDao;

	@Override // save update /save oid
	public void save(Standard model) {
		// 全字段的修改! update xxx set did = xx x xxxx //....... where id = ?
		standardDao.save(model);
	}

	@Override
	public Page<Standard> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return standardDao.findAll(pageRequest);
	}

	@Override
	public void delBatch(String[] idsarr) {
		// TODO Auto-generated method stub
		for (int i = 0; i < idsarr.length; i++) {
			standardDao.delBatch(Integer.parseInt(idsarr[i]));
		}
	}

	@Override
	public List<Standard> findAllInUse() {
		// 收派标准 已启用的
		return standardDao.findAllInUse();
	}

}
