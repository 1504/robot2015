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
	ElevatorThreadClass elevator;
	PIDController elevatorPID;
	Counter hallCounter;
	// HallHandlerClass handler;
	int setPoint;
	boolean[] button;
	boolean servo_1State;
	boolean servo_2State;
	protected boolean isManual;

	int loopcount;
	long starttime;
	
	public Elevator() {
		elevator = new ElevatorThreadClass();
		
		// handler = new HallHandlerClass();
		hallSensor = new DigitalInput(Map.ELEVATOR_DIGITAL_INPUT_PORT);
		hallCounter = new Counter(hallSensor);
		elevatorSolenoid = new DoubleSolenoid(Map.ELEVATOR_SOLENOID_FORWARD_PORT, Map.ELEVATOR_SOLENOID_REVERSE_PORT);
		hallCounter.setUpDownCounterMode();
		hallCounter.setUpSource(hallSensor);// /// may neeed to goooo
		// befoooore.
		servo_1 = new Servo(Map.ELEVATOR_SERVO_LEFT_PORT);
		servo_2 = new Servo(Map.ELEVATOR_SERVO_RIGHT_PORT);
		elevatorMotor = new CANTalon(Map.ELEVATOR_TALON_PORT);
		// hallSensor.enableInterrupts();
		// hallSensor.requestInterrupts(handler);
		setPoint = 0;
		isManual = true;
		loopcount = 0;
	}

	/*
	 * public class HallHandlerClass extends InterruptHandlerFunction<Double> {
	 * public void interruptFired(int i, Double param) { return; }
	 * 
	 * Double overrideableParameter() { Double hi = new Double(2.2); return hi;
	 * } }
	 */
	public void start() {
		elevator.start();
	}

//	public void useSetPoint() {
//		if (setPoint < hallCounter.get()) {
//			elevatorMotor.set(Map.ELEVATOR_DOWN_SPEED); // -1
//		}
//
//		else if (setPoint > hallCounter.get()) {
//			elevatorMotor.set(Map.ELEVATOR_UP_SPEED); // 1
//		}
//
//		else if (setPoint == hallCounter.get())
//			elevatorMotor.set(Map.ELEVATOR_NONE_SPEED); // 0
//	}

	private class ElevatorThreadClass extends Thread {
		protected boolean isRunning = true;
		boolean[] button;

		protected boolean checkButtons() {
			for (boolean ooo : IO.elevatorButtonValues()) {
				if (ooo) {
					return true;
				}
			}
			return false;
		}

		public void run() {
			starttime = System.currentTimeMillis();
			while (isRunning) {
				if (loopcount == 0)
				{
					starttime = System.currentTimeMillis();
				}
				loopcount++;
				isManual = IO.elevator_manual_toggle() || (isManual && !checkButtons());
				if (isManual) {
					if (IO.elevator_manual_toggle()) {
						manual(IO.elevator_manual());
					} else {
						manual(0.0);
					}

				} else {
					button = IO.elevatorButtonValues();
					if (button[0]) {
						setPoint = 1;
					} else if (button[1]) {
						setPoint = 2;
					} else if (button[2]) {
						setPoint = 3;
					} else if (button[3]) {
						setPoint = 4;
					} else if (button[4]) {
						setPoint = 5;
					} else if (button[5]) {
						setPoint = 6;
					} else if (button[6]) {
						setPoint = 7;
					} else if (button[7]) {
						setPoint = 8;
					} else if (button[8]) {
						setPoint = 9;
					} else if (button[9]) {
						setPoint = 10;
					}
					//useSetPoint();
					if (IO.elevator_mode() == 0 && elevatorSolenoid.get() != DoubleSolenoid.Value.kReverse) { // Forks retracted
						// solenoid retracted, servos up
						if(servo_1.getAngle() != Map.ELEVATOR_SERVO_LEFT_OPEN_ANGLE || servo_2.getAngle() != Map.ELEVATOR_SERVO_RIGHT_OPEN_ANGLE)
						{
							servo_1.setAngle(Map.ELEVATOR_SERVO_LEFT_OPEN_ANGLE);
							servo_2.setAngle(Map.ELEVATOR_SERVO_RIGHT_OPEN_ANGLE);
							try {
								Thread.sleep(700);
							} catch (InterruptedException e) {}
						}
						elevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
					}

					else if (IO.elevator_mode() == 1) { // Tote pickup
						// solenoid exteded, servos down
						servo_1.setAngle(Map.ELEVATOR_SERVO_LEFT_DOWN_ANGLE);
						servo_2.setAngle(Map.ELEVATOR_SERVO_RIGHT_DOWN_ANGLE);
						
						elevatorSolenoid.set(DoubleSolenoid.Value.kForward);

					}

					else if (IO.elevator_mode() == 2) { // Bin pickup
						// soleoid extended, servos up
						servo_1.setAngle(Map.ELEVATOR_SERVO_LEFT_OPEN_ANGLE);
						servo_2.setAngle(Map.ELEVATOR_SERVO_RIGHT_OPEN_ANGLE);
						elevatorSolenoid.set(DoubleSolenoid.Value.kForward);
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

	public int get_elevator_level()
	{
		return hallCounter.get();
	}

	public double[] dump()
	{
		double[] vals = new double[11];
		vals[0] = Utils.boolconverter(isManual); //0 is false, 1 is true
		vals[1] = IO.elevatorMode; //0 is retracted mode, 1 is tote mode, 2 is bin mode
		vals[2] = setPoint; //desired lvl
		vals[3] = hallCounter.get(); //current lvl
		vals[4] = elevatorMotor.getSpeed();
		vals[5] = elevatorMotor.getOutputCurrent();
		vals[6] = elevatorMotor.getOutputVoltage();
		vals[7] = servo_1.getAngle();
		vals[8] = servo_2.getAngle();
		vals[9] = loopcount;
		vals[10] = System.currentTimeMillis() - starttime;
		loopcount = 0;
		return vals;
	}

}
