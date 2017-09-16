package cn.itcast.bos.action.auth;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.auth.Menu;
import cn.itcast.bos.domain.user.User;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class MenuAction extends BaseAction<Menu> {
	@Action(value = "menuAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/admin/menu.jsp") })
	public String save() {
		if (model.getMenu() == null || StringUtils.isBlank(model.getMenu().getId())) {
			model.setMenu(null);
		}
		facadeService.getMenuService().save(model);
		return "save";
	}

	@Action(value = "menuAction_ajaxListHasSonMenus", results = { @Result(name = "ajaxListHasSonMenus", type = "fastjson", params = { "includeProperties", "id,name" }) })
	public String ajaxListHasSonMenus() {
		List<Menu> menus = facadeService.getMenuService().ajaxListHasSonMenus();
		push(menus);
		return "ajaxListHasSonMenus";
	}

	@Action(value = "menuAction_ajaxList", results = { @Result(name = "ajaxList", type = "fastjson", params = { "includeProperties", "id,name,pId,page" }) })
	public String ajaxList() {
		List<Menu> menus = facadeService.getMenuService().ajaxList();
		push(menus);
		return "ajaxList";
	}

	@Action(value = "menuAction_findMenuByUserId", results = { @Result(name = "findMenuByUserId", type = "fastjson", params = { "includeProperties", "id,name,pId,page" }) })
	public String findMenuByUserId() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		List<Menu> menus = facadeService.getMenuService().findMenuByUserId(user.getId());
		push(menus);
		return "findMenuByUserId";
	}

	@Action(value = "menuAction_pageQuery")
	public String pageQuery() {
		setPage(Integer.parseInt(getParameter("page")));// struts2模型驱动>属性注入
		Page<Menu> pageData = facadeService.getMenuService().pageQuery(getPageRequest());
		setPageData(pageData);// 父类
		return "pageQuery";
	}

}
