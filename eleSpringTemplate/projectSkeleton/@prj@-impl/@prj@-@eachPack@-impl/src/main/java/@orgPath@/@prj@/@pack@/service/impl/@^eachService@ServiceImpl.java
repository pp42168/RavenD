package @orgPath@.@prj@.@pack@.service.impl;

import @orgPath@.@prj@.@pack@.dto.*;
import @orgPath@.@prj@.@pack@.mapper.@eachEntity@Mapper;
import @orgPath@.@prj@.@pack@.service.*;
import @orgPath@.@prj@.common.exception.*;
import me.ele.elog.Log;
import me.ele.elog.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class @entity@ServiceImpl implements @entity@Service{
	/*
	static {
		Etrack.setAppId("me.ele.scm.@prj@.api");
	}
	@Autowired
    private AddValueToRedisDaoImpl addValueToRedisDaoImpl;
	*/
	
private final static Log LOGGER = LogFactory.getLog(@entity@ServiceImpl.class);
	


#CG EntityCG ESServiceCG
	
	
	public String test() throws Exception{
		try {
		//throw new ServiceException(BaseExceptions.error678);
		//throw new SystemException(BaseExceptions.exception5101);
		//throw new ServiceException("567","这仅仅是演示Exception");

		System.out.println("---test---test---test---");
		return "---test---test---test---";

		}  catch (SystemException e) {
			throw e;
		}catch (Exception e) {
			throw new SystemException(BaseExceptions.UNKNOWN_EXCEPTION, e);
		}
	}
}
