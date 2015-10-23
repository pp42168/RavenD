
	/**
	 * @author
	 * @param   @^opName@RequestDto
	 * @return	@^opName@ResponseDto
	 */

	@RequestMapping(value = "/@..opName@", method = RequestMethod.@apiType@)
	public @ResponseBody ResponseEntity<?> @..opName@(@^opName@RequestDto requestDto) {
		

		@^opName@ResponseDto responseDto = null;
		try {
			responseDto = @callService@;
		}  catch (ServiceException e) {
			return ResponseEntity.fail(e.getCode(), e.getErrorMessage());
		} catch (SystemException e) {
			return ResponseEntity.fail(e.getErrorMessage());
		} catch (Exception e) {
			return ResponseEntity.error(e.getMessage());
		}
		
		return ResponseEntity.success(responseDto);

	}

