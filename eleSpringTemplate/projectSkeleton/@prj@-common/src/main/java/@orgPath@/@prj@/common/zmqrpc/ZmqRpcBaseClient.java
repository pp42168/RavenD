package @orgPath@.@prj@.common.zmqrpc;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.*;
import org.msgpack.unpacker.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javassist.bytecode.ByteArray;


public class ZmqRpcBaseClient {

	private static Logger log = LoggerFactory.getLogger(ZmqRpcBaseClient.class);
	
	private String host = "tcp://192.168.80.49:7050";//需要注入
	

	public ZmqRpcBaseResponse callService (String methodName,Object... paras) throws Exception
	{
		ArrayList<Object> paraList = new ArrayList<Object> ();
		for(Object p:paras)
		{
			paraList.add(p);
		}
		
		return callService(methodName,paraList);
		
	}
	
	public ZmqRpcBaseResponse callService (String methodName,List<Object> paras) throws Exception
	{
		ZMQ.Context context = ZMQ.context(1);
		ZmqRpcBaseResponse response = new ZmqRpcBaseResponse();
		UUID uuid = UUID.randomUUID();

		LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object> ();
		map.put("message_id", uuid.toString());
		map.put("v", 3);
		
		List<Object> dtoPack = new ArrayList<Object>();
		
		dtoPack.add(map);
		dtoPack.add(methodName);
		dtoPack.add(paras);
		
		MessagePack msgpack = new MessagePack();
		// Serialize
		byte[] raw;
		ZMQ.Socket socket = context.socket(ZMQ.REQ); 
		
		try {

			socket.connect(host);		
			
			raw = msgpack.write(dtoPack);
			socket.send(raw);

			byte[] rec;
			rec = socket.recv();
			
			List<Value> responsePack = (List<Value>) msgpack.read(rec);
			
			Map<String,Object> responseHeader = (Map<String, Object>) responsePack.get(0);
			Value statusRaw = responsePack.get(1);
			String status = (String) RawTypeUtil.toObject(statusRaw);
			Object responseData = RawTypeUtil.toObject(responsePack.get(2));
			
			if("OK".equals(status))
			{
				response.setStatus("OK");
				responseData = ((List) responseData).get(0);
				Gson gson = new Gson();
				response.setDataJson(gson.toJson(responseData));
			}
			else 
			{
				response.setStatus("ERR");
				List resList = (List) responseData;
				if(resList.size() == 3)
				{
					response.setErrorCode((String) resList.get(0));
					String errMsg = (String) resList.get(1);
					errMsg += (String) resList.get(2);
					response.setErrorMsg(errMsg);
				}
				else
				{
					response.setErrorCode("unknown error");
					response.setErrorMsg("unknown error");
				}
			}
			
			//convert to json for convinence

		} catch (IOException e) {
			e.printStackTrace();
			throw e;// todo: need analyze exception and throw a proper zmqRpcException.
			
		}


		//System.out.print( "\r\n" + response + "\r\n");
		socket.close();  //先关闭socket
		context.term();  //关闭当前的上下文


		return response;		
	}



}
