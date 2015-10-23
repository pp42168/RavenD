package @orgPath@.@prj@.common.exception;


public enum BaseExceptions implements IExceptionCode {
	@Desc("未知异常")
	UNKNOWN_EXCEPTION,

	@Desc("用户认证错误:用户登录失败")
	SESSION_VALIDATE_EXCEPTION,
	
	@Desc("拉取订单信息失败")
	error4101,
	
	@Desc("该订单已经支付成功")
	error4201,
	
	@Desc("超出支付时限")
	error4301,
	
	
	
	
	@Desc("获取prepayId失败")
	exception5101,
	
	@Desc("获取prepay失败")
	exception5102,
	
	@Desc("不是在线支付订单")
	exception5103,
	
	@Desc("发送支付信息失败")
	exception5104,
	
	@Desc("退款已经成功")
	exception5201,
	
	@Desc("退款进行中")
	exception5202,
	
	@Desc("小哥太帅了")
	error678,
	
	@Desc("小哥太傻了")
	error4993
	
}
