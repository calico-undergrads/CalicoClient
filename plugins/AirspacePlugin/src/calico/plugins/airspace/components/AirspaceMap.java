package calico.plugins.airspace.components;


import java.awt.Image;

import calico.components.CGroup;
import calico.components.CGroupImage;
import calico.inputhandlers.CalicoInputManager;
import calico.networking.netstuff.CalicoPacket;
import calico.plugins.airspace.AirspacePluginNetworkCommands;
import calico.plugins.airspace.inputhandlers.SimplifiedInputHandler;

public class AirspaceMap extends CGroupImage {

	private static final long serialVersionUID = 1L;

	public AirspaceMap(long uuid, long cuid, Image image)
	{
		super(uuid,cuid,image);
		networkLoadCommand = AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD;
	}
	
	@Override
	public void setInputHandler()
	{	
		CalicoInputManager.addCustomInputHandler(this.uuid, new SimplifiedInputHandler(this.uuid));	
	}	
	
	/**
	 * Serialize this activity node in a packet
	 */
	@Override
	public CalicoPacket[] getUpdatePackets(long uuid, long cuid, long puid,
			int dx, int dy, boolean captureChildren) {
		
		//Creates the packet for saving this CGroup
		CalicoPacket packet = super.getUpdatePackets(uuid, cuid, puid, dx, dy,
				captureChildren)[0];

		//Appends my own informations to the end of the packet
		//Example: packet.putDouble(responseTime);
		return new CalicoPacket[] { packet };

	}
	
	@Override
	public int get_signature() {
		//Return 0 since this is client side only
		return 0;
	}

}
