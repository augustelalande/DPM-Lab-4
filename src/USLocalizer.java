import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 30;
	public static int MAX_WALL_DISTANCE = 20;
	public static double DEFAULT_ROTATION_SPEED = 50;

	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	
	public USLocalizer(Odometer odo, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.locType = locType;
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		
		if (locType == LocalizationType.FALLING_EDGE) {

			// rotate the robot until it sees no wall
			robot.setRotationSpeed(DEFAULT_ROTATION_SPEED);
			
			while(isWallDetected()){
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while(!isWallDetected()){
			}
			
			robot.stop();
			odo.getPosition(pos);
			angleA = pos[2];

			// switch direction and wait until it sees no wall
			robot.setRotationSpeed(-DEFAULT_ROTATION_SPEED);
			
			while(isWallDetected()){
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while(!isWallDetected()){
			}
			
			robot.stop();
			odo.getPosition(pos);
			angleB = pos[2];

			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'

			double deltaAngle = Odometer.minimumAngleFromTo(angleA, angleB);
			double orientation = 0;
			
			if(angleA < angleB)
			{
				orientation = 45 - deltaAngle/2;
			}
			else
			{
				orientation = 225 - deltaAngle/2;
			}

			orientation = Odometer.fixDegAngle(orientation);

			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, orientation}, new boolean [] {false, false, true});
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
				
		return distance;
	}
	
	private boolean isWallDetected(){
		if(getFilteredData() < MAX_WALL_DISTANCE)
			return true;
			
		return false;
	}

}
