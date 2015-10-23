	public @^op@ResponseDto @.op@(@^op@RequestDto request) throws Exception
	{	
		try {
				@mock@
			return responseDto;
		} catch (SystemException e) {
			throw e;
		}catch (Exception e) {
			throw new SystemException(BaseExceptions.UNKNOWN_EXCEPTION, e);
		}
	}	