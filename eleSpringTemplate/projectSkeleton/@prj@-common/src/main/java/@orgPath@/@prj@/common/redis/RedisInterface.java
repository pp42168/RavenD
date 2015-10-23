package @orgPath@.@prj@.common.redis;



public interface RedisInterface<T> {
	public boolean addEntityToRedis(final String id,final T session);

	public T getEntityFromRedis(final String id);

	
	public boolean addStrMapStrToRedis(final String key, final String value,
			final long timeout);

	public String getStrObjFromRedis(final String key);

	public boolean expireKeyFromRedis(final String key);

	public boolean resetExpireTimeInRedis(final String key, final long seconds);
	
}
