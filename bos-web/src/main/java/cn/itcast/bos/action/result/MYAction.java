package cn.itcast.bos.action.result;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos")
public class MYAction extends ActionSupport {
	@Action(value = "myAction_demo", results = { @Result(name = "demo", type = "fastjson", params = { "includes", "id,name" }) })
	public String demo() {
		System.out.println("hehe action 执行....");
		return "demo";
	}
}
