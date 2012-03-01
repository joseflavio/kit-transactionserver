package kit.primitives.channel;

import kit.primitives.base.Primitive;

/**
 * Notifies other part about channel status.
 *
 * <ul>
 * <li>Server->Client (end communication).
 * <li>Client->Server (end communication).
 * <li>Client->Server (out of memory): Client has not enough memory to process last primitive.
 * <li>Client->Server (protocol error): Client could not understand last primitive.
 * </ul>
 * 
 * @author Daniel Felix Ferber and José Flávio Aguilar Paulino
 */
public class ChannelNotification extends Primitive {
	public byte type = 0;
	public static final byte END_CHANNEL = 1;
	public static final byte ERROR_OUT_OF_MEMORY = 2;
	public static final byte ERROR_PROTOCOL = 3;
	public static final byte ERROR_SERVER = 4;
	
	public String toString() {
		return super.toString() + " ( " + Integer.toString(type) + " )";
	}
	
}
