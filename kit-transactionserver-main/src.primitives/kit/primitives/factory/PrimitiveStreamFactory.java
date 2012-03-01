package kit.primitives.factory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import kit.primitives.authentication.AuthenticationRequest;
import kit.primitives.authentication.AuthenticationResponse;
import kit.primitives.authentication.AuthenticationStreamer;
import kit.primitives.base.KitDataInputReader;
import kit.primitives.base.KitDataOutputWriter;
import kit.primitives.base.Primitive;
import kit.primitives.base.PrimitiveStreamer;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.channel.ChannelProgress;
import kit.primitives.channel.ChannelStreamer;
import kit.primitives.echo.Echo;
import kit.primitives.echo.EchoStreamer;
import kit.primitives.exceptions.PrimitiveRuntimeException;
import kit.primitives.exceptions.UnauthenticatedPrimitiveException;
import kit.primitives.forms.FormContent;
import kit.primitives.forms.FormContentFull;
import kit.primitives.forms.FormIdOperation;
import kit.primitives.forms.FormListOperation;
import kit.primitives.forms.FormOperation;
import kit.primitives.forms.FormStreamer;

public final class PrimitiveStreamFactory {

	public static final String AUTHENTICATION_RESPONSE = "AUTH-RESP";
	public static final String AUTHENTICATION_REQUEST = "AUTH-REQU";
	public static final String ECHO = "ECHO";
	public static final String CHANNEL = "CHANNEL";
	public static final String COMMUNICATION = "COMM";
	public static final String FORM_CONTENT = "FORM-CONT";
	public static final String FORM_CONTENT_FULL = "FORM-FULL";
	public static final String FORM_LIST_CONTENT = "FORM-LIST";
	public static final String FORM_DATA_OPERATION = "FORM-DATA";
	public static final String FORM_OPERATION = "FORM-OPER";
	public static final String CHANNEL_PROGRESS = "CHANNEL-PROG";

	private static PrimitiveStreamFactory streamFactory = new PrimitiveStreamFactory();

	private final Hashtable classToId = new Hashtable();
	private final Hashtable idToClass = new Hashtable();
	private final Hashtable classToStreamers = new Hashtable();

	private final FormStreamer formStreamer = new FormStreamer();
	private final EchoStreamer echoStreamer = new EchoStreamer();
	private final ChannelStreamer channelStreamer = new ChannelStreamer();
	private final AuthenticationStreamer authenticationStreamer = new AuthenticationStreamer();

	private PrimitiveStreamFactory() {
		this.idToClass.put(AUTHENTICATION_REQUEST, AuthenticationRequest.class);
		this.idToClass.put(AUTHENTICATION_RESPONSE, AuthenticationResponse.class);
		this.idToClass.put(ECHO, Echo.class);
		this.idToClass.put(CHANNEL, ChannelNotification.class);
		this.idToClass.put(FORM_CONTENT, FormContent.class);
		this.idToClass.put(FORM_CONTENT_FULL, FormContentFull.class);
		this.idToClass.put(FORM_LIST_CONTENT, FormListOperation.class);
		this.idToClass.put(FORM_DATA_OPERATION, FormIdOperation.class);
		this.idToClass.put(FORM_OPERATION, FormOperation.class);
		this.idToClass.put(CHANNEL_PROGRESS, ChannelProgress.class);

		Enumeration en = this.idToClass.keys();
		while (en.hasMoreElements()) {
			String currentId = (String) en.nextElement();
			Class currentClass = (Class) this.idToClass.get(currentId);
			this.classToId.put(currentClass, currentId);
		}

		this.classToStreamers.put(AuthenticationRequest.class, this.authenticationStreamer);
		this.classToStreamers.put(AuthenticationResponse.class, this.authenticationStreamer);
		this.classToStreamers.put(Echo.class, this.echoStreamer);
		this.classToStreamers.put(ChannelNotification.class, this.channelStreamer);
		this.classToStreamers.put(FormContent.class, this.formStreamer);
		this.classToStreamers.put(FormContentFull.class, this.formStreamer);
		this.classToStreamers.put(FormListOperation.class, this.formStreamer);
		this.classToStreamers.put(FormIdOperation.class, this.formStreamer);
		this.classToStreamers.put(FormOperation.class, this.formStreamer);
		this.classToStreamers.put(ChannelProgress.class, this.channelStreamer);
	}

