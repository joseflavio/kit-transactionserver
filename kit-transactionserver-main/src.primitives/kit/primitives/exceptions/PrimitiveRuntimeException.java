package kit.primitives.exceptions;

import kit.primitives.base.Primitive;

public class PrimitiveRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3829484068081063304L;
	
	Exception exception = null;
	Primitive primitive = null;
	
	public PrimitiveRuntimeException() {
		super("Communication protocol error.");
		System.out.print(getMessage());
	}
	
	public PrimitiveRuntimeException(String message) {
		super("Communication protocol error: " + message);
		System.out.print(getMessage());
	}
	
	public PrimitiveRuntimeException(Exception e) {
		super("Communication protocol error: " + e.getMessage());
		this.exception = e;
		System.out.print(getMessage());
	}

	public Exception getException() {
		return exception;
	}
}
