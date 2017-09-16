package cn.itcast.bos.service.city;

import java.util.List;

import cn.itcast.bos.domain.city.City;

public interface CityService {

	public List<City> findAll(int pid);

	public String findCityByPidFromRedis(int pid);

}
