import lejos.nxt.*;

public class Navigator {
	
	public enum RotationType { ClockWise, CounterClockWise };
	
	private static final int FORWARD_SPEED = 150;
	private static final int ROTATE_SPEED = 100;
	
	boolean isTravelling = false;
	boolean isTurning = false;
	
	private double leftWheelRadius, rightWheelRadius, wheelSeperation;
	public final NXTRegulatedMotor leftMotor, rightMotor;
	private Odometer odometer;
	
	public Navigator(double leftWheelRadius, double rightWheelRadius, double wheelSeperation,
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, Odometer odometer)
	{
		this.leftWheelRadius = leftWheelRadius;
		this.rightWheelRadius = rightWheelRadius;
		this.wheelSeperation = wheelSeperation;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.odometer = odometer;
		
		this.leftMotor.setAcceleration(3000);
		this.rightMotor.setAcceleration(3000);
	}
	
	public void stop()
	{
		leftMotor.stop();
		rightMotor.stop();
	}
	
	public void rotate(int speed, RotationType rotation )
	{
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		
		if(rotation == RotationType.ClockWise)
		{
			leftMotor.forward();
			rightMotor.backward();
		}
		else
		{
			leftMotor.backward();
			rightMotor.forward();
		}
	}
	
	public void forward(int speed)
	{
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		
		leftMotor.forward();
		rightMotor.forward();
	}
	
	public void travelTo(double x, double y)
	{	
		isTravelling = true;
		
		double currentX = odometer.getX();
		double currentY = odometer.getY();
		
		double deltaX = x - currentX;
		double deltaY = y - currentY;
		
		double distance = Math.sqrt(Math.pow(deltaX, 2)+ Math.pow(deltaY, 2));
		
		double theta = 0;
		
		
		//handle special cases
		if(deltaX == 0)//division by zero
		{
			if(deltaY>0)
			{
				theta = Math.PI/2;
			}
			else
			{
				theta = -Math.PI/2;
			}
		}
		else if(deltaX < 0)//negative delta x must return angle in quadrant 2 or 3
		{
			theta = Math.PI + Math.atan(deltaY/deltaX);
		}
		else//returns angle in quadrant 1 or 4
		{
			theta = Math.atan(deltaY/deltaX);
		}
		
		turnTo(theta, true);
		travelDistance(distance);
		
		isTravelling = false;
	}
	
	public void turnTo(double theta, boolean isRadians)//converts to radians if is degrees
	{	
		if(!isRadians)
			theta = Math.toRadians(theta);
		
		isTurning = true;
		
		double currentTheta = odometer.getTheta();
		double turnToAngle = theta - currentTheta;
		
		while(turnToAngle<-Math.PI || turnToAngle>Math.PI)
		{
			if(turnToAngle<-Math.PI)
				turnToAngle+=(2*Math.PI);
			else
				turnToAngle-=(2*Math.PI);
		}
		
		turnAngle(turnToAngle);
		
		isTurning = false;
	}
	
	public boolean isNavigating()
	{
		if(isTravelling || isTurning)
			return true;
		
		return false;
	}
	
	public void turnAngle(double angle)//radians
	{
		isTurning = true;
		
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(-convertAngle(leftWheelRadius, wheelSeperation, angle), true);
		rightMotor.rotate(convertAngle(rightWheelRadius, wheelSeperation, angle), false);
		
		isTurning = false;
	}
	
	public void travelDistance(double distance)//cm
	{
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);

		leftMotor.rotate(convertDistance(leftWheelRadius, distance), true);
		rightMotor.rotate(convertDistance(rightWheelRadius, distance), false);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) Math.round((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / (2*Math.PI));
	}

}