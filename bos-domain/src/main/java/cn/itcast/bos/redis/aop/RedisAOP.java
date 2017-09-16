package cn.itcast.bos.redis.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RedisAOP {
	@Around(value = "execution(* cn.itcast.bos.service.bc.impl.*.pageQueryBy*(..))")
	public Object around(ProceedingJoinPoint point) {

		Object object = null;
		try {
			object = point.proceed();
			System.out.println("------------aop------------------");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return object;
	}
}
