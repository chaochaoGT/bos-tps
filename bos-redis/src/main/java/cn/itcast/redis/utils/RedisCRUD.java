package cn.itcast.redis.utils;

import com.alibaba.fastjson.serializer.SerializeFilter;

/**
 * redis数据库操作json字符串的crud
 * 
 * @author tps
 *
 */
public interface RedisCRUD {
	/**
	 * 基于fastjson完成json字符串存储到redis数据库上
	 * 
	 * @param key
	 * @param jsonString
	 */
	public void writeJSONStringToRedis(String key, String jsonString);

	public void writeObjectToRedisByFastJSON(String key, Object obj);

	public void writeObjectToRedisByFastJSONIncludes(String key, Object obj, SerializeFilter filter);

	public void writeObjectToRedisByFastJSONIncludes(String key, Object obj, String... inproperties);

	public String GetJSONStringFromRedis(String key);

	public void deleteJSONStringFromRedisByKey(String key);

}
