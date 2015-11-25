package net.eai.app.system;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class Common {

	private static SqlSessionFactory sqlSessionFactory = null;
	

	static {
		String resource = "net/eai/app/system/Configuration.xml";

		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	
	public static SqlSession getSession() {
		return sqlSessionFactory.openSession();
	}


	public static int getIntFromString(String str)
	{
		if(str == null)
			return 0;
		
		if(str.equals(""))
			return 0;
		
		int ret = 0;
		try{
			ret = Integer.valueOf(str);
		}
		catch(Exception e)
		{
			
		}
		return ret;
	}
	
}
