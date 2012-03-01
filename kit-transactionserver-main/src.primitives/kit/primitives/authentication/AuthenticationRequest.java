package kit.primitives.authentication;

import kit.primitives.base.Primitive;

/**
 * Sends authentication data to server. Must be sent automatically as first message.
 * 
 * <ul>
 * <li>Client->Server
 * </ul>
 * 
 * @author Daniel Felix Ferber and José Flávio Aguilar Paulino
 */
public class AuthenticationRequest extends Primitive {
	public byte type = UNDEFINED;
	public final static byte UNDEFINED = 0;
	public final static byte PREVIOUS = 1;
	public final static byte NEWLOGIN = 2;

	public String clientId;
	public String password;
	public long installationId1;
	public long installationId2;

	public String toString() {
		return super.toString() + " (" + Integer.toString(type) + "', '" + clientId + ", "
				+ password + "')";
	}
}
