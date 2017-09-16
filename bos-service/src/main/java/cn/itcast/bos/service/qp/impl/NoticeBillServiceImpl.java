package cn.itcast.bos.service.qp.impl;

import java.sql.Date;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.DecidedZoneDao;
import cn.itcast.bos.dao.bc.RegionDao;
import cn.itcast.bos.dao.qp.NoticeBillDao;
import cn.itcast.bos.dao.qp.WorkBillDao;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.qp.WorkBill;
import cn.itcast.bos.service.base.BaseInterface;
import cn.itcast.bos.service.qp.NoticeBillService;
import cn.itcast.mavencrm.domain.Customer;

@Service
@Transactional
public class NoticeBillServiceImpl implements NoticeBillService {
	@Autowired
	private NoticeBillDao noticeBillDao;
	@Autowired
	private DecidedZoneDao decidedZoneDao;
	@Autowired
	private WorkBillDao workBillDao;
	@Autowired
	private RegionDao regionDao;
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;// 通过@Qualifier修饰符来注入对应的bean

	@Override
	public Customer findCustomerByTelephone(String telephone) {
		// CRM
		String url = BaseInterface.CRM_BASE_URL + "findcustomerbytelephone/" + telephone;
		Customer c = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		return c;
	}

	@Override
	public void save(final NoticeBill model, String province, String city, String district) {
		// 业务通知单录入
		boolean flag = false;// 作用: 控制crm系统老客户地址是否更新
		noticeBillDao.saveAndFlush(model);// saveAndFlush model瞬时 --->持久态 model OID
		final String address = model.getPickaddress();// 阿里大于发送短信有长度限制: 地址不宜过长
		model.setPickaddress(province + city + district + model.getPickaddress());
		// 2: 取派员 自动分单 成功 分单类型 自动! 如果自动分单失败 staff null 分单类型人工
		// 地址库完全匹配
		String url = BaseInterface.CRM_BASE_URL + "findcustomerbyaddress/" + model.getPickaddress();
		Customer c = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (c != null) {
			// 根据地址查询客户信息
			String decidedzoneId = c.getDecidedzoneId();// 断点...
			// System.out.println(decidedzoneId + "----------------------");
			if (StringUtils.isNotBlank(decidedzoneId)) {
				// 客户已经被关联
				DecidedZone decidedZone = decidedZoneDao.findOne(decidedzoneId);
				final Staff staff = decidedZone.getStaff();
				model.setStaff(staff);
				model.setOrdertype("自动");
				// 自动分单已经成功 生成一张工单
				generateWorkBill(model, staff);
				// 发送短信mq
				jmsTemplate.send("bos_staff", new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						MapMessage mapMessage = session.createMapMessage();
						mapMessage.setString("customername", model.getCustomerName());
						mapMessage.setString("customertel", model.getTelephone());
						mapMessage.setString("customeraddr", address);
						mapMessage.setString("stafftelephone", staff.getTelephone());
						return mapMessage;
					}
				});
				// 地址库完全匹配成功 说明用户地址没有更改 所以crm没有必要去更新客户的地址
				flag = true;
				System.out.println("----地址库完全匹配法-----------------");
				crmCustomer(model, flag);// 不需要更新客户地址和定区id
				return;
			}
		}
		// 自动分单 规则2 管理分区匹配法
		// 省市区 查询Region---subareas----> 关键字模糊匹配 --->Subarea --->Decidedzone--->Staff
		Region region = regionDao.findRegionByProvinceAndCityAndDistrict(province, city, district);
		System.out.println("管理分区匹配法 省市区信息" + region.getProvince() + region.getCity() + region.getDistrict());
		Set<Subarea> subareas = region.getSubareas();
		if (subareas != null && subareas.size() != 0) {
			for (Subarea sub : subareas) {
				if (model.getPickaddress().contains(sub.getAddresskey())) {
					DecidedZone zone = sub.getDecidedZone();
					if (zone != null) {
						final Staff staff = zone.getStaff();
						model.setStaff(staff);
						model.setOrdertype("自动");
						generateWorkBill(model, staff);// 生成工单
						// 发送短信mq
						jmsTemplate.send("bos_staff", new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								MapMessage mapMessage = session.createMapMessage();
								mapMessage.setString("customername", model.getCustomerName());
								mapMessage.setString("customertel", model.getTelephone());
								mapMessage.setString("customeraddr", address);
								mapMessage.setString("stafftelephone", staff.getTelephone());
								return mapMessage;
							}
						});
						System.out.println("----管理分区匹配法-----------------");
						flag = false;
						crmCustomer(model, flag);// 录入客户信息 更改客户地址 定区id置null
						return;
					}

				}
			}
		}
		// 自动分单失败
		crmCustomer(model, flag);// 如果自动分单失败,那么我们根据flag 需要更新crm系统客户信息
		// 老客户如果地址相同不需要修改地址 定区id不变
		// 如果是老客户 地址不同则需要更新crm地址库该用户地址 定区id需要置null重新关联定区和客户关系
		// 如果是新客户 直接插入用户信息即可
		model.setOrdertype("人工");

	}

	/**
	 * 生成工单
	 * 
	 * @param model
	 * @param staff
	 */
	private void generateWorkBill(final NoticeBill model, final Staff staff) {
		WorkBill bill = new WorkBill();
		bill.setAttachbilltimes(0);
		bill.setBuildtime(new Date(System.currentTimeMillis()));
		bill.setNoticeBill(model);
		bill.setType("新");
		bill.setStaff(staff);
		bill.setRemark(model.getRemark());
		bill.setPickstate("新单");
		workBillDao.save(bill);
	}

	// crm系统客户信息的处理
	private void crmCustomer(final NoticeBill model, boolean flag) {
		System.out.println(model.getId() + "=================" + String.valueOf(model.getCustomerId()));
		// 1 : 客户是否是一个新客户 crm录入 返回 customerId
		if (!("null".equalsIgnoreCase(String.valueOf(model.getCustomerId())))) {
			if (!flag) {
				// flag = false 需要修改crm地址 客户关联的定区id要置null
				// 老客户 更新crm地址
				System.out.println("-----------crm----------老客户地址修改了 !");
				String urlupdate = BaseInterface.CRM_BASE_URL + "updateadressbyid/" + model.getCustomerId() + "/" + model.getPickaddress();
				WebClient.create(urlupdate).put(null);
			}

		} else {
			// 新客户 crm系统录入客户信息 返回Cusotmer id String.valueOf(model.getCustomerId()) 新客户id是null字符串
			String urlsave = BaseInterface.CRM_BASE_URL + "save";
			Customer customer = new Customer();
			customer.setName(model.getCustomerName());
			customer.setAddress(model.getPickaddress());
			customer.setDecidedzoneId(null);
			customer.setStation("传智播客");
			customer.setTelephone(model.getTelephone());
			Response post = WebClient.create(urlsave).accept(MediaType.APPLICATION_JSON).post(customer);
			// 响应体 获取实体模型
			Customer entity = post.readEntity(Customer.class);// 主键从crm系统获取
			model.setCustomerId(entity.getId());// noticebill --->customerId
		}
	}

}
