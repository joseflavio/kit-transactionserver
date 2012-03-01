package kit.primitives.forms;

/**
 * Holds the a tuple with then field name and the field value.
 * @author José Flávio
 */
public class FieldAndContentBean {

	private String fieldName;
	private String content;
		
	public FieldAndContentBean(String fieldName, String content) {
		super();
		this.fieldName = fieldName;
		this.content = content;
	}//contructor

	public String getContent() {
		return content;
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public String toString() {
		return fieldName + ": " + content;
	}
	
}//bean class :: FieldAndContenBean
