package cn.itcast.bos.service.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.auth.Function;

public interface FunctionService {

	Page<Function> pageQuery(PageRequest pageRequest);

	void save(Function model);

	List<Function> findAll();

}
