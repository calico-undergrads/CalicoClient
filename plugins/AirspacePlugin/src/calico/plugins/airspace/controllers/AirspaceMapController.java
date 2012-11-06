package calico.plugins.airspace.controllers;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


import calico.CalicoDraw;
import calico.components.CGroup;
import calico.components.CStroke;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.controllers.CImageController;
import calico.controllers.CStrokeController;
import calico.plugins.airspace.components.AirspaceMap;
import calico.plugins.airspace.components.AirspaceStroke;
import calico.utils.Geometry;
//import com.javadocmd.simplelatlng.util.LatLngConfig;


public class AirspaceMapController {

	/**
	 * Create the a polygon. Reads an image from the disk and adds it to the canvas
	 * 
	 * @param elementUUID
	 * @param canvasUUID
	 * @param posX
	 * @param posY
	 */
	public static void loadOverheadMap(long elementUUID, long canvasUUID, int posX, int posY, double latitude, double longitude) {
		
		// create shape
		GeneralPath myPolygon = new GeneralPath(new Rectangle(posX, posY, 120, 60));
		Polygon p = Geometry.getPolyFromPath(myPolygon.getPathIterator(null));

		// Read the image from Disk
		Image tempImage = null;
		try {
			tempImage = ImageIO.read(new File(CImageController.getImagePath(elementUUID)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// If the image loaded successfully, then create a new AirspaceMap element, and pass Image in
		if (tempImage != null)
		{
			// initialize custom scrap
			// Original Long/Lat: 33.648315, -117.847466
			CGroup group = new AirspaceMap(elementUUID, canvasUUID, tempImage, latitude, longitude, 8);
			System.out.println("<---------------"+ longitude +" || "+ latitude +"----------->");
			// create the scrap
			no_notify_create_custom_scrap_bootstrap(elementUUID, canvasUUID, group, p, "loaded map");
		}
	}
	
	public static void doActionB(long elementUUID, long canvasUUID, int posX, int posY){
		// create shape
		GeneralPath myPolygon = new GeneralPath(new Rectangle(posX, posY, 120, 60));
		Polygon p = Geometry.getPolyFromPath(myPolygon.getPathIterator(null));
		
			
	}

	/*************************************************
	 * UTILITY METHODS - Taken from Example Plugin
	 *************************************************/
	
	/**
	 * Create a custom `scrap`
	 * 
	 * This method utilized `no_notify_start` and `create_custom_shape`
	 * to create the UI elements.
	 * 
	 * @param uuid
	 * @param cuuid
	 * @param group
	 * @param p
	 * @param optText
	 */
	public static void no_notify_create_custom_scrap_bootstrap(long uuid,
			long cuuid, CGroup group, Polygon p, String optText) {
		no_notify_start(uuid, cuuid, 0l, true, group);
		CGroupController.setCurrentUUID(uuid);
		create_custom_shape(uuid, p);
		// Set the optional text to identify the scrap
		CGroupController.no_notify_set_text(uuid, optText);
		CGroupController.no_notify_finish(uuid, false, false, true);
		CGroupController.no_notify_set_permanent(uuid, true);
		CGroupController.recheck_parent(uuid);
	}

	/**
	 * Starts the creation of any of the activity diagram scrap
	 * 
	 * @param uuid Unique identifier for the group
	 * @param cuid Canvas Unique Id
	 * @param puid Parent Unique Id
	 * @param isperm Is the object permanent 
	 * @param customScrap
	 */
	public static void no_notify_start(long uuid, long cuid, long puid,
			boolean isperm, CGroup customScrap) {
		if (!CCanvasController.exists(cuid))
			return;
		if (CGroupController.exists(uuid)) {
			CGroupController.logger.debug("Need to delete group " + uuid);
			// CCanvasController.canvasdb.get(cuid).getLayer().removeChild(groupdb.get(uuid));
			CalicoDraw.removeChildFromNode(CCanvasController.canvasdb.get(cuid)
					.getLayer(), CGroupController.groupdb.get(uuid));
			// CCanvasController.canvasdb.get(cuid).getCamera().repaint();
		}

		// Add to the GroupDB
		try {
			CGroupController.groupdb.put(uuid, customScrap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CCanvasController.canvasdb.get(cuid).addChildGroup(uuid);
		// CCanvasController.canvasdb.get(cuid).getLayer().addChild(groupdb.get(uuid));
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(cuid)
				.getLayer(), CGroupController.groupdb.get(uuid));
		CGroupController.groupdb.get(uuid).drawPermTemp(true);
		// CCanvasController.canvasdb.get(cuid).repaint();
	}

	/**
	 * Add the points defined in p to the scrap with id uuid
	 *  
	 * @param uuid 	long
	 * @param p 	Polygon
	 */
	public static void create_custom_shape(long uuid, Polygon p) {
		for (int i = 0; i < p.npoints; i++) {
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
		}
	}
	
	/**
	 * Create a custom `label` 
	 * 
	 * @param uuid
	 * @param cuuid
	 * @param group
	 * @param p
	 * @param optText
	 */
	public static void no_notify_create_custom_label_bootstrap(long uuid,
			long cuuid, CGroup group, Polygon p, String optText) {
		no_notify_start(uuid, cuuid, 0l, true, group);
		CGroupController.setCurrentUUID(uuid);
		create_custom_shape(uuid, p);
		// Set the optional text to identify the scrap
		CGroupController.no_notify_set_text(uuid, optText);
		CGroupController.no_notify_finish(uuid, false, false, true);
		CGroupController.no_notify_set_permanent(uuid, true);
		CGroupController.recheck_parent(uuid);
	}

	public static long getPotentialWeighPoint(Point point, int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static AirspaceStroke create_airspace_stroke(long new_uuid, long currentStroke) {
		// TODO Auto-generated method stub
		
		//get the polygon from the stroke
		CStroke stroke = CStrokeController.strokes.get(currentStroke);
		Polygon p = stroke.getPolygon();
		long c_uuid = stroke.getCanvasUUID();
		long parent_uuid = stroke.getParentUUID();
		Color color = stroke.getColor();
		float thickness = stroke.getThickness();
		double rotation = stroke.getRotation();
		
		//going to delete the stroke
		CStrokeController.delete(currentStroke);
		
		//going to create the airspace stroke
		initialize_airspace_stroke(new_uuid, c_uuid, parent_uuid, color, thickness, p, rotation, 1.0d, 1.0d);
		
		return null;
	}
	
	public static void initialize_airspace_stroke(long uuid, long cuid, long puid, Color color, float thickness, Polygon p, double rotation, double scaleX, double scaleY)
	{
//		CStrokeController.no_notify_start(uuid, cuid, puid, color, thickness);
		// -- STROKE START -- //
		
		AirspaceStroke aStroke = new AirspaceStroke(uuid, cuid, puid, color, thickness);
		
		// does the element already exist?
		if(CStrokeController.exists(uuid))
		{
			CStrokeController.no_notify_delete(uuid);
		}
		
		// add to the DB
		CStrokeController.strokes.put(uuid, aStroke);
		
		// Add to the canvas
		CCanvasController.no_notify_add_child_stroke(cuid, uuid, true);
		
		if(puid!=0L)
		{
			// Ok, add to the group
			CGroupController.no_notify_add_child_stroke(puid, uuid);
		}
		
		
		// -- END STROKE START -- //
		
		int[] x = p.xpoints;
		int[] y = p.ypoints;
		
		CStrokeController.no_notify_batch_append(uuid, x, y);
		
		CStrokeController.strokes.get(uuid).primative_rotate(rotation);
		CStrokeController.strokes.get(uuid).primative_scale(scaleX, scaleY);
		
		//CStrokeController.strokes.get(uuid).finish();
		
		CStrokeController.no_notify_finish(uuid);
	}

}
