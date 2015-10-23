package net.eai.umlcg.framework.elespring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.eai.dev.ioUtil;
import net.eai.umlcg.basecg.AttributeCG;
import net.eai.umlcg.basecg.EntityCG;
import net.eai.umlcg.basecg.OperationCG;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityAttribute;
import net.eai.umlmodel.EntityOperation;

public class ESServiceCG implements EntityCG{


	private String m_templatePath;
	private String m_targetPath;
	private ESPackageCG m_packCG;

	public ESServiceCG(ESPackageCG packCG,String templatePath,String targetPath)
	{
		m_templatePath = templatePath;
		m_targetPath = targetPath;
		m_packCG = packCG;
	}
	
	@Override
	public String genCode(Entity entityObj) {
		

		String code = "";

		for(EntityOperation op:entityObj.getOperations())
		{
			LinkedHashMap<String,String> tokens = new LinkedHashMap<String,String>();

			tokens.put("op", op.getName());
			tokens.put("mock", genServiceMoking(op));
			
			code += ioUtil.fillTemplate(m_templatePath + "/ServiceImpl.java", tokens);
		}

		return code;
	}
	

	private String genServiceMoking(EntityOperation op)
	{
		String responseContractName = ioUtil.replaceF("@^t@", "t", op.getName()) + "Response";
		
		String code = "\r\n\t\t\t" + responseContractName + "Dto responseDto = new " +  responseContractName + "Dto();\r\n";
		Entity e = findContract(m_packCG.getPackage(),responseContractName);
		if(e != null)
		{
			for(EntityAttribute att:e.getAttributes())
			{
				String setter = "set" + ioUtil.replaceF("@^t@", "t", att.getName());
				code += "\t\t\tresponseDto." + setter  + "(";
				
				String attType = att.getTypeStr();
				if(att.getDefaultValue() != null)
				{
					if(attType.equals("String"))
						code += "\"" + att.getDefaultValue() + "\"";
					else
						code += att.getDefaultValue();
				}
				else if(attType.equals("String"))
				{
					code += "\"" + att.getName() + "Value\"";
				}
				else if(attType.equals("int") || attType.equals("Integer") || attType.equals("long") || attType.equals("BigInteger"))
				{
					code += "1";
				}
				else if(attType.equals("double") || attType.equals("float")  || attType.equals("Double")  || attType.equals("BigDecimal") )
				{
					code += "12.34" ;
				}
				else if(attType.equals("Date"))
				{
					code += "new Date()";
				}
				
				code += ");\r\n";
			}
		}
		//@^opName@ResponseDto responseDto = @callService@;
		return code;
	}

	@Override
	public void setTargetPath(String targetPath) {
		// TODO Auto-generated method stub
		
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
		return "ESServiceCG";
	}

	
	

	private Entity findContract(DEVPackage pack,String name)
	{
		Iterator<Entry<String, Entity>> entityIter = pack.getEntities().entrySet().iterator();

		while (entityIter.hasNext()) {
			Entry<String, Entity> entry = entityIter.next();
			Entity entity = entry.getValue();
			if("Contract".equals(entity.getStereotype()) && entity.getName().equals(name))
				return entity;
		}

		ArrayList<DEVPackage> depends = pack.getDependPacks();
		for(DEVPackage depPack:depends)
		{
			Entity res =  findContract(depPack,name);
			if(res != null)
				return res;
		}
		return null;

	}
}
