package cn.itcast.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

@Repository
public class RedisCRUDImpl implements RedisCRUD {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void writeJSONStringToRedis(String key, String jsonString) {
		redisTemplate.opsForValue().set(key, jsonString);
	}

	public void writeObjectToRedisByFastJSON(String key, Object obj) {
		String jsonString = JSON.toJSONString(obj);
		redisTemplate.opsForValue().set(key, jsonString);
	}

	public void writeObjectToRedisByFastJSONIncludes(String key, Object obj, SerializeFilter filter) {
		String jsonString = JSON.toJSONString(obj, filter);
		redisTemplate.opsForValue().set(key, jsonString);
	}

	public void writeObjectToRedisByFastJSONIncludes(String key, Object obj, String... inproperties) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(inproperties);
		String jsonString = JSON.toJSONString(obj, filter);
		redisTemplate.opsForValue().set(key, jsonString);
	}

	public String GetJSONStringFromRedis(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void deleteJSONStringFromRedisByKey(String key) {
		// TODO Auto-generated method stub
		redisTemplate.delete(key);
	}

}
