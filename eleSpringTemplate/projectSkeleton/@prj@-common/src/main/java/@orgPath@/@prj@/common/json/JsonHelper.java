package @orgPath@.@prj@.common.json;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 将一个对象，转换为json
	 * @param obj
	 * @return
	 */
	public static String transObjToJsonString(Object obj){
		String str;
		try {
			str = mapper.writeValueAsString(obj);
			return str;
		} catch (IOException e) {
			LOGGER.error("json转换失败,转换的源对象为{}", obj);
		}
		return null;
	}
	
	/**
	 * Json转换为对象
	 * @param json
	 * @param cla
	 * @return
	 */
	public static <T> T transJsonStringToObj(String json, Class<T> cla){
		try {
			T t = mapper.readValue(json, cla);
			return t;
		} catch (IOException e) {
			LOGGER.error("json转换失败");
		}
		return null;
	}
	
}
