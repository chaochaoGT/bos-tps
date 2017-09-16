package cn.itcast.bos.service.bc.impl;

import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.DecidedZoneDao;
import cn.itcast.bos.dao.bc.SubareaDao;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.base.BaseInterface;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.mavencrm.domain.Customer;

@Service
@Transactional
@SuppressWarnings("all")
public class DecidedZoneServiceImpl implements DecidedZoneService {
	@Autowired
	private DecidedZoneDao decidedZoneDao;
	@Autowired
	private SubareaDao subareaDao;

	@Override
	public void save(String[] sid, DecidedZone model) {
		decidedZoneDao.save(model);
		if (sid != null && sid.length != 0) {
			for (String id : sid) {
				// 定区表关联分区 实现
				subareaDao.associationtoDecidedzone(id, model);
			}
		}
	}

	@Override
	public Page<DecidedZone> pageQuery(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		Page<DecidedZone> page = decidedZoneDao.findAll(pageRequest);
		List<DecidedZone> list = page.getContent();
		for (DecidedZone zone : list) {
			Hibernate.initialize(zone.getStaff());
		}
		return page;
	}

	@Override
	public void delBatch(String[] idsarr) {
		if (idsarr != null && idsarr.length != 0) {
			for (String did : idsarr) {
				DecidedZone decidedZone = decidedZoneDao.findOne(did);
				Set<Subarea> subareas = decidedZone.getSubareas();
				for (Subarea subarea : subareas) {
					subarea.setDecidedZone(null);// 解除关系
				}
				decidedZoneDao.delete(decidedZone);
			}
		}

	}

	@Override
	public List<Customer> findnoassociationcustomers() {
		String url = BaseInterface.CRM_BASE_URL + "noassociation";
		List<Customer> customers = (List<Customer>) WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return customers;
	}

	@Override
	public List<Customer> findassociationcustomers(String id) {
		String url = BaseInterface.CRM_BASE_URL + "findcustomerbydecidedzoneid/" + id;
		List<Customer> customers = (List<Customer>) WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return customers;
	}

	@Override
	public void assigncustomerstodecidedzone(String id, String[] customerIds) {
		String url = BaseInterface.CRM_BASE_URL + "assigencusotmertodecidedzone/" + id;
		if (customerIds != null && customerIds.length != 0) {
			StringBuilder sb = new StringBuilder();
			for (String cid : customerIds) {
				sb.append(cid).append(",");
			}
			String s = sb.substring(0, sb.length() - 1);// 1,2,3
			url = url + "/" + s;
		} else {
			url = url + "/tps";// 如果以tps出现 表示当前定区没有选择客户信息
		}

		WebClient.create(url).put(null);
	}

}
