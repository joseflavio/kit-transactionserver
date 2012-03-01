package kit.primitives.authentication;


import kit.primitives.base.Primitive;

/**
 * Notifies client that it has to send its authentication, or that lastauthetication was accepted or
 * rejected. If rejected, the server may close connection and refuse any further communication.
 * 
 * This primitive is identical to CommunicationNotification and ChannelNotification. they are
 * represented by different classes as they context is different.
 * 
 * <ul>
 * <li>Server->Client (always)
 * </ul>
 * 
 * @author Daniel Felix Ferber and José Flávio Aguilar Paulino
 */
public class AuthenticationResponse extends Primitive {
	public byte type = UNDEFINED;
	public static final char UNDEFINED = 0;
	public static final char SUCCESS = 1;
	public static final char FAILED = 2;
	public static final char REQUEST = 3;
	public static final char DATABASEERROR = 4;
	public static final char CLIENTALREADYLOGGED = 5;
	
	public String toString() {
		return super.toString() + " (" + Integer.toString(type) + ")";
	}

}
