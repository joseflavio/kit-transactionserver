package kit.primitives.exceptions;

import kit.primitives.factory.PrimitiveStreamFactory;


public class UnauthenticatedPrimitiveException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1557389377988230955L;
	
	private Class primitiveClass;
	
	public UnauthenticatedPrimitiveException(Class primitiveClass) {
		this.primitiveClass = primitiveClass;
	}

	public String getPrimitiveName() {
		return PrimitiveStreamFactory.getIdByClass(primitiveClass);
	}
}
