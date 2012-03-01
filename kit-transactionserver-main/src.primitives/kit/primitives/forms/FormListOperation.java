package kit.primitives.forms;

import java.util.Vector;

import kit.primitives.base.Primitive;

/**
 * Sends a list of formIds of a specific templateId.
 * 
 * Server->Client (type = remove) Server->Client (type = new) Server->Client
 * (type = restore)
 */
public class FormListOperation extends Primitive {

	public byte type = 0;
	public static final byte NEW    = 1;
	public static final byte DELETE = 2;

	public Vector list = new Vector();
	
	public void add(String formIdToInsert) {
		list.addElement(formIdToInsert);
	}

	public int size() {
		return list.size();
	}

	public String get(int index) {
		return (String) list.elementAt(index);
	}

	public Vector getAllRowIds() {
		return list;
	}
	
	public String toString() {
		String result = super.toString() + " (" + Integer.toString(type);
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				result += ", ";
			}
			result += this.get(i);
		}
		result += ")";
		return result;
	}
}
