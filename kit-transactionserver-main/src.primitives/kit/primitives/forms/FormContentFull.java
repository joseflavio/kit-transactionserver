package kit.primitives.forms;

/**
 * Sends full form content: data, status and configuration. 
 * 
 * <ul>
 * <li>Server->client (add new form on client, change form on client, send form that
 * is missing on client).
 * </ul>
 */
public class FormContentFull extends FormContent {
	
	// Title shown on top of form.
	public String title    = "";
	
	// Which template is used to show form on client. 
	public String templateId;

	// Not empty if forms should be grouped
	public String category = "default";

	// How and where to show form. 
	public byte showFlags = 0;
	
	static public final byte SHOW_UNDEFINED    =  0;
	static public final byte SHOW_ON_START     =  1;
	static public final byte SHOW_ON_RECEIVE   =  2;
	static public final byte SHOW_ALWAYS       =  4;
	static public final byte SHOW_AT_TOP       =  8;
	static public final byte SHOW_AS_IMPORTANT = 16;
	static public final byte SHOW_IN_MENU      = 32;
	
	public String toString() {
		return super.toString() + " ("
		+ "KT Template: " + templateId + ", "
		+ "KT Title: " + title + ", "
		+ "KT Categogy: " + category + ", "
		+ "KT Show Flags: " + Integer.toString(showFlags) + ", "
		+ ")";
	}

}
