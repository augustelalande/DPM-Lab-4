import lejos.nxt.*;

public class LightLocalizer {
	private Odometer odo;
	private ColorSensor cs;
 	private Navigator nav;
	
 	private int DEFAULT_ROTATE_SPEED = 100;
	public static double MAX_LINE_VALUE = 300;
	public static double DISTANCE_FROM_ROBOT_CENTER_TO_COLORSENSOR = 12.5;
	
	public LightLocalizer(Odometer odo, ColorSensor cs, Navigator nav) {
		this.odo = odo;
		this.cs = cs;
		this.nav = nav;
		
		// turn on the light
		cs.setFloodlight(true);
	}
	
	public void doLocalization() {
		
		double[] angles = new double[4];
		
		
		nav.turnTo(45, false);
		nav.travelDistance(12);
		spin(angles);
		
		double xPos = calculatePos(angles[0], angles[2]);
		double yPos = calculatePos(angles[1], angles[3]);
		
		odo.setX(xPos);
		odo.setY(yPos);
		
		nav.travelTo(0, 0);
		nav.turnTo(90, false);
		
//		nav.rotate(DEFAULT_ROTATE_SPEED, Navigator.RotationType.CounterClockWise);
//		
//		while(!isLineDetected() && !isInRange(90, 20))
//		{
//			try {Thread.sleep(30);} catch (Exception e) {}
//		}
//		
//		nav.stop();
//		odo.setTheta(Math.PI/2);
	}
	
	public boolean isLineDetected(){
		if(cs.getNormalizedLightValue() <  MAX_LINE_VALUE)
			return true;
		
		return false;
	}
	
	public void spin(double[] angles){
		int i = 0;
		
		(new Thread() {public void run() {nav.turnAngle(2*Math.PI);}}).start();
		try {Thread.sleep(50);} catch (Exception e) {}
		
		while(nav.isNavigating() && i < 4){
			if(isLineDetected()){
				angles[i] = odo.getTheta();
				i++;
				try {Thread.sleep(300);} catch (Exception e) {}
			}
		}
	}
	
	public double calculatePos(double firstAngle, double secondAngle){
		return (-DISTANCE_FROM_ROBOT_CENTER_TO_COLORSENSOR*Math.cos((secondAngle-firstAngle)/2));
	}
	
//	public void allignWithY()
//	{
//		if(isInRange(90, 0.5))
//		{
//			return;
//		}
//		else if(odo.getTheta() < Math.PI/2)
//		{
//			nav.rotate(DEFAULT_ROTATE_SPEED, Navigator.RotationType.CounterClockWise);
//			
//			while(!isLineDetected() && !isInRange(90, 20))
//			{
//				try {Thread.sleep(30);} catch (Exception e) {}
//			}
//			
//			nav.stop();
//			odo.setTheta(Math.PI/2);
//		}
//		else
//		{
//			nav.rotate(DEFAULT_ROTATE_SPEED, Navigator.RotationType.ClockWise);
//			
//			while(!isLineDetected() && !isInRange(90, 20))
//			{
//				try {Thread.sleep(30);} catch (Exception e) {}
//			}
//			
//			nav.stop();
//			odo.setTheta(Math.PI/2);
//		}
//	}
//	
	public boolean isInRange(double angle, double error)//in degrees
	{
		if(odo.getTheta() > Math.toRadians(angle - error) || odo.getTheta() < Math.toRadians(angle + error)){
			return true;
		}
			
		return false;
	}
}
