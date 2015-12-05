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

public class ESUnitTestCG implements EntityCG{


	private String m_templatePath;
	private String m_targetPath;
	private ESPackageCG m_packCG;

	public ESUnitTestCG(ESPackageCG packCG,String templatePath,String targetPath)
	{
		m_templatePath = templatePath;
		m_targetPath = targetPath;
		m_packCG = packCG;
	}

	@Override
	public String genCode(Entity entityObj) {

		String code = "";
		
		String template = "";

		for(EntityOperation op:entityObj.getOperations())
		{
			LinkedHashMap<String,String> tokens = new LinkedHashMap<String,String>();

			tokens.put("op", op.getName());

			code += ioUtil.fillTemplate(m_templatePath + "/unittest.java", tokens);
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
		return "UnitTest";
	}

}
