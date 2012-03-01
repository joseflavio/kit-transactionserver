package kit.primitives.forms;

import java.util.Date;
import java.util.Vector;

import kit.primitives.base.Primitive;

/**
 * Sends form data and content. Does not send form configuration.
 * <ul>
 * <li>Client->Server (inform server about status and data edited on client).
 * <li>Server->client (inform client about status and data changed on serer).
 * </ul>
 * If fieldContent has size zero, then only status flags should be updated.
 */
public class FormContent extends Primitive {
	// KT: form unique identification;
	public String formId;	

	// KT: form status 
	// Note: When receiveng NEW status, mobile client interprets is as RECEIVED.
	public byte formStatus = FORM_UNDEFINED;
	static public final byte FORM_UNDEFINED = 0;
	static public final byte FORM_NEW = 1;
	static public final byte FORM_RECEIVED = 2;
	static public final byte FORM_READ = 3;
	static public final byte FORM_EDITED = 4;
	
	// KT: last time the form was edited (set status to EDITED)
	// and first time the for was read (set status to READ)
	public Date lastEditDate = null;
	public Date firstReadDate = null;

	// KT: Data Fields
	// Note: Not all fields must be sent. The vector may be empty if only status
	// has changed, ou may contain only changed fields.
	public Vector fieldContent = new Vector();

	public void add(FieldAndContentBean beanToInsert) {
		fieldContent.addElement(beanToInsert);
	}

	public void add(String fieldName, String fieldValue) {
		add(new FieldAndContentBean(fieldName, fieldValue));
	}
	
	public int size() {
		return fieldContent.size();
	}

	public FieldAndContentBean get(int index) {
		return (FieldAndContentBean) fieldContent.elementAt(index);
	}

	public String toString() {
		String result = super.toString() + " ("
		+ "KT ID: " + formId + ", "
		+ "KT Status: " + Integer.toString(formStatus) + ", "
		+ "KT Read: " + firstReadDate.toString() + ", "
		+ "KT Edit: " + lastEditDate.toString() + ", ";
		for (int i = 0; i < fieldContent.size(); i++) {
			if (i > 0) {
				result += ", ";
			}
			result += this.get(i);
		} // for i
		result += ")";
		return result;
	}
}
