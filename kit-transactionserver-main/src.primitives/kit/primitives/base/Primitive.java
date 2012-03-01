package kit.primitives.base;


/**
 * Basic primitive structure.
 * Defines as abstract communication primitive.
 * Direction: never used directly. Use inherited classes.
 * 
 * @author Daniel Felix Ferber and José Flávio Aguilar Paulino
 */
public abstract class Primitive {
	
	public String toString() {
		//return PrimitiveStreamFactory.getIdByClass(this.getClass());
		return this.getClass().getName();
	}
	
}//abstract class :: Primitive
