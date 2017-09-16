package cn.itcast.fastjson;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import cn.itcast.bos.domain.user.User;

public class FastJsonDemo {

	public static void main(String[] args) {
		// run1();
		// run2();
		run3();
		// json---->对象 反序列化

	}

	private static void run3() {
		// TODO Auto-generated method stub
		User user = new User();
		user.setId(1);
		user.setEmail("eee@163.com");
		user.setPassword("1233");
		// 序列化两个参数 id email
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(User.class);
		Set<String> excludes = filter.getExcludes();// 集合 存放不需要序列化字段 id,name
		Set<String> includes = filter.getIncludes();// 集合 存储需要序列化字段
		// excludes.add("email");
		includes.add("email");
		String jsonString = JSON.toJSONString(user, filter);
		System.out.println(jsonString);
	}

	private static void run2() {
		// 属性过滤序列化
		User user = new User();
		user.setId(1);
		user.setEmail("eee@163.com");
		user.setPassword("1233");
		// 序列化两个参数 id email
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(User.class, "email", "id");

		String jsonString = JSON.toJSONString(user, filter);
		System.out.println(jsonString);
	}

	private static void run1() {
		// TODO Auto-generated method stub
		// 对象 --->json 序列化
		User user = new User();
		user.setId(1);
		user.setEmail("eee@163.com");
		user.setPassword("1233");

		// 找对象
		String json = JSON.toJSONString(user);
		System.out.println(json);

	}

}
