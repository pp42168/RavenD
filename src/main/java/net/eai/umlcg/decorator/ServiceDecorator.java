package net.eai.umlcg.decorator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.Gson;

import net.eai.dev.ioUtil;
import net.eai.umlmodel.DEVPackage;
import net.eai.umlmodel.Entity;
import net.eai.umlmodel.EntityAttribute;
import net.eai.umlmodel.EntityOperation;
import net.eai.umlmodel.OperationParameter;
import net.eai.umlmodel.StarUmlObjectContainer;
import net.eai.umlmodel.StarUmlReference;
import net.eai.umlmodel.UMLCollaboration;
import net.eai.umlmodel.UMLInteraction;
import net.eai.umlmodel.UMLParticipant;

public class ServiceDecorator {

	private DEVPackage pack;
	
	
	public ServiceDecorator(DEVPackage p)
	{	
		pack = p;

		addContract();

	}
	
	private void addContract()
	{

        Iterator<Entry<String, Entity>> entityIter = pack.getEntities().entrySet().iterator();

        LinkedList<Entity> contracts = new  LinkedList<Entity>();
        while (entityIter.hasNext()) {
            Entry<String, Entity> entry = entityIter.next();
            Entity service = entry.getValue();
            
            if(!service.getStereotype().equals("Service"))
            	continue;

            for(EntityOperation op : service.getOperations())
    		{ 	
        		List<OperationParameter> paras = op.getParameters(); 
        		
        		if(paras != null)
        		{
        			//generate request contract
        			Entity contract = new Entity();
        			contract.setName(ioUtil.capFirstLetter(op.getName()) + "Request");
        			contract.set_parent(new StarUmlReference(pack.get_id()));
        			contract.setStereotype("Contract");
        			pack.getOwnedElements().add(contract);
        			contracts.add(contract);

        			for(OperationParameter onePara : paras)
        			{
        				if(!onePara.getDirection().equals("return"))
        					contract.addAttribute(onePara.getName(), onePara.getTypeStr());
        			}
        		}
    		}
            
        }
        

        for(Entity e:contracts)
        {
        	pack.addEntity(UUID.randomUUID().toString(), e);
        }
		
		
	}
}
