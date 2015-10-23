
	/**
	 * @author
	 * @param   @^opName@RequestDto
	 * @return	@^opName@ResponseDto
	 */

	@RequestMapping(value = "/@..opName@", method = RequestMethod.@apiType@)
	public @ResponseBody ResponseEntity<?> followPteList(@^opName@RequestDto requestDto) {
		
		@^opName@ResponseDto responseDto = @callService@;
		return ResponseEntity.success(responseDto);
	}

