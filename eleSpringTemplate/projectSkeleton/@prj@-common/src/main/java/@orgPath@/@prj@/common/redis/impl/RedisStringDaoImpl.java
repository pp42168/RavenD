package @orgPath@.@prj@.common.redis.impl;


import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import @orgPath@.@prj@.common.redis.RedisStringDao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;




@Repository
public class RedisStringDaoImpl extends AbstractRedisDao<String>
		implements RedisStringDao {

	private static final long serialVersionUID = 1L;

	@Override
	public Set<byte[]> getAllKeys(final String key) {
		
		Set<byte[]> result = redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(
					RedisConnection connection)
					throws DataAccessException {

				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] tempkey = serializer.serialize(key);
				Set<byte[]> keys = connection.keys(tempkey);
				return keys;
			
			}
		});

		return result;
	}

	@Override
	public Map<byte[], byte[]> getMapObj(final byte[] key) {
		Map<byte[], byte[]> result = redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
			@Override
			public Map<byte[], byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Map<byte[], byte[]> map = connection.hGetAll(key);

				return map;
			}
		});

		return result;

	}
}
