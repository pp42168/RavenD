package @orgPath@.@prj@.svr.web.framework;


import @orgPath@.@prj@.common.exception.Desc;
import @orgPath@.@prj@.common.exception.IExceptionCode;

public enum WebAPIExceptions implements IExceptionCode {

	@Desc("无效身份")
	ERR_AUTH_FAILED(),

	@Desc("无效请求")
	ERR_BAD_REQUEST(),

	@Desc("未知错误")
	ERR_UNKNOWN(),

	@Desc("校验错误")
	ERR_WEBAPI_VALIDATION(),

	@Desc("找不到access_token")
	ERR_WEBAPI_ACCESS_TOKEN_NOT_FOUND();

}
