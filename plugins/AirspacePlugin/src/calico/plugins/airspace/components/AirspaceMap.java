package calico.plugins.airspace.components;


import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.vecmath.Point3d;

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
	private int zoomLevel;
	//Should weigh points go into an extension of 

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
	public AirspaceMap(long uuid, long cuid, Image image, double latOrigin, double longOrigin, int zoomLevel)
	{
		super(uuid,cuid,image);
		networkLoadCommand = AirspacePluginNetworkCommands.AIRSPACEPLUGIN_MAP_LOAD;
		this.latOrigin = latOrigin;
		this.longOrigin = longOrigin;
		//Calculate width and height based off zoom level
		this.zoomLevel = zoomLevel;
		calculateDimensions();
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
	
	public void calculateDimensions(){
		//It is possible to call the javascript Google Maps v3 api to get map bounds.... 
		//Could be much simpler than actually calculating dimensions (haversine formula, etc)
	}

	/* 
	 * 
	 * Moved to AirspaceStroke
	 * 
	 */
	
//	public void addPoint(int x, int y){
//		points.add(getPointOnMap(x, y));
//		System.out.println(getPointOnMap(x,y));
//	}
//	
//	public void addWeightPoint(int x, int y, int z){
//		weighPoints.add(new Point3d(x,y,z));
//	}
//	
//	public boolean intersection(){
//		if(true){
//			return true;
//		}
//		return false;
//	}
//	
//	public double getDistance(){
//		double distance = 0;
//		for(int i=0;i<points.size()-2;i++){
//			distance += calculateDistance(points.get(i), points.get(i+1));
//		}
//		return distance;
//	}
//	
//	public double calculateDistance(Point2D.Double start, Point2D.Double end){
//		final int R = 6371;
//		double lat1 = start.getX();
//		double lat2 = end.getX();
//		double lon1 = start.getY();
//		double lon2 = end.getY();
//		System.out.print("Calculating distance with"+lat1+","+lat2+","+lon1+","+lon2);
//		double latDistance = toRad(lat2-lat1);
//		double lonDistance = toRad(lon2-lon1);
//		double a = Math.sin(latDistance /2d) * Math.sin(latDistance /2d) +
//				Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
//				Math.sin(lonDistance / 2d) * Math.sin(lonDistance / 2d);
//		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//		double distance = R * c;
//		System.out.println(distance);
//		return distance;
//	}
//	
//	private static double toRad(double valueInDegrees){
//		return valueInDegrees * Math.PI / 180;
//	}
//	
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
