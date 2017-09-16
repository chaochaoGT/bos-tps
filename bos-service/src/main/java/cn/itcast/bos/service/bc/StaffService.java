package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.Staff;

public interface StaffService {

	Staff validTelephone(String telephone);

	void save(Staff model);

	Page<Staff> pageQuery(PageRequest pageRequest);// 分页查询

	Page<Staff> pageQuery(PageRequest pageRequest, Specification<Staff> spec);// 条件分页查询

	Staff findOne(String id);

	List<Staff> ajaxListInUse();

}
