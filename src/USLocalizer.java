import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	
	public static int MAX_WALL_DISTANCE = 30;
	public static int DEFAULT_ROTATION_SPEED = 100;
	public static int THRESHOLD_SENSOR_DISTANCE = 50;

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

			double orientation = findOrientation(angleA, angleB, locType);
			odo.setTheta(orientation);
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			// rotate the robot until it sees a wall
			nav.rotate(DEFAULT_ROTATION_SPEED, Navigator.RotationType.ClockWise);

			while(!isWallDetected()){
			}

			try { Thread.sleep(1000); } catch (InterruptedException e) {}

			// keep rotating until the robot sees no wall, then latch the angle
			while(isWallDetected()){
			}

			nav.stop();

			angleA = odo.getTheta();

			// switch direction and wait until it sees a wall
			nav.rotate(DEFAULT_ROTATION_SPEED, Navigator.RotationType.CounterClockWise);

			try { Thread.sleep(1000); } catch (InterruptedException e) {}

			while(!isWallDetected()){
			}
			// keep rotating until the robot sees no wall, then latch the angle
			while(isWallDetected()){
			}

			nav.stop();
			angleB = odo.getTheta();

			double orientation = findOrientation(angleA, angleB, locType);
			odo.setTheta(orientation);
		}
	}
	
	private int getFilteredData() {
		int distance;
		
		us.ping();
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		distance = us.getDistance();
		
		if (distance > THRESHOLD_SENSOR_DISTANCE)
			distance = THRESHOLD_SENSOR_DISTANCE;
				
		return distance;
	}
	
	private boolean isWallDetected() 
	{
		if(getFilteredData() < MAX_WALL_DISTANCE)
			return true;
			
		return false;
	}
	
	public double findOrientation(double angleA, double angleB, LocalizationType locType)
	{
		double deltaAngle = angleB - angleA;
		
		double orientation;
		
		if(locType == LocalizationType.FALLING_EDGE) {
			orientation = (Math.PI/4) + (deltaAngle/2);
		}
		else {
			orientation = (5*Math.PI/4)+ (deltaAngle/2);
		}
		
		return orientation;
	}
}
