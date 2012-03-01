package kit.primitives.echo;

import java.io.IOException;

import kit.primitives.base.KitDataInputReader;
import kit.primitives.base.KitDataOutputWriter;
import kit.primitives.base.Primitive;
import kit.primitives.base.PrimitiveStreamer;

/**
 * Reads an EchoRequest or EchoResponse primitive from stream.
 */
public class EchoStreamer extends PrimitiveStreamer {

	@Override
    public void read(final KitDataInputReader in, final Primitive primitive) throws IOException {
		super.read(in, primitive);
		Echo echo = (Echo) primitive;
		echo.type = in.readShort();
		echo.content = in.readUTF();
	}

	@Override
    public void write(final KitDataOutputWriter out, final Primitive primitive) throws IOException {
		super.write(out, primitive);
		Echo echo = (Echo) primitive;
		out.writeShort(echo.type);
		out.writeUTF(echo.content);
	}

}
