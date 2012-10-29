package calico.plugins.airspace.inputhandlers;

import java.awt.Point;
import java.awt.geom.Point2D;

import calico.Calico;
import calico.CalicoOptions;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.controllers.CStrokeController;
import calico.inputhandlers.CGroupInputHandler;
import calico.inputhandlers.CalicoAbstractInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.airspace.components.AirspaceMap;
import calico.utils.Ticker;
import edu.umd.cs.piccolo.PLayer;

public class AirspaceInputHandler extends CGroupInputHandler{
	long auuid;

	public AirspaceInputHandler(long uuid) {
		super(uuid);
		this.auuid = uuid;
	}
	public Point mouseDown;
	private boolean hasBeenPressed = false;


	public void actionPressed(InputEventInfo e)
	{
	super.actionPressed(e);
		
	
	}


	@Override
	public void actionReleased(InputEventInfo ev) {
		// TODO Auto-generated method stub
		super.actionReleased(ev);
		AirspaceMap map = (AirspaceMap)CGroupController.groupdb.get(this.auuid);
		System.out.println(map.getDistance());
	}


	@Override
	public void actionDragged(InputEventInfo ev) {
		// TODO Auto-generated method stub
		super.actionDragged(ev);
		
		AirspaceMap map = (AirspaceMap)CGroupController.groupdb.get(this.auuid);
		map.addPoint(ev.getX(), ev.getY());
		
//		Point2D.Double point = map.getPointOnMap(ev.getX(), ev.getY());
//		System.out.println("X:  "+point.x+",Y:   "+point.y);
	}

}