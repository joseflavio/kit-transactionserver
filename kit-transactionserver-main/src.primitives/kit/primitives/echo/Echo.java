package kit.primitives.echo;

import kit.primitives.base.Primitive;

/**
 * Sends data over channel to test communication.
 * 
 * Server->Client (type = request)
 * 
 * Client->Server (type = response)
 */
public class Echo extends Primitive {
	public short type;
	public static final short REQUEST = 1;
	public static final short RESPONSE = 2;

	public String content;
	
	public String toString() {
		return super.toString() + " ("+Integer.toString(type)+" '" + content + "')";
	}
}
