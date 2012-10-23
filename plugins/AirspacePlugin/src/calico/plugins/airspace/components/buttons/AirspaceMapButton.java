package calico.plugins.airspace.components.buttons;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import calico.Calico;
import calico.CalicoDataStore;
import calico.CalicoDraw;
import calico.components.menus.CanvasMenuButton;
import calico.controllers.CCanvasController;
import calico.controllers.CImageController;
import calico.inputhandlers.InputEventInfo;
import calico.networking.Networking;
import calico.plugins.airspace.AirspacePlugin;
import calico.plugins.airspace.AirspacePluginNetworkCommands;
import calico.plugins.airspace.iconsets.AirspaceIconManager;


public class AirspaceMapButton extends CanvasMenuButton{

	private static final long serialVersionUID = 1L;

	public AirspaceMapButton(long canvasUUID)
	{
		super();
		cuid = canvasUUID;
		
		iconString = "plugins.airspaceplugin.customIcon";
		try
		{
//			Image img = CanvasGenericMenuBar.getTextImage("  UserList  ", 
//				new Font("Verdana", Font.BOLD, 12));
			setImage(AirspaceIconManager.getIconImage(iconString));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void actionMouseClicked(InputEventInfo event)
	{	
		if (event.getAction() == InputEventInfo.ACTION_PRESSED)
		{
			super.onMouseDown();
		}
		else if (event.getAction() == InputEventInfo.ACTION_RELEASED && isPressed)
		{
//			final JFileChooser fc = new JFileChooser();
//			fc.setFileFilter(new ImageFileFilter());
//	        int returnVal = fc.showOpenDialog(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getComponent());
	
//	        if (returnVal == JFileChooser.APPROVE_OPTION) {
//	            File file = fc.getSelectedFile();
				URL url = null;
				try {
					url = new URL("http://maps.googleapis.com/maps/api/staticmap?center=33.6694,-117.8222&zoom=12&size=600x600&maptype=roadmap&sensor=false");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
		            // Read the image ...
		        InputStream inputStream = null;
				try {
					inputStream = url.openStream();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        ByteArrayOutputStream output = new ByteArrayOutputStream();
		        byte [] buffer               = new byte[ 1024 ];
	
		        int n = 0;
		        try {
					while (-1 != (n = inputStream.read(buffer))) {
					   output.write(buffer, 0, n);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        try {
					inputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
		        // Here's the content of the image...
		        byte [] data = output.toByteArray();
//				File file = 
	            long new_uuid = Calico.uuid();
	            try {
	            	CImageController.save_to_disk(new_uuid, new_uuid + ".png", 
	            			data);
	            }
	            catch (IOException e)
	            {
	            	e.printStackTrace();
	            }
	            
				
				long cuuid=CCanvasController.getCurrentUUID();
				int x= new Double(Math.random() * 100).intValue() - 50 + CalicoDataStore.ScreenWidth / 3;
				int y= new Double(Math.random() * 100).intValue() - 50 + CalicoDataStore.ScreenHeight / 3;
				AirspacePlugin.UI_send_command(AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD, new_uuid, 
						cuuid, x, y);
				
//	            Networking.send(CImageController.getImageTransferPacket(Calico.uuid(), CCanvasController.getCurrentUUID(), 
//	            		50, 50, file));
//			}
			
			//send command to UI
//			long new_uuid = Calico.uuid();
//			long cuuid=CCanvasController.getCurrentUUID();
//			int x= new Double(Math.random() * 100).intValue() - 50 + CalicoDataStore.ScreenWidth / 3;
//			int y= new Double(Math.random() * 100).intValue() - 50 + CalicoDataStore.ScreenHeight / 3;
			AirspacePlugin.UI_send_command(AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD, new_uuid, cuuid, x, y);
			
			super.onMouseUp();
		}
	}
	
	/**
	 * Part of scaffolding
	 */
	public void highlight_on()
	{
		if (!isPressed)
		{
			isPressed = true;
			setSelected(true);
			final CanvasMenuButton tempButton = this;
			SwingUtilities.invokeLater(
					new Runnable() { public void run() { 
						double tempX = tempButton.getX();
						double tempY = tempButton.getY();
								
						setImage(AirspaceIconManager.getIconImage(iconString));
						tempButton.setX(tempX);
						tempButton.setY(tempY);
					}});
			//CalicoDraw.setNodeX(this, tempX);
			//CalicoDraw.setNodeY(this, tempY);
			//this.repaintFrom(this.getBounds(), this);
			CalicoDraw.repaintNode(this);
		}
	}
	
	/**
	 * Part of scaffolding
	 */
	public void highlight_off()
	{
		if (isPressed)
		{
			isPressed = false;
			setSelected(false);
			final CanvasMenuButton tempButton = this;
			SwingUtilities.invokeLater(
					new Runnable() { public void run() { 
						double tempX = tempButton.getX();
						double tempY = tempButton.getY();
								
						setImage(AirspaceIconManager.getIconImage(iconString));
						tempButton.setX(tempX);
						tempButton.setY(tempY);
					}});
			//CalicoDraw.setNodeX(this, tempX);
			//CalicoDraw.setNodeY(this, tempY);
			//this.repaintFrom(this.getBounds(), this);
			CalicoDraw.repaintNode(this);
		}
	}	
	
	class ImageFileFilter extends javax.swing.filechooser.FileFilter 
	{
	    public boolean accept(File file) 
	    {
	    	if (file.isFile())
	    	{
    	        String filename = file.getName().toLowerCase();
    	        return filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".gif");
	    	}
	    	else
	    	{
	    		return true;
	    	}
	    }
	    
	    public String getDescription() 
	    {
	        return "*.png, *.jpg, *.gif";
	    }
	}

}
