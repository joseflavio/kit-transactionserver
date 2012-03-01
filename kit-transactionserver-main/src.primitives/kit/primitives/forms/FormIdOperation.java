package kit.primitives.forms;

import kit.primitives.base.Primitive;

public class FormIdOperation extends Primitive {
	public byte type = 0;
	public static final byte CLIENT_REQUEST              = 1;
	public static final byte SERVER_SENT                 = 2;
	public static final byte SERVER_INTERNAL_DELETE_SENT = 3;
	public static final byte CLIENT_CONFIRM_DELETE       = 4;
	
	public static final byte DELETE = 5;
	
	public String formId;

	public String toString() {
		return super.toString() + " ("+Integer.toString(type)+" '" + formId + "')";
	}
		
} // AuthenticateRequest
