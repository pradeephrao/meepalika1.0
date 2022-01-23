package com.meepalika.response;

public class ApiResponse {
	public Long id;
	public Object data;

	public static enum CODES {
		SUCCESS, FAILURE, FATAL, INFORMATION
	}

	private CODES code;
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ApiResponse(CODES code, String message) {
		this.code = code;
		this.message = message;
	}

	public ApiResponse(CODES code, String message, Long id) {
		this.code = code;
		this.message = message;
		this.id = id;
	}

	public ApiResponse(CODES code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public ApiResponse(CODES code) {
		this(code, null);
	}

	public CODES getCode() {
		return code;
	}

	public boolean isSucessful() {
		return code.equals(CODES.SUCCESS);
	}

	public boolean isFatal() {
		return code.equals(CODES.FATAL);
	}

	public String getMessage() {
		return message;
	}

	public static class SUCCESS extends ApiResponse {
		public SUCCESS() {
			this(null);
		}

		public SUCCESS(String message) {
			super(CODES.SUCCESS, message);
		}
	}

	public static class FAILURE extends ApiResponse {
		public FAILURE() {
			this(null);
		}

		public FAILURE(String message) {
			super(CODES.FAILURE, message);
		}
	}

	public static class FATAL extends ApiResponse {
		public FATAL() {
			this(null);
		}

		public FATAL(String message) {
			super(CODES.FATAL, message);
		}
	}
	
	public static class INFORMATION extends ApiResponse {
		public INFORMATION() {
			this(null);
		}

		public INFORMATION(String message) {
			super(CODES.INFORMATION, message);
		}
	}
}
