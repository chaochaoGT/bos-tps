package cn.itcast.bos.action.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.service.facade.FacadeService;
import cn.itcast.bos.utils.DownLoadUtils;

// 复用 Action 代码
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	protected T model;

	public T getModel() {
		return model;
	}

	// 分页查询结果集抽取

	private Page<T> pageData;// 获取分页结果集数据
	// 子类调用

	public void setPageData(Page<T> pageData) {
		this.pageData = pageData;
	}

	// 父类封装map 存入值栈即可 json-plugin 结果集 调用getXXX方法 配置root= obj
	public Object getObj() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		return map;
	}

	// 请求对象封装 子类调用使用
	public PageRequest getPageRequest() {
		PageRequest pageRequest = new PageRequest(page - 1, rows);
		return pageRequest;
	}

	// 注入门面类
	@Autowired
	protected FacadeService facadeService;

	public BaseAction() {
		// 对model进行实例化， 通过子类 类声明的泛型
		Type superclass = this.getClass().getGenericSuperclass();
		// 转化为参数化类型
		ParameterizedType parameterizedType = (ParameterizedType) superclass;
		// 获取一个泛型参数
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 获取请求参数
	public String getParameter(String name) {
		return ServletActionContext.getRequest().getParameter(name);
	}

	// 获取Session Attribute
	public Object getSessionAttribute(String name) {
		return ServletActionContext.getRequest().getSession().getAttribute(name);
	}

	// 向session保存属性
	public void setSessionAttribute(String name, Object value) {
		ServletActionContext.getRequest().getSession().setAttribute(name, value);
	}

	// 向session移除对象
	public void removeSessionAttribute(String name) {
		ServletActionContext.getRequest().getSession().removeAttribute(name);
	}

	// 值栈操作 后续子类actions操作
	public void push(Object obj) {
		ActionContext.getContext().getValueStack().push(obj);// root
	}

	public void set(String key, Object obj) {
		ActionContext.getContext().getValueStack().set(key, obj);// root 将一个map集合 存放在栈顶
	}

	// 目标数据存放 值栈 下方 map结构
	public void put(String key, Object obj) {
		ActionContext.getContext().getValueStack().getContext().put(key, obj);
	}

	// json序列化 ....

	// 下载 封装 response对象
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	// 两个头 一个流 下载需要文件名和浏览器类型
	public void download(String filename, String path) {
		HttpServletResponse response = getResponse();
		try {
			ServletContext context = ServletActionContext.getServletContext();
			response.setHeader("Content-Disposition", "attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			response.setContentType(context.getMimeType(filename));
			ServletOutputStream outputStream = response.getOutputStream();
			InputStream in = new FileInputStream(path);
			int len;
			byte[] bytes = new byte[1024 * 8];
			while ((len = in.read(bytes)) != -1) {
				outputStream.write(bytes, 0, len);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 分页请求属性驱动 菜单分页查询 page封装不进来... struts2框架 封装请求参数 优先级 模型驱动>属性驱动
	protected int page; // 页码
	protected int rows; // 每页 记录数

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
}
