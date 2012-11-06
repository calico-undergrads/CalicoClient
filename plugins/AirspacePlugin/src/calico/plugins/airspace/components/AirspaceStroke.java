package calico.plugins.airspace.components;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.vecmath.Point3d;

import calico.components.CStroke;

public class AirspaceStroke extends CStroke{
	
	private ArrayList<Point3d> weighPoints = new ArrayList<Point3d>();
	private ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AirspaceStroke(long uuid, long cuid, long puid, Color color,
			float thickness) {
		super(uuid, cuid, puid, color, thickness);
	}
	
	public AirspaceStroke(long u, long canvas, long puid){
		super(u, canvas, puid);
	}
	
	public void addWeighPoint(int x, int y, int z){
		weighPoints.add(new Point3d(x,y,z));
	}
	
	public void addPoint(int x, int y){
	points.add(getPointOnMap(x, y));
	System.out.println(getPointOnMap(x,y));
}

	public void addWeightPoint(int x, int y, int z){
		weighPoints.add(new Point3d(x,y,z));
	}
	
	public boolean intersection(){
		if(true){
			return true;
		}
		return false;
	}
	
	public double getDistance(){
		double distance = 0;
		for(int i=0;i<points.size()-2;i++){
			distance += calculateDistance(points.get(i), points.get(i+1));
		}
		return distance;
	}
	
	public double calculateDistance(Point2D.Double start, Point2D.Double end){
		final int R = 6371;
		double lat1 = start.getX();
		double lat2 = end.getX();
		double lon1 = start.getY();
		double lon2 = end.getY();
		System.out.print("Calculating distance with"+lat1+","+lat2+","+lon1+","+lon2);
		double latDistance = toRad(lat2-lat1);
		double lonDistance = toRad(lon2-lon1);
		double a = Math.sin(latDistance /2d) * Math.sin(latDistance /2d) +
				Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
				Math.sin(lonDistance / 2d) * Math.sin(lonDistance / 2d);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double distance = R * c;
		System.out.println(distance);
		return distance;
	}
	
	private static double toRad(double valueInDegrees){
		return valueInDegrees * Math.PI / 180;
	}

	
	@Override
	public int get_signature()
	{
		return 0;
	}

}
