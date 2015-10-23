package net.eai.umlcg.framework.elespring;

import java.util.List;

import net.eai.umlcg.basecg.AttributeCG;
import net.eai.umlcg.basecg.EntityCG;
import net.eai.umlcg.basecg.OperationCG;
import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityAttribute;
import net.eai.umlmodel.EntityOperation;
import net.eai.umlmodel.OperationParameter;

public class ESSqlSchemaCG implements EntityCG{


	private String m_templatePath;
	private String m_targetPath;
	private ESPackageCG m_packCG;

	public ESSqlSchemaCG(ESPackageCG packCG,String templatePath,String targetPath)
	{
		m_templatePath = templatePath;
		m_targetPath = targetPath;
		m_packCG = packCG;
	}
	public void setTargetPath(String targetPath)
	{
		m_targetPath = targetPath;
	}




	@Override
	public String genCode(Entity entityObj) {

		if(!"Entity".equals(entityObj.getStereotype()))
			return "";

		String daoCode = genIbaitsMapper(entityObj);
		//	m_packCG.addIbatisMapper(daoCode);

		return daoCode;
	}




	public String genIbaitsMapper(Entity entity)
	{
		String schemaCode = "";
		String columnDef = "`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键'";

		String tableName = getTableName(entity);
		String complexTableNames = getTableName(entity) + " as " + entity.getName();;

		boolean hasComplexData = false;

		for(EntityAttribute val : entity.getAttributes())
		{ 	
			String attName = val.getName(); 
			String attType = val.getTypeStr();

			String doc = val.getDocumentation();
			
			String columnName = getColumnName(attName);//getColumnName(attName);

			if("id".equals(attName.toLowerCase()))
				continue;

			String dictionary = val.getExtraAttribute("字典");

			if(val.getMultiplicity() !=  null && !val.getMultiplicity().equals(""))
			{
				if("数据表".equals(val.getExtraAttribute("多对多实现")))
					continue;
				else 
					attType = "String";
			}

			columnDef += ",\r\n ";

			if(attType.equals("String"))
				columnDef += "`" + columnName + "` CHAR(32)";
			else if(attType.equals("int") || attType.equals("Integer") )
				columnDef += "`" + columnName + "` INT(11) ";
			else if(attType.equals("enum"))
				columnDef += "`" + columnName + "` TINYINT(4) ";
			else if(attType.equals("long") || attType.equals("Big"))
				columnDef += "`" + columnName + "` BIGINT(20) ";
			else if(attType.equals("Date"))
				columnDef += "`" + columnName + "` DATETIME ";
			else if(attType.equals("Timestamp"))
				columnDef += "`" + columnName + "` TIMESTAMP ";
			else if(attType.equals("double"))
				columnDef += "`" + columnName + "` DOUBLE ";
			else if(attType.equals("BigDecimal"))
				columnDef += "`" + columnName + "` DECIMAL(10,2) ";
			else
			{
				Entity typeEntity = entity.getDepends(attType);
				if(typeEntity != null)
				{        			
					columnDef += ",`" + columnName + "Id` BIGINT(20)";
				}
			}
			
			columnDef += " NOT NULL ";
		}

		columnDef += ",\r\n `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'";
		columnDef += ",\r\n `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'";
		columnDef += ",\r\n `is_delete` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '软删除标记'";


		//generate createTable sql
		schemaCode += "\r\n\r\nCREATE TABLE `" + tableName + "`(" + columnDef 
				+ " ,\r\nPRIMARY KEY (`id`)\r\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

		return schemaCode;

	}



	private String getColumnName(String att)
	{
		String columnName = "";
		for(int i = 0; i< att.length();i++)
		{
			char ch = att.charAt(i);
			if(ch <= 'Z' && ch >= 'A')
				columnName += "_" + ch;
			else 
				columnName += ch;
		}
		columnName = columnName.toLowerCase();
		return columnName;
	}

	private String getTableName(Entity entity)
	{
		String tableName = "";
		for(int i = 0; i< entity.getName().length();i++)
		{
			char ch = entity.getName().charAt(i);
			if(ch <= 'Z' && ch >= 'A')
				tableName += "_" + ch;
			else 
				tableName += ch;
		}
		tableName = tableName.toLowerCase();

		return "tb" + tableName;
	}


	@Override
	public List<AttributeCG> getAttributeCGs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OperationCG> getOperationCGs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "CreateTableCG";
	}

}
