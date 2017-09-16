package cn.itcast.bos.dao.bc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.bc.DecidedZone;

public interface DecidedZoneDao extends JpaRepository<DecidedZone, String>, JpaSpecificationExecutor<DecidedZone> {

}
