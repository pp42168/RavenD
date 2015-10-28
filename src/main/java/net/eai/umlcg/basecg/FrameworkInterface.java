package net.eai.umlcg.basecg;

import java.util.List;

import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;

public interface FrameworkInterface {
	
	public boolean hasError();

	public ProjectCG getProjectCG(DEVProject project);
	public List<EntityCG> getEntityCGs(PackageCG packCG);
	public PackageCG getPackageCG(ProjectCG projectCG,DEVPackage pac);
	
	public UmlFactory getEntityApiFactory();
	public UmlFactory getEntityPageFactory();

	
}
