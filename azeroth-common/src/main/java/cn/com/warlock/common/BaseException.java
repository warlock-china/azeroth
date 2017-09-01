package cn.com.warlock.common;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    public BaseException() {
        super();
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
