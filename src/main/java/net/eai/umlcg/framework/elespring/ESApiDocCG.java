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

public class ESApiDocCG implements EntityCG{


	private String m_templatePath;
	private String m_targetPath;
	private ESPackageCG m_packCG;

	public ESApiDocCG(ESPackageCG packCG,String templatePath,String targetPath)
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
			Entity requestContract = entityObj.getDepends(op + "Request");
			Entity responseContract = entityObj.getDepends(op + "Response");
			
			LinkedHashMap<String,String> tokens = new LinkedHashMap<String,String>();

			tokens.put("methodName", op.getName());
		//	tokens.put("mock", genServiceMoking(op));
			
			code += ioUtil.fillTemplate(m_templatePath + "/ServiceImpl.java", tokens);
		}

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
		return "ESApiDocCG";
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
