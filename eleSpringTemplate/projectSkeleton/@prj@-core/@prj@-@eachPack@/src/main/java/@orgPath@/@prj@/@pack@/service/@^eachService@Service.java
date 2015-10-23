package @orgPath@.@prj@.@pack@.service;
import @orgPath@.@prj@.@pack@.dto.*;


/**
 * @author
 * @entity@服务接口
 * 
 * */
public interface @^entity@Service {
	
	public @^eachOp@ResponseDto @.eachOp@(@^eachOp@RequestDto request)throws Exception;

	public String test()throws Exception;
}
