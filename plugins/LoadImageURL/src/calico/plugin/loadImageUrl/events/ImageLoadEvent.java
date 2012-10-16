package calico.plugin.loadImageUrl.events;

import calico.networking.netstuff.CalicoPacket;
import calico.plugins.events.CalicoEvent;
/**
 * Custom Event Creation
 * 
 * @author tellez
 *
 */
public class ImageLoadEvent extends CalicoEvent {

	public ImageLoadEvent() {
		super();
	}

	public void getPacketData(CalicoPacket p) {
		// To be implemented by the plugin
	}

	public void execute() throws Exception {
	}
}
