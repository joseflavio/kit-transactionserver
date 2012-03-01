package kit.primitives.forms;

import java.io.IOException;
import java.util.Date;

import kit.primitives.base.KitDataInputReader;
import kit.primitives.base.KitDataOutputWriter;
import kit.primitives.base.Primitive;
import kit.primitives.base.PrimitiveStreamer;

public class FormStreamer extends PrimitiveStreamer {

	@Override
    public void read(final KitDataInputReader in, final Primitive primitive) throws IOException {
		super.read(in, primitive);
		if (primitive instanceof FormContent) {
			FormContent formContent = (FormContent) primitive;

			formContent.formId = in.readUTF();
			formContent.formStatus = in.readByte();

			long value;

			value = in.readLong();
			if (value != 0) {
                formContent.firstReadDate = new Date(value);
            }
            else {
                formContent.firstReadDate = null;
            }

			value = in.readLong();
			if (value != 0) {
                formContent.lastEditDate = new Date(value);
            }
            else {
                formContent.lastEditDate = null;
            }

			if (formContent instanceof FormContentFull) {
				FormContentFull formContentFull = (FormContentFull) formContent;
				formContentFull.templateId = in.readUTF();
				formContentFull.title = in.readUTF();
				formContentFull.category = in.readUTF();
				formContentFull.showFlags = in.readByte();
			}

			int numberOfRegisters = in.readInt();
			for (int i = 0; i < numberOfRegisters; i++) {
				String fieldName = in.readUTF();
				String content = in.readUTF();
				formContent.add(new FieldAndContentBean(fieldName, content));
			}

		} else if (primitive instanceof FormListOperation) {
			FormListOperation formListContent = (FormListOperation) primitive;
			formListContent.type = in.readByte();

			int numberOfRegisters = in.readInt();
			for (int i = 0; i < numberOfRegisters; i++) {
				formListContent.add(in.readUTF());
			}
		} else if (primitive instanceof FormIdOperation) {
			FormIdOperation formOperation = (FormIdOperation) primitive;

			formOperation.formId = in.readUTF();
			formOperation.type = in.readByte();
		} else if (primitive instanceof FormOperation) {
			FormOperation operation = (FormOperation) primitive;

			operation.type = in.readByte();
		}
	}

	@Override
    public void write(final KitDataOutputWriter out, final Primitive primitive) throws IOException {
		super.write(out, primitive);
		if (primitive instanceof FormContent) {
			FormContent formContent = (FormContent) primitive;

			out.writeUTF(formContent.formId);
			out.writeByte(formContent.formStatus);

			if (formContent.firstReadDate == null) {
                out.writeLong(0);
            }
            else {
                out.writeLong(formContent.firstReadDate.getTime());
            }

			if (formContent.lastEditDate == null) {
                out.writeLong(0);
            }
            else {
                out.writeLong(formContent.lastEditDate.getTime());
            }

			if (formContent instanceof FormContentFull) {
				FormContentFull formContentFull = (FormContentFull) formContent;
				out.writeUTF(formContentFull.templateId);
				out.writeUTF(formContentFull.title);
				out.writeUTF(formContentFull.category);
				out.writeByte(formContentFull.showFlags);
			}

			out.writeInt(formContent.size());
			for (int i = 0; i < formContent.size(); i++) {
				FieldAndContentBean bean = formContent.get(i);
				out.writeUTF(bean.getFieldName());
				out.writeUTF(bean.getContent());
			}
		} else if (primitive instanceof FormListOperation) {
			FormListOperation formListContent = (FormListOperation) primitive;
			out.writeByte(formListContent.type);

			out.writeInt(formListContent.size());
			for (int i = 0; i < formListContent.size(); i++) {
				out.writeUTF(formListContent.get(i));
			}
		} else if (primitive instanceof FormIdOperation) {
			FormIdOperation formOperation = (FormIdOperation) primitive;

			out.writeUTF(formOperation.formId);
			out.writeByte(formOperation.type);
		} else if (primitive instanceof FormOperation) {
			FormOperation operation = (FormOperation) primitive;

			out.writeByte(operation.type);
		}
	}

}
