import lejos.nxt.*;

public class LightLocalizer {
	private Odometer odo;
	private ColorSensor cs;
 	private Navigator nav;
	
	public static double MAX_LINE_VALUE = 300;
	public static double DISTANCE_FROM_ROBOT_CENTER_TO_COLORSENSOR = 12.5;
	public static double APPROXIMATE_DISTANCE_TO_ORIGIN = 12;
	
	public LightLocalizer(Odometer odo, ColorSensor cs, Navigator nav) {
		this.odo = odo;
		this.cs = cs;
		this.nav = nav;
		
		// turn on the light
		cs.setFloodlight(true);
	}
	
	public void doLocalization() {
	
		
		double[] spinAngles = new double[4];
		
		
		//turn toward (0,0)
		nav.turnTo(45, false);
		nav.travelDistance(APPROXIMATE_DISTANCE_TO_ORIGIN);
		spinOnItself(spinAngles);
		
		double xPos = calculatePos(spinAngles[0], spinAngles[2]);
		double yPos = calculatePos(spinAngles[1], spinAngles[3]);
		
		odo.setX(xPos);
		odo.setY(yPos);
		
		nav.travelTo(0, 0);
		nav.turnTo(90, false);
		
	}
	
	public boolean isLineDetected(){
		if(cs.getNormalizedLightValue() <  MAX_LINE_VALUE)
			return true;
		
		return false;
	}
	
	public void spinOnItself(double[] angles){
		int angleCount = 0;
		
		(new Thread() {public void run() {nav.turnAngle(2*Math.PI);}}).start();
		try {Thread.sleep(50);} catch (Exception e) {}
		
		while(nav.isNavigating() && angleCount < 4){
			
			if(isLineDetected()){
				angles[angleCount] = odo.getTheta();
				angleCount++;
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
//	public boolean isInRange(double angle, double error)//in degrees
//	{
//		if(odo.getTheta() > Math.toRadians(angle - error) || odo.getTheta() < Math.toRadians(angle + error)){
//			return true;
//		}
//			
//		return false;
//	}
}
