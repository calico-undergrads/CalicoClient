package calico.plugins.airspace;

import org.apache.log4j.Logger;

import calico.CalicoOptions;
import calico.components.menus.CanvasStatusBar;
import calico.events.CalicoEventHandler;
import calico.events.CalicoEventListener;
import calico.networking.PacketHandler;
import calico.networking.netstuff.CalicoPacket;
import calico.plugins.CalicoPlugin;
import calico.plugins.airspace.components.buttons.AirspaceMapButton;
import calico.plugins.airspace.controllers.AirspaceMapController;
import calico.plugins.airspace.iconsets.AirspaceIconManager;
//import com.javadocmd.simplelatlng.util.LatLngConfig;


public class AirspacePlugin extends CalicoPlugin implements
		CalicoEventListener {

	public static Logger logger = Logger.getLogger(AirspacePlugin.class
			.getName());

	public AirspacePlugin() {
		super();
		PluginInfo.name = "AirspacePlugin";
		AirspaceIconManager.setIconTheme(this.getClass(), CalicoOptions.core.icontheme);
	}

	public void onPluginStart() {
		// Register for events that this plugin will perform
		for (Integer event : this.getNetworkCommands())
			CalicoEventHandler.getInstance().addListener(event.intValue(),
					this, CalicoEventHandler.ACTION_PERFORMER_LISTENER);

		// Set a button for what we are trying to achieve.
		CanvasStatusBar
				.addMenuButtonRightAligned(AirspaceMapButton.class);
	}

	public void onPluginEnd() {
		// No actions to perform on plugin end.
	}

	@Override
	public Class<?> getNetworkCommandsClass() {
		return AirspacePluginNetworkCommands.class;
	}

	@Override
	public void handleCalicoEvent(int eventId, CalicoPacket networkPacket) {
		switch (eventId) {
		case AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD:
			doActionA(networkPacket);
			break;
		case AirspacePluginNetworkCommands.COMMAND_B:
			doActionA(networkPacket);
			break;
		}
	}

	public static void UI_send_command(int com, Object... params) {
		// Create the packet
		CalicoPacket p;
		if (params != null) {
			p = CalicoPacket.getPacket(com, params);
		} else {
			p = CalicoPacket.getPacket(com);
		}

		p.rewind();
		// Send the packet locally
		PacketHandler.receive(p);

		// Send the packet to the network (server)
		// Networking.send(p);
	}

	/**
	 * Actions to perform when an eventId is matched to a defined
	 * NetworkCommand.
	 * 
	 */

	/**
	 * Take action "A"
	 * 
	 * @param paramsPacket
	 */
	public void doActionA(CalicoPacket paramsPacket) {
//		AirspacePlugin.UI_send_command(AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD, new_uuid, 
//				cuuid, x, y,file.getName());
		paramsPacket.rewind();
		paramsPacket.getInt();
		long uuid = paramsPacket.getLong();
		long cuuid = paramsPacket.getLong();
		int x = paramsPacket.getInt();
		int y = paramsPacket.getInt();
		
		AirspaceMapController.doActionA(uuid, cuuid, x, y);
		
//		String fileName = paramsPacket.getString();
		
		
		
	}

	/**
	 * Take action "B"
	 * 
	 * @param paramsPacket
	 */
	public void doActionB(CalicoPacket paramsPacket) {
		paramsPacket.rewind();
		paramsPacket.getInt();
		long uuid = paramsPacket.getLong();
		long cuuid = paramsPacket.getLong();
		int x = paramsPacket.getInt();
		int y = paramsPacket.getInt();
		
//		AirspaceMapController.doActionB(uuid, cuuid, x, y);
		AirspaceMapController.doActionB(uuid, cuuid, x, y);
		
	}
}
