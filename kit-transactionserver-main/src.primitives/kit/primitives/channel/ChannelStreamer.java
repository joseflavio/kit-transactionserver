package kit.primitives.channel;

import java.io.IOException;

import kit.primitives.base.KitDataInputReader;
import kit.primitives.base.KitDataOutputWriter;
import kit.primitives.base.Primitive;
import kit.primitives.base.PrimitiveStreamer;

public class ChannelStreamer extends PrimitiveStreamer {

	@Override
    public void read(final KitDataInputReader in, final Primitive primitive) throws IOException {
		super.read(in, primitive);
		if (primitive instanceof ChannelNotification) {
			ChannelNotification channelNotification = (ChannelNotification) primitive;
			channelNotification.type = in.readByte();
		} else if (primitive instanceof ChannelProgress) {
			ChannelProgress channelProgress = (ChannelProgress) primitive;
			channelProgress.numberOfSteps = in.readShort();
		}
	}

	@Override
    public void write(final KitDataOutputWriter out, final Primitive primitive) throws IOException {
		super.write(out, primitive);
		if (primitive instanceof ChannelNotification) {
			ChannelNotification channelNotification = (ChannelNotification) primitive;
			out.writeByte(channelNotification.type);
		} else if (primitive instanceof ChannelProgress) {
			ChannelProgress channelProgress = (ChannelProgress) primitive;
			out.writeShort(channelProgress.numberOfSteps);
		}
	}
}
