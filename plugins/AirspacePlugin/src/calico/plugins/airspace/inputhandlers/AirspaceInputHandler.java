package calico.plugins.airspace.inputhandlers;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import calico.Calico;
import calico.CalicoDraw;
import calico.CalicoOptions;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.controllers.CStrokeController;
import calico.inputhandlers.CGroupInputHandler;
import calico.inputhandlers.CalicoAbstractInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.inputhandlers.PressAndHoldAction;
import calico.plugins.airspace.components.AirspaceMap;
import calico.plugins.airspace.components.AirspaceStroke;
import calico.plugins.airspace.controllers.AirspaceMapController;
import calico.utils.Ticker;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

public class AirspaceInputHandler extends CGroupInputHandler implements PressAndHoldAction{
	long auuid;
	PText tNode;

	public AirspaceInputHandler(long uuid) {
		super(uuid);
		this.auuid = uuid;
	}
	public Point mouseDown;
	private boolean hasBeenPressed = false;
	private boolean hasStartedBge = false;
	CalicoAbstractInputHandler.MenuTimer menuTimer;


	public void actionPressed(InputEventInfo e)
	{
	super.actionPressed(e);
	
	hasBeenPressed = true;
	long uuid = 0l;
	
	if(e.isLeftButton())
	{
		int x = e.getX();
		int y = e.getY();
		uuid = Calico.uuid();
		CStrokeController.setCurrentUUID(uuid);
		CStrokeController.start(uuid, canvas_uid, 0L);
		CStrokeController.append(uuid, x, y);
		hasStartedBge  = true;
		mouseDown = e.getPoint();
		
		long potentialConnector;
		if ((potentialConnector = AirspaceMapController.getPotentialWeighPoint(e.getPoint(), 20)) > 0l)
		{
			PLayer layer = CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer();
			menuTimer = new CalicoAbstractInputHandler.MenuTimer(this, uuid, CalicoOptions.core.hold_time/2, CalicoOptions.core.max_hold_distance, CalicoOptions.core.hold_time, e.getPoint(), potentialConnector, layer);
			Ticker.scheduleIn(CalicoOptions.core.hold_time, menuTimer);
		}
		else if (CStrokeController.getPotentialScrap(e.getPoint()) > 0l)
		{
			PLayer layer = CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer();
			menuTimer = new CalicoAbstractInputHandler.MenuTimer(this, uuid, CalicoOptions.core.hold_time/2, CalicoOptions.core.max_hold_distance, CalicoOptions.core.hold_time, e.getPoint(), e.group, layer);
			Ticker.scheduleIn(CalicoOptions.core.hold_time, menuTimer);
		}
//		menuThread = new DisplayMenuThread(this, e.getGlobalPoint(), e.group);		
//		Ticker.scheduleIn(CalicoOptions.core.hold_time, menuThread);
	}
		
	
	}


	@Override
	public void actionReleased(InputEventInfo ev) {
		// TODO Auto-generated method stub
		super.actionReleased(ev);
		AirspaceMap map = (AirspaceMap)CGroupController.groupdb.get(this.auuid);
		long currentStroke = CStrokeController.getCurrentUUID();
//		AirspaceStroke stroke = (AirspaceStroke)CGroupController.groupdb.get(this.auuid);
		AirspaceStroke stroke = AirspaceMapController.create_airspace_stroke(Calico.uuid(), currentStroke);
		System.out.println(stroke.getDistance());
		
		final PLayer n = CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer();
		

//			CalicoDraw.removeChildFromNode(map, map.removeChild(tNode));
		final PNode old = tNode;
		tNode = new PText(  ((double)((int)(stroke.getDistance() * 100)))/100 + " kilometers");
		int tX, tY;
		Rectangle tR = map.getBounds().getBounds().getBounds();
		tX = tR.x;
		tY = tR.y + tR.height + /*height of text pnode*/ new Double(tNode.getHeight()).intValue();
		tNode.setOffset(tX, tY);
//		tNode.setText(map.getDistance() + " kilometers");
//		CalicoDraw.addChildToNode(map, tNode);
		
		SwingUtilities.invokeLater(
				new Runnable() { public void run() { 
					if (tNode != old)
						n.removeChild(old);
					n.addChild(tNode);
					
				}});
		
		
		CalicoDraw.repaint(tNode);
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


	@Override
	public long getLastAction() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Point getMouseDown() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Point getMouseUp() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Point getLastPoint() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public double getDraggedDistance() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void pressAndHoldCompleted() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pressAndHoldAbortedEarly() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void openMenu(long potScrap, long group, Point point) {
		CalicoAbstractInputHandler.clickMenu(potScrap, group, point);		
	}

}