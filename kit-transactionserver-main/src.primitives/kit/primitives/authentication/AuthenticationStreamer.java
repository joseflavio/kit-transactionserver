package kit.primitives.authentication;

import java.io.IOException;

import kit.primitives.base.KitDataInputReader;
import kit.primitives.base.KitDataOutputWriter;
import kit.primitives.base.Primitive;
import kit.primitives.base.PrimitiveStreamer;

/**
 * Serializes authentication primitives. (AuthenticationRequest and AuthenticationResponse).
 *
 * @author Daniel Felix Ferber and Jos� Fl�vio Aguilar Paulino
 */
public class AuthenticationStreamer extends PrimitiveStreamer {

	@Override
    public void read(final KitDataInputReader in, final Primitive primitive) throws IOException {
		super.read(in, primitive);
		if (primitive instanceof AuthenticationRequest) {
			AuthenticationRequest request = (AuthenticationRequest) primitive;
			request.clientId = in.readUTF();
			request.password = in.readUTF();
			request.type = in.readByte();
			request.installationId1 = in.readLong();
			request.installationId2 = in.readLong();
		} else if (primitive instanceof AuthenticationResponse) {
			AuthenticationResponse response = (AuthenticationResponse) primitive;
			response.type = in.readByte();
		}
	}

	@Override
    public void write(final KitDataOutputWriter out, final Primitive primitive) throws IOException {
		super.write(out, primitive);
		if (primitive instanceof AuthenticationRequest) {
			AuthenticationRequest request = (AuthenticationRequest) primitive;
			out.writeUTF(request.clientId);
			out.writeUTF(request.password);
			out.writeByte(request.type);
			out.writeLong(request.installationId1);
			out.writeLong(request.installationId2);
		} else if (primitive instanceof AuthenticationResponse) {
			AuthenticationResponse response = (AuthenticationResponse) primitive;
			out.writeByte(response.type);
		}
	}
}
