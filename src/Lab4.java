import lejos.nxt.*;

public class Lab4 {

	public static void main(String[] args) {
		
		int option = 0;
		
		// setup the odometer, display, and ultrasonic and light sensors
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		LCDInfo lcd = new LCDInfo(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S2);
		ColorSensor ls = new ColorSensor(SensorPort.S1);
		
		USLocalizer usl;
	
		LightLocalizer lsl = new LightLocalizer(odo, ls);			
		
		//clear the display
		LCD.clear();

		// give user options
		LCD.drawString("<---   |    --->", 0, 0);
		LCD.drawString("FALLING| RISING ", 0, 1);
		LCD.drawString(" EDGE  |  EDGE  ", 0, 2);
		LCD.drawString("       |        ", 0, 3);
		LCD.drawString("<---   |    --->", 0, 4);

		option = Button.waitForAnyPress();
		(new Thread() {public void run() {Button.waitForAnyPress();System.exit(0);}}).start();

		switch(option) {
		case Button.ID_LEFT:
			
			usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
			
			usl.doLocalization();
			lsl.doLocalization();

			break;
		case Button.ID_RIGHT:
			
			usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.RISING_EDGE);

			usl.doLocalization();
			lsl.doLocalization();

			break;
		default:
			System.exit(0);
			break;
		}

		Button.waitForAnyPress();
		System.exit(0);
	}

}
