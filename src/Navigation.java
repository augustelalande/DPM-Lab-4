import lejos.nxt.*;

public class Navigation {
	// put your navigation code here 
	
	public static double DEFAULT_ROTATION_SPEED = 50;
	private int DEFAULT_FOWARD_SPEED = 100;
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
	}
	
	public Navigation(Odometer odo, TwoWheeledRobot robot) {
		this.odo = odo;
		this.robot = robot;
	}
	
	public void travelTo(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
	}
	
	public void turnTo(double angle) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
		robot.setRotationSpeed(DEFAULT_ROTATION_SPEED);
		while(odo.getTheta() != angle){}
		
		robot.stop();
	}
}
