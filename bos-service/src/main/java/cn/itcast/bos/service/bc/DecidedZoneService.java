package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.mavencrm.domain.Customer;

public interface DecidedZoneService {

	void save(String[] sid, DecidedZone model);

	Page<DecidedZone> pageQuery(PageRequest pageRequest);

	void delBatch(String[] idsarr);

	List<Customer> findnoassociationcustomers();

	List<Customer> findassociationcustomers(String id);

	void assigncustomerstodecidedzone(String id, String[] customerIds);

}
