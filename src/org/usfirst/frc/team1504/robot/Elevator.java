package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.PIDController;

//import edu.wpi.first.wpilibj.JoystickButton;

public class Elevator extends Loggable { // thread
	DigitalInput hallSensor;
	DoubleSolenoid elevatorSolenoid;
	Servo servo_1;
	Servo servo_2;
	CANTalon elevatorMotor;
	ElevatorThreadClass elevator = new ElevatorThreadClass();
	PIDController elevatorPID;
	Counter hallCounter;
	//HallHandlerClass handler;
	int setPoint;
	boolean[] button;
	boolean servo_1State;
	boolean servo_2State;
	public Elevator() {
		// handler = new HallHandlerClass();
		hallSensor = new DigitalInput(Map.ELEVATOR_DIGITAL_INPUT_PORT);
		hallCounter = new Counter(hallSensor);
		elevatorSolenoid = new DoubleSolenoid(Map.ELEVATOR_SOLENOID_FORWARD_PORT, Map.ELEVATOR_SOLENOID_REVERSE_PORT);
		hallCounter.setUpDownCounterMode();
		hallCounter.setUpSource(hallSensor);// /// may neeed to goooo
		// befoooore.
		servo_1 = new Servo(Map.ELEVATOR_SERVO_ONE_PORT);
		servo_2 = new Servo(Map.ELEVATOR_SERVO_TWO_PORT);
		elevatorMotor = new CANTalon(Map.ELEVATOR_TALON_PORT);
		// hallSensor.enableInterrupts();
		// hallSensor.requestInterrupts(handler);
		setPoint = 0;
	}

	public double[] dump() {
		double[] dump = new double[0]; // change this l8er.
		return dump;
	}

/*	public class HallHandlerClass extends InterruptHandlerFunction<Double> {
		public void interruptFired(int i, Double param) {
			return;
		}

		Double overrideableParameter() {
			Double hi = new Double(2.2);
			return hi;
		}
	}
*/
	public void start() {
		elevator.start();
	}

	public void useSetPoint() 
	{	
		if(setPoint < hallCounter.get())
		{
			elevatorMotor.set(-1);
		}
		
		else if(setPoint > hallCounter.get())
		{
			elevatorMotor.set(1);
		}
		
		else if(setPoint == hallCounter.get())
			elevatorMotor.set(0);
	}
	
	public void setServos()
	{
		boolean[] servos = new boolean[2];
		servos = IO.bincapture_input();
		if(servo_1State && servo_2State)
		{
			servo_1.setAngle(90); //change later
			servo_2.setAngle(90); 
		}
		
		if(servos[0] || servos[1])
		{
			servo_1.setAngle(0); //change later
			servo_2.setAngle(0); //change later
		}
		
		
	}

	private class ElevatorThreadClass extends Thread {
		protected boolean isRunning = true;
		protected boolean isManual = true;

		public void run() {
			while (isRunning) {
				
				isManual = IO.elevator_manual_toggle();
				if (isManual) {
					manual(IO.elevator_manual());
				} else {
					button = IO.buttonValues();
					if (button[0]){setPoint = 1;} 
					else if (button[1]){setPoint = 2;}
					else if (button[2]){setPoint = 3;}
					else if (button[3]){setPoint = 4;}
					else if (button[4]){setPoint = 5;}
					else if (button[5]){setPoint = 6;}
					else if (button[6]){setPoint = 7;}
					else if (button[7]){setPoint = 8;}
					else if (button[8]){setPoint = 9;}
					else if (button[9]){setPoint = 10;}
					useSetPoint();
					if(IO.elevator_mode() == 0)
					{
						//solenoid retracted, servos up
						elevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
						servo_1.setAngle(0);
						servo_2.setAngle(0);
					}
					
					else if(IO.elevator_mode() == 1)
					{
						//soleoid exteded, servos down
						elevatorSolenoid.set(DoubleSolenoid.Value.kForward);
						servo_1.setAngle(0);
						servo_2.setAngle(0);
						
					}
					
					else if(IO.elevator_mode() == 2)
					{
						//soleoid extended, servos up
						elevatorSolenoid.set(DoubleSolenoid.Value.kForward);
						servo_1.setAngle(0);
						servo_2.setAngle(0);
					}
					// PID();
					manual(0);
				}
			}
		}

		private void manual(double y) {
			elevatorMotor.set(y);
		}

		public void stopElevator() {
			isRunning = false;
		}

	}

}
