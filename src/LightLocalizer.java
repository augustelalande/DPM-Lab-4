import lejos.nxt.*;

public class LightLocalizer {
	private Odometer odo;
	private TwoWheeledRobot robot;
	private ColorSensor cs;
 	private Navigation navigation;
	
	private int DEFAULT_FOWARD_SPEED = 40;
	public static double MAX_LINE_VALUE = 300;
	public static double DISTANCE_FROM_ROBOT_CENTER_TO_COLORSENSOR = 12.5;
	
	public LightLocalizer(Odometer odo, ColorSensor cs) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.cs = cs;
		this.navigation = odo.getNavigation();
		
		// turn on the light
		cs.setFloodlight(true);
	}
	
	public void doLocalization() {
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		// do trig to compute (0,0) and 0 degrees
		// when done travel to (0,0) and turn to 0 degrees
		
		//set Y
		navigation.turnTo(0);
//		robot.setForwardSpeed(DEFAULT_FOWARD_SPEED);
//		
//		while(!isLineDetected()){
//		}
//		
//		robot.stop();
//		double newY = 0 + DISTANCE_FROM_ROBOT_CENTER_TO_COLORSENSOR;
//		odo.setPosition(new double [] {0.0, newY, 0.0}, new boolean [] {false, true, false});
//		
//		///set X
//		navigation.turnTo(90);
//		robot.setForwardSpeed(DEFAULT_FOWARD_SPEED);
//		
//		while(!isLineDetected()){
//		}
//		
//		robot.stop();
//		double newX = 0 + DISTANCE_FROM_ROBOT_CENTER_TO_COLORSENSOR;
//		odo.setPosition(new double [] {newX, 0.0, 0.0}, new boolean [] {true, false, false});
//		
//		//travel to (0,0,0)
//		navigation.travelTo(0, 0);
//		navigation.turnTo(0);
	}
	
	public boolean isLineDetected(){
		if(cs.getNormalizedLightValue() <  MAX_LINE_VALUE)
			return true;
		
		return false;
	}
	

}
