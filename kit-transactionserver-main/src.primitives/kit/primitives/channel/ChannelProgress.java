package kit.primitives.channel;

import kit.primitives.base.Primitive;

/**
 * Notifies the progress of channel communication.
 * 
 * @author Daniel Felix Ferber and José Flávio Aguilar Paulino
 */
public class ChannelProgress extends Primitive {
	public short numberOfSteps = 0;
	
	public String toString() {
		return super.toString() + " ChannelProgress ( number of steps " + Integer.toString(numberOfSteps) + " )";
	}
	
}
