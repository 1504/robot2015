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
	boolean moveToggle;
	int setPoint;
	boolean[] button;
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

	public void setSetPoint() {

		

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
					if (button[0] && !moveToggle) {
						moveToggle = true;
						setPoint++;
					} else if (button[1] && !moveToggle) {
						moveToggle = true;
						setPoint--;
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
