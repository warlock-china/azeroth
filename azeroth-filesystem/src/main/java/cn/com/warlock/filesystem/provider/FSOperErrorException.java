package cn.com.warlock.filesystem.provider;

public class FSOperErrorException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	private String privoderName;
	private int code;
	
	public FSOperErrorException() {
	}

	public FSOperErrorException(String privoderName,Throwable cause) {
		super(cause);
		this.privoderName = privoderName;
	}
	
	public FSOperErrorException(String privoderName,String message) {
		this(privoderName, 500, message);
	}

	public FSOperErrorException(String privoderName,int code,String message) {
		super(message);
		this.code = code;
		this.privoderName = privoderName;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
