package cn.itcast.bos.action.auth;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.auth.Function;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class FunctionAction extends BaseAction<Function> {
	@Action(value = "functionAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/admin/function.jsp") })
	public String save() {
		facadeService.getFunctionService().save(model);
		return "save";
	}

	@Action(value = "functionAction_pageQuery")
	public String pageQuery() {
		Page<Function> pageData = facadeService.getFunctionService().pageQuery(getPageRequest());
		setPageData(pageData);// 父类
		return "pageQuery";
	}

	@Action(value = "functionAction_ajaxList", results = { @Result(name = "ajaxList", type = "fastjson", params = { "includeProperties", "id,name" }) })
	public String ajaxList() {
		List<Function> pageData = facadeService.getFunctionService().findAll();
		push(pageData);
		return "ajaxList";
	}

}
