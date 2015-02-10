import lejos.nxt.*;

public class Lab4 {
	public static void main(String[] args)
	{
		int option;

		final double leftWheelRadius = 2.11;
		final double rightWheelRadius = 2.11;
		final double wheelSeperation = 15.41;
		final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B;
		ColorSensor cs = new ColorSensor(SensorPort.S1);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S2);

		//class declarations
		Odometer odo = new Odometer(leftWheelRadius, rightWheelRadius, wheelSeperation, leftMotor, rightMotor);
		OdometryDisplay display = new OdometryDisplay(odo);
		Navigator nav = new Navigator(leftWheelRadius, rightWheelRadius, 
				wheelSeperation, leftMotor, rightMotor, odo);
		
		USLocalizer usl = new USLocalizer(odo, us, nav);;
		LightLocalizer lsl = new LightLocalizer(odo, cs, nav);

		//clear the display
		LCD.clear();

		// give user options
		LCD.drawString("<---   |    --->", 0, 0);
		LCD.drawString("       |        ", 0, 1);
		LCD.drawString("Falling| Rising ", 0, 2);
		LCD.drawString(" Edge  |  Edge  ", 0, 3);
		LCD.drawString("       |        ", 0, 4);
		LCD.drawString("<---   |    --->", 0, 5);

		option = Button.waitForAnyPress();

			switch(option) {
			case Button.ID_LEFT:
				
				odo.start();
				display.start();
				
				usl.doLocalization(USLocalizer.LocalizationType.FALLING_EDGE);
				
				lsl.doLocalization();
				
				break;
			case Button.ID_RIGHT:
				
				odo.start();
				display.start();
			
				usl.doLocalization(USLocalizer.LocalizationType.RISING_EDGE);
				
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
