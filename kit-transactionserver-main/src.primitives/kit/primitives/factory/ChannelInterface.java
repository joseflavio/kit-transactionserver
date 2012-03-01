package kit.primitives.factory;

import kit.primitives.base.Primitive;

public interface ChannelInterface {
	public boolean isChannelAuthenticated();
	public void    pushPrimitiveToServer(Primitive primitive);
}
