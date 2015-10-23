package @orgPath@.@prj@.common.redis.impl;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 
 * @author 肖冰
 *
 */
public  class AbstractRedisDao<T>{

	@Autowired
	protected RedisTemplate<String, T> redisTemplate;
	
	/**
	 * 设置redisTemplate
	 * 
	 * @param redisTemplate
	 *            the redisTemplate to set
	 */
	public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 获取 RedisSerializer
	 */
	protected RedisSerializer<java.lang.String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
	
	
	
	public boolean addEntityToRedis(final String id,final T session) {
		boolean result = false;
		result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] tempkey = serializer.serialize(id);
				byte[] sessionBytes = null;
				ByteArrayOutputStream baos = null;
				ObjectOutputStream oos = null;
				try {
					baos = new ByteArrayOutputStream();
					oos = new ObjectOutputStream(baos);
					oos.writeObject(session);
					sessionBytes = baos.toByteArray();
					oos.close();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != oos) {
							oos.close();
						}
					} catch (Exception ex) {
					}
					try {
						baos.close();
					} catch (Exception exx) {
					}
				}
				connection.set(tempkey, sessionBytes);
				return true;
			}
		});
		return result;
	}

	public T getEntityFromRedis(final String id) {
		
		T result = redisTemplate.execute(new RedisCallback<T>() {
					public T doInRedis(
							RedisConnection connection)
							throws DataAccessException {

						RedisSerializer<String> serializer = getRedisSerializer();
						T entity = null;
						byte[] tempkey = serializer.serialize(id);
						byte[] sessionBytes = connection.get(tempkey);
						if (sessionBytes == null) {
							return null;
						}
						ByteArrayInputStream bais = null;
						ObjectInputStream ois = null;
						try {
							bais = new ByteArrayInputStream(sessionBytes);
							ois = new ObjectInputStream(bais);
							entity = (T) ois.readObject();
							ois.close();
							bais.close();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (null != ois) {
									ois.close();
								}
							} catch (Exception ex) {
							}
							try {
								bais.close();
							} catch (Exception exx) {
							}
						}
						return entity;
					}
				});

		return result;
	}

	
	public boolean addStrMapStrToRedis(final String key, final String value,
			final long timeout) {
		boolean result = false;
//		System.out.println(key);
		result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] tempkey = serializer.serialize(key);
				try {
					byte[] valueBytes = value.getBytes("utf8");
					connection.set(tempkey, valueBytes);
					if(timeout>0){
						connection.expire(tempkey, timeout);
					}
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				return true;
			}
		});
		return result;
	}

	public String getStrObjFromRedis(final String key) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] tempkey = serializer.serialize(key);
				if (null == tempkey) {
					return null;
				}
				byte[] temp = connection.get(tempkey);
				if (null == temp || temp.length == 0) {
					return null;
				}
				String ucmid = null;
				try {
					ucmid = new String(temp, "utf8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return ucmid;
			}
		});

		return result;
	}

	public boolean expireKeyFromRedis(final String key) {
		boolean result = false;
		result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] tempkey = serializer.serialize(key);
				connection.del(tempkey);
				return true;
			}
		});

		return result;
	}

	public boolean resetExpireTimeInRedis(final String key, final long seconds) {
		
		boolean result = false;
		result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] tempkey = serializer.serialize(key);
				return connection.expire(tempkey, seconds);
			}
		});

		return result;
	}
	
}
