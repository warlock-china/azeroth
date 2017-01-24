package cn.com.warlock.emitter;

public class GeneratorException extends Exception {
	private static final long serialVersionUID = 1L;

	public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratorException() {
        super();
    }

    public GeneratorException(Throwable cause) {
        super(cause);
    }
}
