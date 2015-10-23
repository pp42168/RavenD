package @orgPath@.@prj@.common.redis;


import java.util.Map;
import java.util.Set;


public interface RedisStringDao extends RedisInterface<String>{

	Set<byte[]> getAllKeys(String key);
	Map<byte[], byte[]> getMapObj(byte[] key);
}
