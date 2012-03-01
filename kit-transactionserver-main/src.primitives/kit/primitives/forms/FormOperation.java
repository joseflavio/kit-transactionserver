package kit.primitives.forms;

import kit.primitives.base.Primitive;

/**
 * Requests an operation to be performed.
 * 
 * Server->Client (reset)
 */
public class FormOperation extends Primitive {
	public byte type = 0;
	
	public static final byte RESET = 1;
	public static final byte UPDATED_FORMS = 2;
	public static final byte UPDATED_FORMS_COMPLETE = 3;
	public static final byte UPDATED_FORMS_CLEAR_FLAGS = 4;
	public static final byte ALL_NEW_FORMS = 5;
	public static final byte UPDATED_FORMS_PARTIAL = 6;
	public static final byte UPDATED_FORMS_CLEAR_RECENT_FLAGS = 7;
	
	public static final byte GET_STATUS = 9; // não gostei deste nome, mas não achei melhor
	public static final byte CLIENT_FAILURE = 10;
	public static final byte CLIENT_SUCCESS = 11;
	
	// Future enhancements
	public static final byte HAS_FORM = 12;
	public static final byte GET_FORM = 13;
	public static final byte FORM_FOUND = 14;
	public static final byte FORM_NOT_FOUND = 15;
	
	public String toString() {
		return super.toString() + " (" + Integer.toString(type) + ")";
	}	
}
