package cn.itcast.bos.action.city;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.city.City;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class LoadCityAction extends BaseAction<City> {
	// @Action(value = "loadCityAction_load", results = { @Result(name = "load", type = "json") })
	// public String load() {
	// // 1: 获取请求的Pid
	// List<City> citys = facadeService.getCityService().findAll(model.getPid());
	// System.out.println("----------" + citys);
	// push(citys);
	// return "load";
	// }
	@Action(value = "loadCityAction_load", results = { @Result(name = "load", type = "json") })
	public String load() {
		// 1: 获取请求的Pid
		try {
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			String fromRedis = facadeService.getCityService().findCityByPidFromRedis(model.getPid());
			response.getWriter().println(fromRedis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
}
