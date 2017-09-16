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
import cn.itcast.bos.domain.auth.Role;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class RoleAction extends BaseAction<Role> {
	@Action(value = "roleAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/admin/role.jsp") })
	public String save() {
		// 接受菜单ids
		String menuids = getParameter("menuIds");// 1,2,3,4 model Role 主键uuid
		String[] functionIds = getRequest().getParameterValues("functionIds");
		facadeService.getRoleService().save(model, menuids, functionIds);
		return "save";
	}

	// @Action(value = "menuAction_ajaxListHasSonMenus", results = { @Result(name = "ajaxListHasSonMenus", type = "fastjson", params = { "includeProperties", "id,name" }) })
	// public String ajaxListHasSonMenus() {
	// List<Menu> menus = facadeService.getMenuService().ajaxListHasSonMenus();
	// push(menus);
	// return "ajaxListHasSonMenus";
	// }
	//
	@Action(value = "roleAction_ajaxList", results = { @Result(name = "ajaxList", type = "fastjson", params = { "includeProperties", "id,name" }) })
	public String ajaxList() {
		List<Role> roles = facadeService.getRoleService().ajaxList();
		push(roles);
		return "ajaxList";
	}

	@Action(value = "roleAction_pageQuery")
	public String pageQuery() {
		Page<Role> pageData = facadeService.getRoleService().pageQuery(getPageRequest());
		setPageData(pageData);// 父类
		return "pageQuery";
	}

}
