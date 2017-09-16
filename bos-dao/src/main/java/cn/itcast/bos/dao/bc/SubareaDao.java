package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Subarea;

public interface SubareaDao extends JpaRepository<Subarea, String>, JpaSpecificationExecutor<Subarea> {
	@Query("from Subarea where decidedZone is null")
	public List<Subarea> noassociation();

	@Modifying
	@Query("update Subarea set decidedZone =?2 where id =?1 ")
	public void associationtoDecidedzone(String sid, DecidedZone model);

}
