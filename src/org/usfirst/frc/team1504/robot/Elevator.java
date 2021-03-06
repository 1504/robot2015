package org.usfirst.frc.team1504.robot;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;

//import edu.wpi.first.wpilibj.JoystickButton;

public class Elevator extends Loggable { // thread
	DriverStation ds = DriverStation.getInstance();

	DigitalInput hallSensor;
	DigitalInput limit;

	DoubleSolenoid elevatorSolenoid;
	Solenoid flapperSolenoid;
	
	// TODO: fix .ordinal() hack, through IO class
	public enum ForkMode {retracted, toteMode, binMode, retractedFinal, toteModeFinal, binModeFinal, NULL}

	Servo servo_1;
	Servo servo_2;
	CANTalon elevatorMotor;
	ElevatorThreadClass elevator;
	PIDController elevatorPID;
	Counter hallCounter;
	// HallHandlerClass handler;

	int setPoint;
	int elevatorMode;

	boolean[] button;
	boolean servo_1State;
	boolean servo_2State;
	protected boolean isManual;
	private boolean overcurrent_limit, overcurrent_limit_triggered;
	private boolean fork_hung, fork_hung_triggered;
	
	private volatile long flap_timeout = 0;
	private static int timeout_looptime = 50;

	int loopcount;
	long starttime;
	
	ForkMode fm;
	
