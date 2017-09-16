package cn.itcast.bos.action.user;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.utils.RandStringUtils;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("mavenbos") // 值栈 中转站 业务需求需要数据 放到 root request session app.....setAttribute()
public class UserAction extends BaseAction<User> {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;// redisTemplate
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;// 通过@Qualifier修饰符来注入对应的bean
	// userAction_validCheckCode
	// 验证码ajax校验

	@Action(value = "userAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/admin/userlist.jsp") })
	public String save() {
		String[] roleIds = getRequest().getParameterValues("roleIds");
		facadeService.getUserService().save(model, roleIds);
		return "save";
	}

	@Action(value = "userAction_pageQuery")
	public String pageQuery() {
		Page<User> pageData = facadeService.getUserService().pageQuery(getPageRequest());
		setPageData(pageData);// 父类
		return "pageQuery";
	}

	@Action(value = "userAction_validCheckCode", results = { @Result(name = "validCheckCode", type = "json") })
	public String validCheckCode() {
		String checkcode = getParameter("checkcode");
		String session_checkcode = (String) getSessionAttribute("key");
		if (checkcode.equalsIgnoreCase(session_checkcode)) {
			push(true);// 压入栈顶 json结果集源码 执行execute (没有配置root的情况下)将 栈顶元素进行json序列化
		} else {
			push(false);
		}
		return "validCheckCode";
	}

	// 找回密码 更新数据库
	@Action(value = "userAction_gobackpassword", results = { @Result(name = "gobackpassword", type = "json") })
	public String gobackpassword() {
		try {
			facadeService.getUserService().gobackpassword(model.getTelephone(), model.getPassword());
			// redisTemplate.delete(model.getTelephone()); // 手动清除redis指定手机号对应的验证码
			push(true);
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "gobackpassword";
	}

	// 密码找回 验证码校验 0 验证按不存在 1 验证码输入错误 2 手机号错误 3 有效
	@Action(value = "userAction_smsPassword", results = { @Result(name = "smsPassword", type = "json") })
	public String smsPassword() {
		// 1: 手机号是否存在系统中
		User existUser = facadeService.getUserService().findUserByTelephone(model.getTelephone());
		if (existUser == null) {
			// 数据库不存在
			push("2");
		} else {
			// 2:
			String checkcode = getParameter("checkcode");// 用户输入的验证码
			// redis验证码
			String redisCode = redisTemplate.opsForValue().get(model.getTelephone());
			if (StringUtils.isNotBlank(redisCode)) {
				// redis 存储用户验证码 没有失效
				if (redisCode.equals(checkcode)) {
					// 验证码正确
					push("3");
				} else {
					push("1");
				}
			} else {
				push("0");
			}
		}
		return "smsPassword";
	}

	// shiro 完成认证操作
	@Action(value = "userAction_login", results = { @Result(name = "login_error", location = "/login.jsp"), @Result(name = "login_ok", type = "redirect", location = "/index.jsp"), @Result(name = "input", location = "/login.jsp") })
	public String login() {
		// 一次性验证码
		removeSessionAttribute("key");
		// 第一步 获取Subject对象 SecurityUtils提供对象获取Subject
		try {
			Subject subject = SecurityUtils.getSubject();
			// subject.logout();// Subject保存客户信息移除
			// subject.getPrincipal() 获取用户信息 User对象 == session.getAttribute("existUser")
			UsernamePasswordToken token = new UsernamePasswordToken(model.getEmail(), model.getPassword());
			subject.login(token);// login 登录方法 该方法 如果异常 认证失败 ...如果方法没有异常 认证成功
			return "login_ok";
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			this.addActionError(this.getText("login.password.error"));// 国际化配置文件加载该数据
			return "login_error";
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			this.addActionError(this.getText("login.email.error"));// 国际化配置文件加载该数据
			return "login_error";
		}
	}
	// 原来的登录业务
	// @Action(value = "userAction_login", results = { @Result(name = "login_error", location = "/login.jsp"), @Result(name = "login_ok", type = "redirect", location = "/index.jsp"), @Result(name = "input", location = "/login.jsp") })
	// public String login() {
	// User existUser = facadeService.getUserService().findUserByUsernameAndPassword(model.getEmail(), model.getPassword());
	// // 一次性验证码
	// removeSessionAttribute("key");
	// if (existUser == null) {
	// this.addActionError(this.getText("login.emailorpassword.error"));// 国际化配置文件加载该数据
	// return "login_error";
	// } else {
	// setSessionAttribute("existUser", existUser);
	// return "login_ok";
	// }
	// }

	// 发送验证码 mq redis存储
	@Action(value = "userAction_sendSms", results = { @Result(name = "sendSms", type = "json") })
	public String sendSms() {
		// 生成验证码 获取手机号 redis 存储 发送mq
		// 发送mq
		try { // 发送验证码操作
			final String code = RandStringUtils.getCode();// 生成验证码
			redisTemplate.opsForValue().set(model.getTelephone(), code, 120, TimeUnit.SECONDS);// 120秒 有效找回 redis保存验证码
			System.out.println(code + "----------短信密码找回验证码----------");
			// SmsSystem.sendSms(code, model.getTelephone());// 没有ACTIVEMQ 单线程发送给用户手机
			// 调用ActiveMQ jmsTemplate，发送一条消息给MQ
			jmsTemplate.send("bos_sms", new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("telephone", model.getTelephone());
					mapMessage.setString("msg", code);
					return mapMessage;
				}
			});
			push(true);
		} catch (Exception e) {
			push(false);
		}

		return "sendSms";
	}
}
