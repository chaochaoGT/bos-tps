package cn.itcast.bos.service.qp;

import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.mavencrm.domain.Customer;

public interface NoticeBillService {

	Customer findCustomerByTelephone(String telephone);

	void save(NoticeBill model, String province, String city, String district);

}
