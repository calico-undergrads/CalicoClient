package calico.plugins.airspace;

import org.apache.log4j.Logger;

import calico.CalicoOptions;
import calico.components.menus.CanvasMenuBar;
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

/**
 * 
 * @author Calico Undergrads Research Team 1 : Fall2012
 *
 */
public class AirspacePlugin extends CalicoPlugin implements
		CalicoEventListener {

	public static Logger logger = Logger.getLogger(AirspacePlugin.class
			.getName());

	/**
	 * Constructor for the plugin. 
	 * 1. Set PluginInfo.name
	 * 2. Set the Icon Theme via plugin specific Icon Manger
	 *
	 */
	public AirspacePlugin() {
		super();
		PluginInfo.name = "AirspacePlugin";
		AirspaceIconManager.setIconTheme(this.getClass(), CalicoOptions.core.icontheme);
	}

	/**
	 * This is the main plugin hook that the Calico Core uses to instantiate the plugin.
	 * 
	 * You can/should:
	 * 1. Register Events that the plugin should listen for. 
	 * 2. Set UI elements that need to be added to UI
	 */
	public void onPluginStart() {
		// Register for events that this plugin will perform
		for (Integer event : this.getNetworkCommands())
			CalicoEventHandler.getInstance().addListener(event.intValue(),
					this, CalicoEventHandler.ACTION_PERFORMER_LISTENER);

		// Set a button for what we are trying to achieve. (Statis Bar is at bottom of the UI)
		CanvasStatusBar.addMenuButtonRightAligned(AirspaceMapButton.class);

		// Menubar is at left & right of UI.
		CanvasMenuBar.addMenuButton(AirspaceMapButton.class);
	}

	/**
	 * Main plugin hook that gets called when Calico is getting ready to shutdown the plugin.
	 */
	public void onPluginEnd() {
		// No actions to perform on plugin end.
	}

	
	/**
	 * Returns the class that stores the network commands:value mappings for this plugin.
	 * @return AirspacePluginNetworkCommands.class
	 */
	@Override
	public Class<?> getNetworkCommandsClass() {
		return AirspacePluginNetworkCommands.class;
	}

	
	/**
	 * Methods that handles events and delegates tasks out to appropriate methods.
	 * 
	 * @param eventId 		Will be matched against the plugin's Network Commands
	 * @param networkPacket Holds actual data to be passed to the delegate method
	 */
	@Override
	public void handleCalicoEvent(int eventId, CalicoPacket networkPacket) {
		switch (eventId) {
		case AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD:
			loadOverheadMap(networkPacket);
			break;
		case AirspacePluginNetworkCommands.COMMAND_B:
			loadOverheadMap(networkPacket);
			break;
		}
	}

	/**
	 * Hook method called from the plugin's UI button element, after being acted upon.
	 * 
	 * @param com		Packet identifier - int that typically gets tossed out.
	 * @param params	Array holding other params passed that will be put into a packet
	 */
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
	 * Load the Overhead Map.
	 * 
	 * @param paramsPacket
	 */
	public void loadOverheadMap(CalicoPacket paramsPacket) {
//		AirspacePlugin.UI_send_command(AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD, new_uuid, 
//				cuuid, x, y,file.getName());
		paramsPacket.rewind();
		paramsPacket.getInt();
		long uuid = paramsPacket.getLong();
		long cuuid = paramsPacket.getLong();
		int x = paramsPacket.getInt();
		int y = paramsPacket.getInt();
		double latitude = paramsPacket.getDouble();
		double longitude = paramsPacket.getDouble();
		
		AirspaceMapController.loadOverheadMap(uuid, cuuid, x, y, latitude, longitude);
		
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
