package cn.itcast.bos.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.facade.FacadeService;

/**
 * 连接数据库 获取用户相关数据
 * 
 * @author tps 访问数据库 调用业务层 shiro模块 依赖业务层
 */
public class BosRealm extends AuthorizingRealm {
	@Autowired
	private FacadeService facadeService;

	// 根据当前用户 查询当前用户 对应角色或者权限 关键字 --->AuthorizationInfo
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权---");
		// 1:当前用户信息
		Subject subject = SecurityUtils.getSubject();
		User existUser = (User) subject.getPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 2: 分 超级管理员登录 和 非超管登录 显示菜单不同
		if ("tps520wx@163.com".equalsIgnoreCase(existUser.getEmail())) {
			// 超级管理员 拥有所有角色和所有权限
			List<Role> roles = facadeService.getRoleService().findAll();
			List<Function> functions = facadeService.getFunctionService().findAll();
			for (Role role : roles) {
				info.addRole(role.getCode());
			}
			for (Function function : functions) {
				info.addStringPermission(function.getCode());
			}

		} else {
			// 非超级管理员 用户该用户对应角色和对应权限
			List<Role> roles = facadeService.getRoleService().findRolesByUserId(existUser.getId());
			for (Role role : roles) {
				info.addRole(role.getCode());
				Set<Function> functions = role.getFunctions();
				for (Function function : functions) {
					info.addStringPermission(function.getCode());
				}
			}
		}

		return info;
	}

	/**
	 * 认证完成!
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证---");
		// 该方法如果返回值 null 认证失败 如果返回值AuthenticationInfo 认证成功
		UsernamePasswordToken mytoken = (UsernamePasswordToken) token;
		String email = mytoken.getUsername();// 表单提交的账号
		// 密码 认证信息 shiro 和数据库比较 流程 : 通过账号 查询数据库 一旦查询到用户信息 可以获取数据库用户真实密码
		// 和令牌密码比对 shiro内部比对密码的!
		User existUser = facadeService.getUserService().findUserByEmail(email);
		if (existUser == null) {
			return null;
		} else {
			// shiro 比对 密码 数据库密码提供即可
			// 参数一 用户对象 后续 通过Subject.getPricinpal()获取 existUser对象
			// 参数二 : 证书 数据库密码
			// 参数三 : realm注册名称 id值 通过 super.getName() 获取注册realm名称
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(existUser, existUser.getPassword(), super.getName());
			return info;
		}

	}

}
