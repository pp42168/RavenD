package net.eai.umlcg.basecg;

import java.util.List;

import net.eai.umlmodel.Entity;

public interface EntityCG {
	String genCode(Entity entityObj);
	void setTargetPath(String targetPath);
	List<AttributeCG> getAttributeCGs();
	List<OperationCG> getOperationCGs();
	
	String getType();
}
