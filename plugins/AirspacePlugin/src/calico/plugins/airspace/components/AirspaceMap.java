package calico.plugins.airspace.components;


import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import calico.components.CGroup;
import calico.components.CGroupImage;
import calico.controllers.CGroupController;
import calico.inputhandlers.CalicoInputManager;
import calico.networking.netstuff.CalicoPacket;
import calico.plugins.airspace.AirspacePluginNetworkCommands;
import calico.plugins.airspace.inputhandlers.AirspaceInputHandler;
//import com.javadocmd.simplelatlng.LatLng;


public class AirspaceMap extends CGroupImage {

	private static final long serialVersionUID = 1L;
//	private LatLng point1 = new LatLng(33.648315,-117.847466);
//	private LatLng point2 = new LatLng(33.648315,-117.835965);
//	private LatLng point3 = new LatLng(33.642348, -117.835965);
//	private LatLng point4 = new LatLng(33.642348, -117.847466);
	
	private double latOrigin;
	private double longOrigin;
	private double width;
	private double height;
	private ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
	
	/**
	 * UI Map Element
	 * 
	 * @param uuid
	 * @param cuid
	 * @param image
	 * @param latOrigin
	 * @param longOrigin
	 * @param width
	 * @param height
	 */
	public AirspaceMap(long uuid, long cuid, Image image, double latOrigin, double longOrigin, double width, double height)
	{
		super(uuid,cuid,image);
		networkLoadCommand = AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD;
		this.latOrigin = latOrigin;
		this.longOrigin = longOrigin;
		this.width = width;
		this.height = height;
		
	}
	
	
	
	/**
	 * Sets `AirspaceInputHandler` for the Airspace map object.
	 */
	@Override
	public void setInputHandler()
	{	
		CalicoInputManager.addCustomInputHandler(this.uuid, new AirspaceInputHandler(uuid));	
	}	
	
	/**
	 * Returns Longitude & Latitude Coordinates from the map.
	 * 
	 * @param x
	 * @param y
	 * @return Point2D Coordinates
	 */
	public Point2D.Double getPointOnMap(int x, int y){
		Rectangle bounds = CGroupController.groupdb.get(uuid).getPathReference().getBounds();
		double deltaX = x - bounds.x;
		double deltaY = y - bounds.y;
		double ratioX = deltaX / bounds.width;
		double ratioY = deltaY / bounds.height;
		
		double latitude = (this.width * ratioX) + this.longOrigin;
		double longitude = (this.height * ratioY) + this.latOrigin;
		Point2D.Double output = new Point2D.Double(latitude, longitude);
//		System.out.println(output);
		return output;
	}
	
	public void addPoint(int x, int y){
		points.add(getPointOnMap(x, y));
		System.out.println(getPointOnMap(x,y));
	}
	
	public double getDistance(){
		double distance = 0;
		for(int i=0;i<points.size()-1;i++){
			distance += calculateDistance(points.get(i), points.get(i));
		}
		return distance;
	}
	
	public double calculateDistance(Point2D.Double start, Point2D.Double end){
		final int R = 6371;
		Double lat1 = start.x;
		Double lat2 = end.x;
		Double lon1 = start.y;
		Double lon2 = end.y;
		System.out.print("Calculating distance with"+lat1+","+lat2+","+lon1+","+lon2);
		Double latDistance = toRad(lat2-lat1);
		Double lonDistance = toRad(lon2-lon1);
		Double a = Math.sin(latDistance /2) * Math.sin(latDistance /2) +
				Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
				Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = R * c;
		System.out.println(distance);
		return distance;
	}
	
	private static Double toRad(Double value){
		return value * Math.PI / 180;
	}
	
	/**
	 * Serialize this activity node in a packet
	 * 
	 * @param uuid
	 * @param cuid
	 * @param puid
	 * @param dx
	 * @param dy
	 * @param captureChildren
	 */
	@Override
	public CalicoPacket[] getUpdatePackets(long uuid, long cuid, long puid, int dx, int dy, boolean captureChildren) {
		
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
