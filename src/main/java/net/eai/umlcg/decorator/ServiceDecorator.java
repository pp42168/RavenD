package net.eai.umlcg.decorator;

import java.util.Iterator;
import java.util.LinkedHashMap;
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
	}

	private void addResponseContract(String opName,String retName,String retType,LinkedHashMap<String,Entity> contracts)
	{
		//generate request contract
		Entity contract = new Entity();
		contract.setName(ioUtil.capFirstLetter(opName) + "Response");
		contract.set_parent(new StarUmlReference(pack.get_id()));
		contract.setStereotype("Contract");
		if(retName != null && retType != null)
			contract.addAttribute(retName,retType);
		pack.getOwnedElements().add(contract);
		contracts.put(contract.getName(),contract);
	}

	public DEVPackage genApiPack()
	{
		DEVPackage apiPack = new DEVPackage();
		apiPack.setName(pack.getName());
		
		Iterator<Entry<String, Entity>> entityIter = pack.getEntities().entrySet().iterator();

		while (entityIter.hasNext()) {
			Entry<String, Entity> entry = entityIter.next();
			Entity service = entry.getValue();

			if(service.getStereotype() ==null || !service.getStereotype().equals("Service"))
				continue;

			Entity apiEntity = new Entity();
			apiEntity.setName(ioUtil.capFirstLetter(service.getName()));
			apiEntity.setStereotype("Api");
			for(EntityOperation op : service.getOperations())
			{ 	
				if(op.getStereotype() == null)
					op.setStereotype("GET");
				
				apiEntity.addOperation(op);
			}
			apiEntity.addDepend(service.getName(), service);

			apiPack.addEntity(UUID.randomUUID().toString(), apiEntity);
			
		}


		return apiPack;
	}

	public void addContract()
	{

		Iterator<Entry<String, Entity>> entityIter = pack.getEntities().entrySet().iterator();

		LinkedHashMap<String,Entity> contracts = new LinkedHashMap<String,Entity>();
		LinkedHashMap<String,Entity> existingContracts = new LinkedHashMap<String,Entity>();

		while (entityIter.hasNext()) {
			Entry<String, Entity> entry = entityIter.next();
			Entity entity = entry.getValue();

			//scan all the existing contracts
			if(entity.getStereotype() != null && entity.getStereotype().equals("Contract"))
				existingContracts.put(entity.getName(), entity);


			if(entity ==null && !entity.getStereotype().equals("Service"))
				continue;

			for(EntityOperation op : entity.getOperations())
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
					contracts.put(contract.getName(),contract);

					for(OperationParameter onePara : paras)
					{
						if(!onePara.getDirection().equals("return"))
							contract.addAttribute(onePara.getName(), onePara.getTypeStr());
						else 
						{
							addResponseContract(op.getName(),onePara.getTypeStr(),onePara.getStereotype(),contracts);
						}
					}
				}
			}

		}

		//check if every method has responseDto
		Iterator<Entry<String, Entity>> resIter = pack.getEntities().entrySet().iterator();
		while (resIter.hasNext()) {
			Entry<String, Entity> entry = resIter.next();
			Entity service = entry.getValue();

			if(service.getStereotype() == null || !service.getStereotype().equals("Service"))
				continue;

			for(EntityOperation op : service.getOperations())
			{
				if(!contracts.containsKey(ioUtil.capFirstLetter(op.getName()) + "Response") 
						&& !existingContracts.containsKey(ioUtil.capFirstLetter(op.getName()) + "Response"))
				{
					addResponseContract(ioUtil.capFirstLetter(op.getName()),null,null,contracts);
				}
			}
		}


		//add all contracts into pak
		for(Entity e:contracts.values())
		{
			pack.addEntity(UUID.randomUUID().toString(), e);
		}


	}
}
