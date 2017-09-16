package cn.itcast.bos.action.qp;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.user.User;
import cn.itcast.mavencrm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class NoticeBillAction extends BaseAction<NoticeBill> {
	@Action(value = "noticeBillAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/qupai/noticebill_add.jsp") })
	public String save() {
		User user = (User) getSessionAttribute("existUser");// 受理人
		// 客户id 业务层封装
		model.setUser(user);
		String province = getParameter("nprovince");
		String city = getParameter("ncity");
		String district = getParameter("ndistrict");
		// model.setPickaddress(province + city + district + model.getPickaddress());// 阿里大于的短信长度限制...在这里不需要封装
		// 2: 调用业务层完成业务通知单录入 以及自动分单的实现
		facadeService.getNoticeBillService().save(model, province, city, district);
		// 3: 业务层 3.1 业务通知单录入 3.2 自动分单 (地址库完全匹配/管理分区匹配法) 3.3 客户新客户 crm(插入)录入 3.4 老客户 address更新
		return "save";
	}

	// @Action(value = "standardAction_pageQuery")
	// public String pageQuery() {
	// Page<Standard> pageData = facadeService.getStandardService().pageQuery(getPageRequest());
	// setPageData(pageData);// 父类
	// return "pageQuery";
	// }

	@Action(value = "noticeBillAction_findCustomerByTelephone", results = { @Result(name = "findCustomerByTelephone", type = "fastjson") })

	public String findCustomerByTelephone() {
		// 查询数据库 List<Standard> 序列化
		Customer c = facadeService.getNoticeBillService().findCustomerByTelephone(model.getTelephone());
		push(c);// name
		return "findCustomerByTelephone";
	}

}
