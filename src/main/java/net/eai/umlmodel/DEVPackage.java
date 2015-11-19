package net.eai.umlmodel;


import com.google.gson.Gson;

import net.eai.dev.ioUtil;
import net.eai.dev.entitycg.*;
import net.eai.umlcg.decorator.ServiceDecorator;

import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

public class DEVPackage<T> {
    private HashMap<String, Entity> entities = new HashMap<String, Entity>();
    private HashMap<String, Entity> filters = new HashMap<String, Entity>();
    private ArrayList<DEVPackage> dependPacks = new ArrayList<DEVPackage>();
    
    
    
    public DEVPackage() {
        _type = "UMLPackage";
        String str = this.toString();
        _id = str.substring(str.indexOf("@"));
        name = "untitled";
    }

    private List<T> ownedElements = new ArrayList();
    private String packageName = "domain";
    private String projectName = "domain";
    private String framework;
    private String templatePath;
    private String stereotype;
    private String name;
    private String _type;
    private String _id;


    private String dbConfigXml = null;
    private String createTables = " ";
    
    

    public HashMap<String, Entity> getEntities(){
    	return entities;
    }
    
    public void addEntity(String id, Entity entity) {
        entities.put(id, entity);
        //	ownedElements.add((T) entity);
    }

    public void exportUMLFragment(String path) {
        Gson gson = new Gson();
        String code = gson.toJson(this);
        ioUtil.writeFile(path + "/" + this.getName() + ".mfj", code);
    }

    public Entity findEntity(String entityName)
    {
    	Entity e = null;
    	
    	return e;
    }

    @SuppressWarnings("rawtypes")
    public static DEVPackage importFromJson(String jsonfile) {

        String json = ioUtil.readFile(jsonfile);
        Gson gson = new Gson();
        DEVPackage devPackage = gson.fromJson(json, DEVPackage.class);
        initDevPackage(devPackage);
        return devPackage;

    }


	private static Entity getEntity(Map data)
	{
		Gson gson = new Gson();
		Entity entity = null;
	    String type = (String) data.get("_type");
	    if (type.equals("UMLClass")) {
	         String eleJson = gson.toJson(data);
	         entity = gson.fromJson(eleJson, Entity.class);
	    }
		return entity;
	}
	
