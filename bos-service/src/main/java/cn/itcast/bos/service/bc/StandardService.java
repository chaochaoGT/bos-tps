package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.bc.Standard;

public interface StandardService {

	void save(Standard model);

	Page<Standard> pageQuery(PageRequest pageRequest);

	void delBatch(String[] idsarr);

	List<Standard> findAllInUse();

}
