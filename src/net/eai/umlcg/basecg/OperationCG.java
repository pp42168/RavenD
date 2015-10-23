package net.eai.umlcg.basecg;

import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityOperation;

public interface OperationCG {
	void genCode(EntityOperation op);
}
