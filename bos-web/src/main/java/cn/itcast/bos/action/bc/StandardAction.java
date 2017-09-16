package cn.itcast.bos.action.bc;

import java.sql.Timestamp;
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
import cn.itcast.bos.domain.bc.Standard;
import cn.itcast.bos.domain.user.User;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class StandardAction extends BaseAction<Standard> {
	@Action(value = "standardAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/standard.jsp") })
	public String save() {
		// 添加 又是修改
		try {
			if (StringUtils.isBlank(model.getDeltag() + "")) {
				// 添加 操作 收派标准获取null会覆盖实体类Standard 里面的属性deltag 初始值
				// 所以添加操作 Standard 手动给予赋值 1
				model.setDeltag(1);
			}
			User existUser = (User) getSessionAttribute("existUser");
			model.setOperator(existUser.getEmail());
			model.setOperatorcompany(existUser.getStation());
			model.setOperationtime(new Timestamp(System.currentTimeMillis()));
			facadeService.getStandardService().save(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	@Action(value = "standardAction_pageQuery")
	public String pageQuery() {
		Page<Standard> pageData = facadeService.getStandardService().pageQuery(getPageRequest());
		setPageData(pageData);// 父类
		return "pageQuery";
	}

	@Action(value = "standardAction_ajaxListName", results = { @Result(name = "ajaxListName", type = "fastjson", params = { "includeProperties", "name" }) })

	public String ajaxListName() {
		// 查询数据库 List<Standard> 序列化
		List<Standard> standards = facadeService.getStandardService().findAllInUse();
		push(standards);// name
		return "ajaxListName";
	}

	@Action(value = "standardAction_delBatch", results = { @Result(name = "delBatch", type = "json") })
	public String delBatch() {
		try {
			String ids = getParameter("ids");
			if (StringUtils.isNotBlank(ids)) {
				String[] idsarr = ids.split(",");
				facadeService.getStandardService().delBatch(idsarr);
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
}
