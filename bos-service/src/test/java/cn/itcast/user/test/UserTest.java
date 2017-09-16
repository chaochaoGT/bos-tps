package cn.itcast.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.facade.FacadeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-domain.xml", "classpath:applicationContext-dao.xml", "classpath:applicationContext-service.xml" })
public class UserTest {
	// @Autowired
	// private UserService userService;
	@Autowired
	private FacadeService facadeService;

	// @Test
	public void test1() {
		User user = new User();
		user.setEmail("tps@163.com");
		user.setPassword("111");
		user.setTelephone("12211");
		facadeService.getUserService().save(user);
	}

	@Test
	public void test2() {
		System.out.println(facadeService.getUserService().findUserByUsernameAndPassword("tps@163.com", "111"));
	}
}
