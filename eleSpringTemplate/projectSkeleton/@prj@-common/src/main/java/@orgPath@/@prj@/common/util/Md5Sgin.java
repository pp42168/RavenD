package @orgPath@.@prj@.common.util;


import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

public class Md5Sgin {
	
	private static String authKey = "-----一个自定义key-----";

	public static String generateSignatureByMsg(String jsonMsg) throws Exception{//生成签名方法
		HashMap<String,Object> map=convertToMap(jsonMsg);
		if(map.containsKey("sign")){
			map.remove("sign");// 移除个人签名
		}
		map.put("authKey", authKey);
		return generateSignature(map);
	}
	
	public static String generateSignatureByMsgForCheck(String jsonStr) throws Exception {//验证签名方法
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonStr);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> objMap = mapper.convertValue(jsonNode,HashMap.class);
		@SuppressWarnings("unchecked")
		HashMap<String,Object> mapBody=(HashMap<String, Object>) objMap.get("body");
		mapBody.remove("sign");
		String verifySign=generateSignature(mapBody);
		return verifySign;
	}
	
	public static HashMap<String, Object> convertToMap(String msg) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(msg);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> objMap = mapper.convertValue(jsonNode,
				HashMap.class);
		return objMap;
	}
	
	public static String generateSignature( Map<String,Object> parameters){
        StringBuilder str = new StringBuilder();
        List<String> sorted = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
        	if(entry.getValue()!=null){
        		if (entry.getValue() instanceof List) {
					Object[] objArr=((List)entry.getValue()).toArray();
					LinkedHashMap<String, Object> tmpMap=null;
					StringBuffer sBuffer=new StringBuffer();
					sBuffer.append("[");
					for (int i = 0; i < objArr.length; i++) {
						tmpMap=(LinkedHashMap<String, Object>) objArr[i];
						StringBuffer mapBuffer=new StringBuffer();
						mapBuffer.append("{");
						for (Map.Entry<String,Object> tmpEntry : tmpMap.entrySet()) {
							if (tmpEntry.getValue()!=null)
							mapBuffer.append(tmpEntry.getKey()+"="+tmpEntry.getValue().toString()+",");
						}
						if (mapBuffer.length()>1) {
							mapBuffer.deleteCharAt(mapBuffer.length()-1);
						}
						mapBuffer.append("}");
						if (i==objArr.length-1) {
							sBuffer.append(mapBuffer.toString());
						}else {
							sBuffer.append(mapBuffer.toString()+",");
						}
					}
        			sBuffer.append("]");
        			sorted.add(entry.getKey()+ "=" +sBuffer.toString());
				}else {
					sorted.add(entry.getKey()+ "=" + entry.getValue().toString());
				}
        	}
		}
        sorted.sort( (String p,String q) -> p.compareTo(q) );
        sorted.forEach(str::append);
        System.out.println(DigestUtils.md5Hex(str.toString()));
        return DigestUtils.md5Hex(str.toString());
    }
    
	
//	public static void  main(String [] s) {//测试签名方法
//		String beforesign = "{\"body\":\"youcai\",\"merchantId\":\"1\",\"notifyUrl\":\"http://vpca-scm-cashier-01.vm.elenet.me/mercuris-webapi-paymentapi/paymentapi/setpaymentstatus\",\"orderTotalAmount\":\"10\",\"orderTradeNo\":\"1556151240871003117321774\",\"payDeadLine\":\"26\",\"returnUrl\":\"localhost%3A8080%2F%23%2Forder\",\"sign\":\"f21ca8aae30b433c701bd3088cb2e593\",\"source\":\"web\",\"spBillCreateIp\":\"8.8.8.8\"}";
//		try {
//			System.out.println(beforesign);
//			String aftersign = generateSignatureByMsg(beforesign);
//			System.out.println(aftersign);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
