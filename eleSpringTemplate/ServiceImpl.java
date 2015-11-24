	public @^op@Response @.op@(@^op@Request request) throws Exception
	{	
		try {
			@mock@

			@flow@

			return responseDto;
		} catch (SystemException e) {
			throw e;
		}catch (Exception e) {
			throw new SystemException(BaseExceptions.UNKNOWN_EXCEPTION, e);
		}
	}	