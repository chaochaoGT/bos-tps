package cn.itcast.bos.service.city.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.itcast.bos.dao.city.CityDao;
import cn.itcast.bos.domain.city.City;
import cn.itcast.bos.service.city.CityService;
import cn.itcast.redis.utils.RedisCRUD;

@Service
@Transactional
public class CityServiceImpl implements CityService {
	@Autowired
	private CityDao cityDao;
	@Autowired
	private RedisCRUD redisCRUDImpl;

	// 查询数据库 没有缓存优化
	@Override
	public List<City> findAll(int pid) {
		// TODO Auto-generated method stub
		return cityDao.findAllByPid(pid);
	}

	// 使用redis缓存优化查询
	@Override
	public String findCityByPidFromRedis(int pid) {
		// 第一次查询 从redis服务器获取对应数据
		String jsonvalue = redisCRUDImpl.GetJSONStringFromRedis(String.valueOf(pid));
		// 不为null 查询过了,缓存存在
		if (StringUtils.isBlank(jsonvalue)) {
			// null redis没有查询数据 查询数据库
			// 第一次查询数据库,将数据库数据序列化Json格式的数据存储到redis 服务器上
			List<City> list = findAll(pid);
			jsonvalue = JSON.toJSONString(list);// 满足easyui需要分页数据的json格式
			redisCRUDImpl.writeJSONStringToRedis(String.valueOf(pid), jsonvalue);// 将分页的json格式数据存储到redis服务器上
		}
		return jsonvalue;
	}

}
