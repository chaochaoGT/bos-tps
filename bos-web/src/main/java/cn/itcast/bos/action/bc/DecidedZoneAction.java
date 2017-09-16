package cn.itcast.bos.action.bc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.mavencrm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class DecidedZoneAction extends BaseAction<DecidedZone> {
	private String[] sid;

	public void setSid(String[] sid) {
		this.sid = sid;
	}

	@Action(value = "decidedZoneAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/decidedzone.jsp") })
	public String save() {
		// 接受sid数组
		try {
			// getRequest().getParameterValues("sid")
			facadeService.getDecidedZoneService().save(sid, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	@Action(value = "decidedZoneAction_pageQuery")
	public String pageQuery() {
		Page<DecidedZone> pageData = facadeService.getDecidedZoneService().pageQuery(getPageRequest());
		setPageData(pageData);// 父类
		return "pageQuery";
	}

	@Action(value = "decidedZoneAction_delBatch", results = { @Result(name = "delBatch", type = "json") })
	public String delBatch() {
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] idsarr = ids.split(",");
				facadeService.getDecidedZoneService().delBatch(idsarr);
				push(true);
			} else {
				push(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "delBatch";
	}

	@Action(value = "decidedZoneAction_findnoassociationcustomers", results = { @Result(name = "findnoassociationcustomers", type = "fastjson", params = { "includeProperties", "id,name" }) })
	public String findnoassociationcustomers() {
		List<Customer> customers = facadeService.getDecidedZoneService().findnoassociationcustomers();
		push(customers);
		return "findnoassociationcustomers";
	}

	@Action(value = "decidedZoneAction_findassociationcustomers", results = { @Result(name = "findassociationcustomers", type = "fastjson", params = { "includeProperties", "id,name" }) })
	public String findassociationcustomers() {
		List<Customer> customers = facadeService.getDecidedZoneService().findassociationcustomers(model.getId());
		push(customers);
		return "findassociationcustomers";
	}

	@Action(value = "decidedZoneAction_assigncustomerstodecidedzone", results = { @Result(name = "assigncustomerstodecidedzone", location = "/WEB-INF/pages/base/decidedzone.jsp") })
	public String assigncustomerstodecidedzone() {
		// 获取定区id 和 选中右边所有客户ids
		String[] customerIds = getRequest().getParameterValues("customerIds");
		facadeService.getDecidedZoneService().assigncustomerstodecidedzone(model.getId(), customerIds);
		return "assigncustomerstodecidedzone";
	}

}