    @SuppressWarnings("rawtypes")
    public static void initDevPackage(DEVPackage devPackage) {

        Gson gson = new Gson();
        if (devPackage != null && devPackage.getOwnedElements() != null) {
            List ownedElements = (List) devPackage.getOwnedElements();

/*
            Entity entityManageApi = new Entity();
            entityManageApi.setName(devPackage.getName() + "EntityManageApi");
            entityManageApi.set_parent(new StarUmlReference(devPackage.get_id()));
            entityManageApi.setTemplatePath(devPackage.getTemplatePath());
            entityManageApi.setStereotype("Api");
            devPackage.getOwnedElements().add(entityManageApi);*/

            for (int i = 0; i < ownedElements.size(); i++) {
                Entity entity = null;
                Object aa = ownedElements.get(i);
                if (aa.getClass().toString().contains("Entity")) {
                    entity = (Entity) aa;
                } else if(aa.getClass().toString().contains("Map")){
                	entity = getEntity((Map)aa);
                	/*
                    LinkedHashMap data = (LinkedHashMap) ownedElements.get(i);
                    String type = (String) data.get("_type");
                    String id = (String) data.get("_id");
                    String stereotype = (String) data.get("stereotype");
                    if (type.equals("UMLClass")) {
                        String eleJson = gson.toJson(data);
                        entity = gson.fromJson(eleJson, Entity.class);
                    }*/
                }

                if (entity != null) {
                    entity.collectExtraAttributes();
                    entity.setDevPackage(devPackage);
                    entity.setTemplatePath(devPackage.getTemplatePath());
                    StarUmlObjectContainer.addObject(entity.get_id(), entity);
                    StarUmlObjectContainer.addObjectByName(entity.getName(), entity);
                    devPackage.addEntity(entity.get_id(), entity);
                    StarUmlReference ref = new StarUmlReference(devPackage.get_id());
                    entity.set_parent(ref);
                    ownedElements.set(i, entity);


                }


            }
            //	devPackage.normalizeTypes();
        }
    }


    
    public void scanDepends()
	{
		if(this.getOwnedElements() != null)
		{
			for(Object obj:this.getOwnedElements())
			{
				String objType = obj.getClass().toString();
				if(objType.contains("Map"))
				{
					Map map = (Map) obj;
					if("dependency".equals(map.get("_type")))
					{
						Gson gson = new Gson();
	    				String json = gson.toJson(obj);
	    				
	    				String typeName = "String";
					}
					
				}
					objType = "";
				/*
				String targetID = depend.getTarget().get$ref();
				Entity entity = (Entity) StarUmlObjectContainer.getObject(targetID) ;
				if(entity != null)
				{
					depends.put(entity.getName(), entity);
				}*/
			}
		}
	}
    
    
    public void normalizeTypes() {

        Iterator<Entry<String, Entity>> entityIter = entities.entrySet().iterator();

        while (entityIter.hasNext()) {
            Entry<String, Entity> entry = entityIter.next();
            Entity entity = entry.getValue();
            entity.normalizeTypes();
        }

        Iterator<Entry<String, Entity>> depends = entities.entrySet().iterator();

        //create a dictionary entity for all entities to depend

        while (depends.hasNext()) {
            Entry<String, Entity> entry = depends.next();
            Entity entity = entry.getValue();
            entity.scanDepends();
            dependPacks.addAll(entity.getDependPacks());
        }
        
        //collect pack dependency
        
        


    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    


    public String[] exportDotNetCode(String path) {
        path = path + "/" + projectName + "/";
        Iterator<Entry<String, Entity>> entityIter = entities.entrySet().iterator();

        String sqlMap = "";

        String csprojectContent = "";

        while (entityIter.hasNext()) {
            Entry<String, Entity> entry = entityIter.next();
            Entity val = entry.getValue();

            val.setTemplatePath(templatePath);
            val.setDevPackage(this);
/*
            PersistEntityDotNet persistDN = new PersistEntityDotNet(val);
            persistDN.setTemplatePath(templatePath);
            PersistEntityDotNet.projectName = projectName;
            if ("Api".equals(val.getStereotype())) {
                csprojectContent += persistDN.genOperations(path);
            } else if ("Entity".equals(val.getStereotype())) {
                csprojectContent += persistDN.genEntity(path);
                csprojectContent += persistDN.genMapper(path);
                csprojectContent += persistDN.genControl(path);
                csprojectContent += persistDN.genResponse(path);

                sqlMap += MessageFormat.format("\t<sqlMap resource=\"Mapper/{0}.xml\"/>\r\n", persistDN.getBaseEntity().getName());
            } else if (val.getStereotype() != null && val.getStereotype().startsWith("Html")) {
            } else if (val.getStereotype() == null || !val.getStereotype().startsWith("Ele")) {
                csprojectContent += persistDN.genItem(path);
                csprojectContent += persistDN.genResponse(path);
                sqlMap += MessageFormat.format("\t<sqlMap resource=\"Mapper/{0}.xml\"/>\r\n", persistDN.getBaseEntity().getName());
            }
            
            */
        }

        String[] s = new String[2];
        s[0] = sqlMap;
        s[1] = csprojectContent;
        return s;


    }


    public List<T> getOwnedElements() {
        return ownedElements;
    }

    public void setOwnedElements(List<T> ownedElements) {
        this.ownedElements = ownedElements;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDbConfigXml() {
        return dbConfigXml;
    }


    public void setDbConfigXml(String dbConfigXml) {
        this.dbConfigXml = dbConfigXml;
    }


    public String getCreateTables() {
        return createTables;
    }


    public void setCreateTables(String createTables) {
        this.createTables = createTables;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public ArrayList<DEVPackage> getDependPacks() {
		return dependPacks;
	}

	public void setDependPacks(ArrayList<DEVPackage> dependPacks) {
		this.dependPacks = dependPacks;
	}
}
