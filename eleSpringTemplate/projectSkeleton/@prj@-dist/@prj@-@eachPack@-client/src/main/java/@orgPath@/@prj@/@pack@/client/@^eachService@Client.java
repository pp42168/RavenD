package @orgPath@.@prj@.@pack@.client;

import me.ele.contract.client.ClientUtil;
import @orgPath@.@prj@.@..pack@.dto.*;
import @orgPath@.@prj@.@..pack@.service.@^entity@Service;

public class @^entity@Client implements @^entity@Service {
	private final static @^entity@Service client = ClientUtil.getContext()
			.getClient(@^entity@Service.class);

@<@
	@Override
	public @^eachOp@ResponseDto @.eachOp@(@^eachOp@RequestDto request)throws Exception
	{
		return client.@.eachOp@(request);
	}
@>@

	@Override
	public String test()throws Exception
	{
		return client.test();
	}

}
