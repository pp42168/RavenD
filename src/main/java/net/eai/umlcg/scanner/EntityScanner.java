package net.eai.umlcg.scanner;

import java.util.List;

import net.eai.app.system.Common;
import net.eai.dev.DataMapper;
import net.eai.dev.TableDesc;
import net.eai.dev.ioUtil;
import net.eai.umlmodel.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class EntityScanner {
	
	public DEVPackage scanDb()
	{
		DEVPackage pack = new DEVPackage();
		
		SqlSession s = Common.getSession();
		DataMapper mapper = s.getMapper(DataMapper.class);
		List<String> tables = mapper.showTables();
		for(String table:tables)
		{
			Entity e = scanTable(table);
			pack.addEntity(e.toString(), e);
		}
		return pack;
	}
	
	public Entity scanTable(String table)
	{
		SqlSession s = Common.getSession();
		DataMapper mapper = s.getMapper(DataMapper.class);
		List<TableDesc> aa = mapper.descTable(table);
	
		Entity e = new Entity();
		e.setStereotype("Entity");
		e.setName(ioUtil.convertColumnSpeelToAttSpell(table));
		for(TableDesc o:aa)
		{
			String type = o.getType();
			if(o.getField().equals("id") || 
					o.getField().equals("create_at") || 
					o.getField().equals("update_at")|| 
					o.getField().equals("is_deleted"))
				continue;
			
			if(type.contains("("))
			{		
				type = type.substring(0, type.indexOf("("));
			}
			
			if("decimal".equals(type))
				type = "BigDecimal";
				
			
			
			e.addAttribute(o.getField(), type);
			
		}
		
		return e;
	}
	 
}
