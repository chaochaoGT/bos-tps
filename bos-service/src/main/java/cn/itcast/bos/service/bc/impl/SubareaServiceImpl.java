package cn.itcast.bos.service.bc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.itcast.bos.dao.bc.SubareaDao;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.base.SubareaCondtion;
import cn.itcast.bos.service.bc.SubareaService;
import cn.itcast.redis.utils.RedisCRUD;

@Service
@Transactional
public class SubareaServiceImpl implements SubareaService {
	@Autowired
	private SubareaDao subareaDao;
	@Autowired
	private RedisCRUD redisCRUDImpl;

	@Override
	public void save(Subarea model) {
		subareaDao.save(model);
	}

	@Override
	public Page<Subarea> pageQuery(PageRequest pageRequest) {

		// 延迟对象 Region 立刻查询 数据库
		Page<Subarea> all = subareaDao.findAll(pageRequest);
		List<Subarea> list = all.getContent();
		for (Subarea subarea : list) {
			Hibernate.initialize(subarea.getRegion());
		}
		return all;
	}

	@Override
	public Page<Subarea> pageQuery(PageRequest pageRequest, SubareaCondtion<Subarea> spec) {
		// TODO Auto-generated method stub
		Page<Subarea> all = subareaDao.findAll(spec, pageRequest);
		List<Subarea> list = all.getContent();
		for (Subarea subarea : list) {
			Hibernate.initialize(subarea.getRegion());
		}
		return all;
	}

	@Override
	public List<Subarea> findAllBySpecification(SubareaCondtion<Subarea> specification) {
		List<Subarea> findAll = subareaDao.findAll(specification);
		for (Subarea subarea : findAll) {
			Hibernate.initialize(subarea.getRegion());// 代码执行 立刻发送sql 查询 区域信息
		}
		return findAll;
	}

	@Override
	public List<Subarea> noassociation() {
		// TODO Auto-generated method stub
		return subareaDao.noassociation();
	}

	@Override
	public String pageQueryByRedisCacahe(PageRequest pageRequest, SubareaCondtion<Subarea> spec, Map<String, Object> conditions1) {
		int pageNumber = pageRequest.getPageNumber();// 获取当前查询的页码
		int pageSize = pageRequest.getPageSize();// 获取当前查询每页记录数
		String keyId = "subarea_" + pageNumber + "_" + pageSize;// 因为是分页查询 ,所以存在redis服务器的key需要唯一!
		StringBuilder sb = new StringBuilder(keyId);
		// 1:map集合第一次null
		if (conditions1 != null && conditions1.size() != 0) {
			if (conditions1.containsKey("addresskey")) {
				sb.append(conditions1.get("addresskey")).append("_");
			}
			if (conditions1.containsKey("region.province")) {
				sb.append(conditions1.get("region.province")).append("_");
			}
			if (conditions1.containsKey("region.city")) {
				sb.append(conditions1.get("region.city")).append("_");
			}
			if (conditions1.containsKey("region.district")) {
				sb.append(conditions1.get("region.district")).append("_");
			}
			if (conditions1.containsKey("decidedzone.id")) {
				sb.append(conditions1.get("decidedzone.id")).append("_");
			}
		}
		System.out.println("---------------json key =" + sb.toString());
		// 通过key查询对应的分页数据 所以采用1_10 2_10 页码_每页记录数作为唯一标识 存储在redis服务器上
		String jsonvalue = redisCRUDImpl.GetJSONStringFromRedis(sb.toString());
		if (StringUtils.isBlank(jsonvalue)) {
			// 查询数据库 第一次查询数据库,将数据库数据序列化Json格式的数据存储到redis 服务器上
			Page<Subarea> pageData = subareaDao.findAll(spec, pageRequest);// 查询数据库
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", pageData.getTotalElements());
			map.put("rows", pageData.getContent());
			jsonvalue = JSON.toJSONString(map);// 满足easyui需要分页数据的json格式
			// jsonvalue = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);// 满足easyui需要分页数据的json格式
			redisCRUDImpl.writeJSONStringToRedis(sb.toString(), jsonvalue);// 将分页的json格式数据存储到redis服务器上
		}
		return jsonvalue;
	}

}
