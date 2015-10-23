package @orgPath@.@prj@.svr.web.@..pack@;

import javax.inject.Named;
import @orgPath@.@prj@.svr.web.framework.ResponseEntity;

import @orgPath@.@prj@.@..eachDependPack@.dto.*;
import @orgPath@.@prj@.@..eachDependPack@.service.*;
import @orgPath@.@prj@.common.exception.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/@..entity@")
public class @entity@Controller {

@<@
	@Autowired(required = false)
	@Named("@.eachDependEntity@Service")
	private @^eachDependEntity@Service @.eachDependEntity@Service;
@>@


	#CG EntityCG ApiEntity

	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> test(){
		try {
			
			String returnStr = @.eachDependEntity@Service.test();
			return ResponseEntity.success(returnStr);

		} catch (ServiceException e) {
			return ResponseEntity.fail(e.getCode(), e.getErrorMessage());
		} catch (SystemException e) {
			return ResponseEntity.fail(e.getErrorMessage());
		} catch (Exception e) {
			return ResponseEntity.error(e.getMessage());
		}
	}
}
