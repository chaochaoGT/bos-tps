package cn.itcast.bos.redis.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-redis.xml" })
public class UserRedis {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;// jdbcTemplate HibernateTemplate

	@Test // 存储数据
	public void test1() {
		// opsForValue存储简单数据类型
		redisTemplate.opsForValue().set("sh123", "上海", 300, TimeUnit.SECONDS);
		// redisTemplate.delete(key);
		System.out.println("set ok");
	}

	@Test // 存储数据
	public void test2() {
		User user = new User();
		user.setId(1);
		user.setEmail("eee@163.com");
		user.setPassword("1233");
		System.out.println("set ok");
	}

	@Test // 获取redis数据
	public void test3() {
		// String val = redisTemplate.opsForValue().get("sh123");
		// System.out.println("get ok --->" + val);
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("applicationContext-redis.xml");
		Object bean = c.getBean("redisTemplate");
		System.out.println(bean);
	}
}
