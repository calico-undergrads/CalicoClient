package calico.plugins.loadImageURL;

import java.io.IOException;

import org.apache.log4j.Logger;

import calico.Calico;
import calico.CalicoOptions;
import calico.components.menus.CanvasMenuBar;
import calico.components.menus.CanvasStatusBar;
import calico.events.CalicoEventHandler;
import calico.events.CalicoEventListener;
import calico.networking.Networking;
import calico.networking.PacketHandler;
import calico.networking.netstuff.CalicoPacket;
import calico.plugins.CalicoPlugin;
import calico.plugins.loadImageURL.components.buttons.CreateCustomScrapButton;
import calico.plugins.loadImageURL.controllers.CustomScrapController;
import calico.plugins.loadImageURL.controllers.GoogleMapImageController;
import calico.plugins.loadImageURL.iconsets.CalicoIconManager;

public class LoadImageUrl extends CalicoPlugin implements CalicoEventListener {
	// the logger for this plugin
	public static Logger logger = Logger
			.getLogger(LoadImageUrl.class.getName());

	/**
	 * Construct the Plugin Object
	 */
	public LoadImageUrl() {
		super();
		PluginInfo.name = "LoadImageURL";
		calico.plugins.loadImageURL.iconsets.CalicoIconManager.setIconTheme(
				this.getClass(), CalicoOptions.core.icontheme);
	}

	
	/**
	 * Plugin Start Method
	 */
	public void onPluginStart() {
		// Register for events that this plugin will perform
		for (Integer event : this.getNetworkCommands())
			CalicoEventHandler.getInstance().addListener(event.intValue(),
					this, CalicoEventHandler.ACTION_PERFORMER_LISTENER);

		CanvasStatusBar
				.addMenuButtonRightAligned(CreateCustomScrapButton.class);

		// Add an additional voice to the bubble menu
		// CGroup.registerPieMenuButton(SaveToPaletteButton.class);

		// Register to the events I am interested in from Calico's core events
		// Example:
		// CalicoEventHandler.getInstance().addListener(NetworkCommand.VIEWING_SINGLE_CANVAS,
		// this, CalicoEventHandler.PASSIVE_LISTENER);

	}

	/**
	 * Things to do when program exits
	 */
	public void onPluginEnd() {
		// do nothing
	}

	@Override
	public Class<?> getNetworkCommandsClass() {
		return LoadImageUrlNetworkCommands.class;
	}

	@Override
	/**
	 * Plugin specific commands can be defined
	 * in the example plugin network commands class
	 */
	public void handleCalicoEvent(int event, CalicoPacket p) {
		switch (event) {
		case LoadImageUrlNetworkCommands.LOAD_NEW_IMAGE:
			LOAD_NEW_IMAGE(p); // See this class for implementation
			break;
		}
	}

	/*************************************************
	 * UI ENTRY POINTS The user interface calls those methods which create the
	 * command packets to send to the network
	 *************************************************/

	/**
	 *  Called by calico.plugin.loadImageURL.component.button.CreateCustomScrapButton event handler method
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

	/*************************************************
	 * COMMANDS This is where the actual commands are received and processed by
	 * the different controllers
	 *************************************************/
	/**
	 * Called handleCalicoEvent
	 * @param packet - Passed from global calico 
	 */
	private void LOAD_NEW_IMAGE(CalicoPacket p) {
		p.rewind();
		p.getInt(); // Throw Away
		long new_uuid = p.getLong();
		long cuuid = p.getLong();
		int x = p.getInt();
		int y = p.getInt();

		// CustomScrapController.load_new_image(new_uuid, cuuid, x, y);
		GoogleMapImageController.addImage(cuuid, new_uuid,
				"https://www.google.com/images/srpr/logo3w.png");

	}
}
