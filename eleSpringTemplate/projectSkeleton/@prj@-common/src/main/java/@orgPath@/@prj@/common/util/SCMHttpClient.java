package @orgPath@.@prj@.common.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.entity.StringEntity;

public class SCMHttpClient {

	public String getHttpClient(String url) {
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		byte[] responseBody = null;
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,3000 );
		getMethod.addRequestHeader( "Content-Type","charset=UTF-8" );  
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed:" + getMethod.getStatusLine());
			}
			// 读取内容
			responseBody = getMethod.getResponseBody();
			// 处理内容
			// System.out.println(new String(responseBody));
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return new String(responseBody);
	}

	public String postHttpClient(String url,Map<String,String> map) {
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		byte[] responseBody = null;
		// 创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,3000 );
		postMethod.addRequestHeader( "Content-Type","charset=UTF-8" );  
		// 填入各个表单域的值
		//NameValuePair[] data = { new NameValuePair("appid", "wxe6e339d5db9e"), new NameValuePair("code", "0015b9d925e9710c34e39ddee477ac7T") };
		NameValuePair[] data = new NameValuePair[map.size()];

		int flag = 0;
		for(String str:map.keySet()){
			 String value = map.get(str);
			 NameValuePair nameValuePair = new NameValuePair(str,value);
			 data[flag++] = nameValuePair;
		}
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
			// 301或者302
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				// 从头中取出转向的地址
				Header locationHeader = postMethod.getResponseHeader("location");
				String location = null;
				if (locationHeader != null) {
					location = locationHeader.getValue();
					System.out.println("The page was redirected to:" + location);
				} else {
					System.err.println("Location field value is null.");
				}
			}
			responseBody = postMethod.getResponseBody();
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(responseBody);
	}
	
	public String postJsonHttpClient(String url,String jsonstr){  
		// 构造HttpClient的实例
        HttpClient httpClient = new HttpClient();  
        byte[] responseBody = null;
        
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,3000 );
        postMethod.addRequestHeader( "Content-Type","application/json;charset=UTF-8" );  
        postMethod.setRequestBody(jsonstr);  
        try{  
            int statusCode = httpClient.executeMethod(postMethod);  
//            log.info(statusCode);  
            if(statusCode != HttpStatus.SC_OK){  
                return null;  
            }    
            responseBody = postMethod.getResponseBody();           
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
        	postMethod.releaseConnection();  
        }  
        return new String(responseBody);  
    }  
	
	public String putJsonHttpClient(String url,String jsonstr){  
		// 构造HttpClient的实例
        HttpClient htpClient = new HttpClient();  
        byte[] responseBody = null;
        
        PutMethod putMethod = new PutMethod(url);  
        putMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,3000 );
        putMethod.addRequestHeader( "Content-Type","application/json;charset=UTF-8" );  
        putMethod.setRequestBody( jsonstr );  
        try{  
            int statusCode = htpClient.executeMethod( putMethod );  
//            log.info(statusCode);  
            if(statusCode != HttpStatus.SC_OK){  
                return null;  
            }    
            responseBody = putMethod.getResponseBody();           
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            putMethod.releaseConnection();  
        }  
        return new String(responseBody);  
    }  
	


}