	public Elevator() {
		elevator = new ElevatorThreadClass();

		limit = new DigitalInput(Map.ELEVATOR_DIGITAL_INPUT_PORT);
		// handler = new HallHandlerClass();
		// hallSensor = new DigitalInput(Map.ELEVATOR_DIGITAL_INPUT_PORT);
		// hallCounter = new Counter(hallSensor);
		elevatorSolenoid = new DoubleSolenoid(Map.ELEVATOR_SOLENOID_FORWARD_PORT, Map.ELEVATOR_SOLENOID_REVERSE_PORT);
		flapperSolenoid = new Solenoid(Map.ELEVATOR_FLAPPER_SOLENOID_PORT);
		// hallCounter.setUpDownCounterMode();
		// hallCounter.setUpSource(hallSensor);// /// may neeed to goooo
		// befoooore.
		servo_1 = new Servo(Map.ELEVATOR_SERVO_LEFT_PORT);
		servo_2 = new Servo(Map.ELEVATOR_SERVO_RIGHT_PORT);
		elevatorMotor = new CANTalon(Map.ELEVATOR_TALON_PORT);
		// hallSensor.enableInterrupts();
		// hallSensor.requestInterrupts(handler);
		setPoint = 0;
		isManual = true;
		loopcount = 0;
		//fm = ForkMode.retracted;
		fm = ForkMode.NULL;
		
		overcurrent_limit = overcurrent_limit_triggered = false;
		fork_hung = false;
		
		new Timer().schedule(new TimerTask() {
			public void run() {
				if(flap_timeout > timeout_looptime)
					flap_timeout -= timeout_looptime;
				else
					flap_timeout = 0;
			}
		}, 0, timeout_looptime);
		
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

	// public void useSetPoint() {
	// if (setPoint < hallCounter.get()) {
	// elevatorMotor.set(Map.ELEVATOR_DOWN_SPEED); // -1
	// }
	//
	// else if (setPoint > hallCounter.get()) {
	// elevatorMotor.set(Map.ELEVATOR_UP_SPEED); // 1
	// }
	//
	// else if (setPoint == hallCounter.get())
	// elevatorMotor.set(Map.ELEVATOR_NONE_SPEED); // 0
	// }

	private class ElevatorThreadClass extends Thread {
		protected boolean isRunning = true;
		boolean[] button;

		protected boolean checkButtons() {
			for (boolean chkbtns : IO.elevatorButtonValues()) {
				if (chkbtns) {
					return true;
				}
			}
			return false;
		}

		public void run() {
			starttime = System.currentTimeMillis();
			while (isRunning) {
				if (loopcount == 0) {
					starttime = System.currentTimeMillis();
				}
				loopcount++;

				if (ds.isOperatorControl()) {
					setElevatorMode(IO.elevator_mode());

					isManual = IO.elevator_manual_toggle() || (isManual && !checkButtons());
					if (isManual) {
						
						//System.out.println(elevatorMotor.getOutputCurrent() + "\t" + IO.elevator_manual() * 1.0);
						// Motor overcurrent protection
						if(!overcurrent_limit && !overcurrent_limit_triggered && elevatorMotor.getOutputCurrent() > Map.ELEVATOR_OVERCURRENT_LIMIT) {
							overcurrent_limit_triggered = true;
							new Timer().schedule(new TimerTask() {
								public void run() {
									overcurrent_limit_triggered = false;
									if(elevatorMotor.getOutputCurrent() > Map.ELEVATOR_OVERCURRENT_LIMIT) {
										overcurrent_limit = true;
										new Timer().schedule(new TimerTask() {
											public void run() {
												overcurrent_limit = false;
											}
										}, Map.ELEVATOR_OVERCURRENT_TIMEOUT);
									}
								}
							}, Map.ELEVATOR_OVERCURRENT_DETECTION_TIME);
						}
						
						if (IO.elevator_manual_toggle()) {
							manual(IO.elevator_manual() * ((IO.elevator_manual() > 0.0 || !overcurrent_limit) ? 1.0 : 0.3 ));
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
						// useSetPoint();

						// PID();
						manual(0);
					}
				}
				
				if ((fm == ForkMode.retracted) && elevatorSolenoid.get() != DoubleSolenoid.Value.kReverse) { // Forks
																									// retracted
					fm = ForkMode.retractedFinal;
					
					flapperSolenoid.set(false);
					
					//flap_timeout = 0;
										
					// solenoid retracted, servos up
					if (servo_1.getAngle() != Map.ELEVATOR_SERVO_LEFT_OPEN_ANGLE || servo_2.getAngle() != Map.ELEVATOR_SERVO_RIGHT_OPEN_ANGLE) {
						servo_1.setAngle(Map.ELEVATOR_SERVO_LEFT_OPEN_ANGLE);
						servo_2.setAngle(Map.ELEVATOR_SERVO_RIGHT_OPEN_ANGLE);
						flap_timeout = Map.ELEVATOR_RETRACTION_TIMEOUT;
					}
					
					new Timer().schedule(new TimerTask() {
						public void run() {
							if(fm == ForkMode.retractedFinal)
								elevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
						}
					}, flap_timeout);
					
					
				} else if (fm == ForkMode.toteMode && servo_1.getAngle() != Map.ELEVATOR_SERVO_LEFT_DOWN_ANGLE &&  servo_2.getAngle() != Map.ELEVATOR_SERVO_RIGHT_DOWN_ANGLE) { // Tote pickup
					// solenoid exteded, servos down
					servo_1.setAngle(Map.ELEVATOR_SERVO_LEFT_DOWN_ANGLE);
					servo_2.setAngle(Map.ELEVATOR_SERVO_RIGHT_DOWN_ANGLE);

					elevatorSolenoid.set(DoubleSolenoid.Value.kForward);
					new Timer().schedule(new TimerTask() {
						public void run() {
							if(fm == ForkMode.toteMode) // Check to see if things were canceled
								fm = ForkMode.toteModeFinal;
						}
					}, 700);
					
					
				}else if (fm == ForkMode.toteModeFinal)
				{
					// elevator move down = positive vals
					if (IO.fork_flapper_override() || elevatorMotor.get() > 0.0) {
						flapperSolenoid.set(false);
					}
					// elevator move up = neg vals
					else if (elevatorMotor.get() <= 0.0) {
						flapperSolenoid.set(true);
					}
				}
				
				else if(fm == ForkMode.binMode) {// Bin pickup
												 // soleoid extended, servos up
					flap_timeout = Map.ELEVATOR_RETRACTION_TIMEOUT;
						
					servo_1.setAngle(Map.ELEVATOR_SERVO_LEFT_OPEN_ANGLE);
					servo_2.setAngle(Map.ELEVATOR_SERVO_RIGHT_OPEN_ANGLE);
					elevatorSolenoid.set(DoubleSolenoid.Value.kForward);
					flapperSolenoid.set(false);
					
					fm = ForkMode.binModeFinal;
				}

			}
		}

		public void stopElevator() {
			isRunning = false;
		}
	}
		
	public void setElevatorMode(ForkMode elevatorMode) {
		switch (elevatorMode)
		{
		case retracted:
			if(fm != ForkMode.retractedFinal)
				fm = ForkMode.retracted;
			break;
		case toteMode:
			if(fm != ForkMode.toteModeFinal)
				fm = ForkMode.toteMode;
			break;
		case binMode:
			if(fm != ForkMode.binModeFinal)
				fm = ForkMode.binMode;
			break;
		default:
			break;
		}
	}

	public void manual(double y) {
		if (!limit.get() || y <= 0.0) {
			manual_raw(y);
		} else {
			manual_raw(0.0);
		}
	}
	
	public void manual_raw(double y){elevatorMotor.set(y);}

	public int get_elevator_level() {
		return hallCounter.get();
	}

	public double[] dump() {
		double[] vals = new double[11];
		vals[0] = Utils.boolconverter(isManual); // 0 is false, 1 is true
		vals[1] = IO.elevatorMode; // 0 is retracted mode, 1 is tote mode, 2 is
									// bin mode
		vals[2] = setPoint; // desired lvl
		vals[3] = 0;// hallCounter.get(); // current lvl
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
