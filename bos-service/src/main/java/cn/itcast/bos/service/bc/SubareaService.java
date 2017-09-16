package cn.itcast.bos.service.bc;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.base.SubareaCondtion;

public interface SubareaService {

	void save(Subarea model);

	Page<Subarea> pageQuery(PageRequest pageRequest);

	Page<Subarea> pageQuery(PageRequest pageRequest, SubareaCondtion<Subarea> spec);

	String pageQueryByRedisCacahe(PageRequest pageRequest, SubareaCondtion<Subarea> spec, Map<String, Object> conditions);

	List<Subarea> findAllBySpecification(SubareaCondtion<Subarea> specification);

	List<Subarea> noassociation();

}
