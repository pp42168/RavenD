package net.eai.umlcg.basecg;

import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.DEVProject;
import net.eai.umlmodel.Entity;

public interface UmlFactory {
	
	public void createUml();
	public void createService();
	public void createServiceApi();
	public void createApi();
	public void createModel();
	public void createServiceClient();
	
	
	public void exportUml();

	public void createModels(DEVProject project, DEVPackage pack,Entity entityObj);
}
