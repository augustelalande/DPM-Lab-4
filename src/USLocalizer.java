import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	
	public static int MAX_WALL_DISTANCE = 30;
	public static int DEFAULT_ROTATION_SPEED = 100;

	private Odometer odo;
	private UltrasonicSensor us;
	private Navigator nav;
	
	public USLocalizer(Odometer odo, UltrasonicSensor us, Navigator nav) {
		this.odo = odo;
		this.us = us;
		this.nav = nav;
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	public void doLocalization(LocalizationType locType) {
		
		double angleA, angleB;
		
		if (locType == LocalizationType.FALLING_EDGE) {

			// rotate the robot until it sees no wall
			nav.rotate(DEFAULT_ROTATION_SPEED, Navigator.RotationType.ClockWise);
			
			while(isWallDetected()){
			}
			
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
			
			// keep rotating until the robot sees a wall, then latch the angle
			while(!isWallDetected()){
			}
			
			nav.stop();
			
			angleA = odo.getTheta();

			// switch direction and wait until it sees no wall
			nav.rotate(DEFAULT_ROTATION_SPEED, Navigator.RotationType.CounterClockWise);
			
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
			
			while(isWallDetected()){
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while(!isWallDetected()){
			}
			
			nav.stop();
			
			angleB = odo.getTheta();

			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'

			double orientation = findOrientation(angleA, angleB);

			// update the odometer position (example to follow:)
			odo.setTheta(orientation);
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//
			// FILL THIS IN
			//
		}
	}
	
	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
		
		// filter out large values
		if (distance > 50)
			distance = 50;
				
		return distance;
	}
	
	private boolean isWallDetected() 
	{
		if(getFilteredData() < MAX_WALL_DISTANCE)
			return true;
			
		return false;
	}
	
	public double findOrientation(double angleA, double angleB)
	{
		double deltaAngle = angleB - angleA;
		
		double orientation = (Math.PI/4) + (deltaAngle/2);
		
		return orientation;
	}
}
