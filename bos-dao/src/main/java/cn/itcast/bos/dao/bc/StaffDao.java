package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Staff;

public interface StaffDao extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {

	public Staff findByTelephone(String telephone);

	@Query("from Staff where deltag = 1")
	public List<Staff> ajaxListInUse();

}