	// ----------------------------------------------------------------------

	private static PrimitiveStreamFactory getInstance() {
		if (streamFactory == null) {
			streamFactory = new PrimitiveStreamFactory();
		}
		return streamFactory;
	}

	public static PrimitiveStreamer getStreamerByClass(final Class primitiveClass) {
		return (PrimitiveStreamer) PrimitiveStreamFactory.getInstance().classToStreamers.get(primitiveClass);
	}

	public static Class getClassById(final String primitiveId) {
		return (Class) PrimitiveStreamFactory.getInstance().idToClass.get(primitiveId);
	}

	public static String getIdByClass(final Class primitiveClass) {
		return (String) PrimitiveStreamFactory.getInstance().classToId.get(primitiveClass);
	}

	// ----------------------------------------------------------------------

	public static void writePrimitive(final KitDataOutputWriter output, final List<Primitive> primitiveList) throws IOException {
	    for(int i=0; i < primitiveList.size(); ++i) {
	        PrimitiveStreamFactory.writePrimitive( output, primitiveList.get(i) );
	    }
	    output.flush();
	}

	private static void writePrimitive(final KitDataOutputWriter output, final Primitive primitive) throws IOException {
		Class primitiveClass = primitive.getClass();
		String primitiveId = PrimitiveStreamFactory.getIdByClass(primitiveClass);
		if (primitiveId == null) {
            throw new PrimitiveRuntimeException("Unkown primitive primitive class: " + primitiveClass.getName());
        }

		output.writeUTF(primitiveId);
		PrimitiveStreamer primitiveWriter = PrimitiveStreamFactory.getStreamerByClass(primitiveClass);
		if (primitiveWriter == null) {
            throw new PrimitiveRuntimeException("Unkown primitive writer for: " + primitiveId);
        }
		primitiveWriter.write(output, primitive);
	}

	public static Primitive readPrimitive(final KitDataInputReader dis) throws IOException {
		Class primitiveClass = PrimitiveStreamFactory.readPrimitiveClass(dis);
		return PrimitiveStreamFactory.finishAssemblyingPrimitive(dis, primitiveClass);
	}

	public static Primitive readPrimitiveVerifyingAuthentication(final KitDataInputReader input,
			final ChannelInterface channelInterface, final RequireAuthentication tester) throws IOException,
			UnauthenticatedPrimitiveException {

		Class primitiveClass = PrimitiveStreamFactory.readPrimitiveClass(input);

		if (tester.requiresAuthentication(primitiveClass)) {
			if (channelInterface.isChannelAuthenticated()) {
				return PrimitiveStreamFactory.finishAssemblyingPrimitive(input, primitiveClass);
			} else {
				throw new UnauthenticatedPrimitiveException(primitiveClass);
			}// if-else
		} else {
			return PrimitiveStreamFactory.finishAssemblyingPrimitive(input, primitiveClass);
		}// if-else
	}// private method :: readPrimitiveVerifyingAuthentication(..)

	// ----------------------------------------------------------------------

	private static Class readPrimitiveClass(final KitDataInputReader input) throws IOException {
		String primitiveId;
		primitiveId = input.readUTF();
		Class primitiveClass = PrimitiveStreamFactory.getClassById(primitiveId);
		if (primitiveId == null) {
            throw new PrimitiveRuntimeException("Unkown primitive ID: " + primitiveId);
        }
		return primitiveClass;
	}

	private static Primitive finishAssemblyingPrimitive(final KitDataInputReader input, final Class primitiveClass) throws IOException {
		PrimitiveStreamer primitiveReader = PrimitiveStreamFactory.getStreamerByClass(primitiveClass);
		if (primitiveReader == null) {
			throw new PrimitiveRuntimeException("Unkown primitive reader: "
					+ PrimitiveStreamFactory.getIdByClass(primitiveClass));
		}

		try {
			Primitive primitive = (Primitive) primitiveClass.newInstance();
			primitiveReader.read(input, primitive);
			return primitive;
		} catch (InstantiationException e) {
			throw new PrimitiveRuntimeException("Cannot create primitive for: "
					+ PrimitiveStreamFactory.getIdByClass(primitiveClass));
		} catch (IllegalAccessException e) {
			throw new PrimitiveRuntimeException("Cannot create primitive for: "
					+ PrimitiveStreamFactory.getIdByClass(primitiveClass));
		}
	}

}
