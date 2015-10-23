package net.eai.talk.exception;

import java.util.List;

import net.eai.talk.Word;

public class UsageException extends Exception{

		private String errorCode;
		private List<Word> missingParas;

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public List<Word> getMissingParas() {
			return missingParas;
		}

		public void setMissingParas(List<Word> missingParas) {
			this.missingParas = missingParas;
		}
}
