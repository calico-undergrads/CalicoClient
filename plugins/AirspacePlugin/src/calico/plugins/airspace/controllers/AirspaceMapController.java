package calico.plugins.airspace.controllers;

import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import calico.CalicoDraw;
import calico.components.CGroup;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.controllers.CImageController;
import calico.plugins.airspace.components.AirspaceMap;
import calico.utils.Geometry;

public class AirspaceMapController {

	public static void doActionA(long elementUUID, long canvasUUID, int posX,
			int posY) {
		// create shape
		GeneralPath myPolygon = new GeneralPath(new Rectangle(posX, posY, 120, 60));
		Polygon p = Geometry.getPolyFromPath(myPolygon.getPathIterator(null));

		Image tempImage = null;
		try {
			tempImage = ImageIO.read(new File(CImageController.getImagePath(elementUUID)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (tempImage != null)
		{
			// initialize custom scrap
			CGroup group = new AirspaceMap(elementUUID, canvasUUID, tempImage);
	
			// create the scrap
			no_notify_create_custom_scrap_bootstrap(elementUUID, canvasUUID, group, p, "Action A");
		}
	}

	/*************************************************
	 * UTILITY METHODS - Taken from Example Plugin
	 *************************************************/
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

	// Starts the creation of any of the activity diagram scrap
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

	// Add the points defined in p to the scrap with id uuid
	public static void create_custom_shape(long uuid, Polygon p) {
		for (int i = 0; i < p.npoints; i++) {
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
		}
	}

}
