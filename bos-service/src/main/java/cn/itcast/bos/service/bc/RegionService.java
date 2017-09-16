package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.bc.Region;

public interface RegionService {

	void save(List<Region> regions);

	Page<Region> pageQuery(PageRequest pageRequest);

	/**
	 * 优化查询 从redis服务器查询数据
	 * 
	 * @param pageRequest
	 * @return
	 */
	String pageQueryByRedis(PageRequest pageRequest);

	List<Region> findAll();

	List<Region> findAll(String param);

}
