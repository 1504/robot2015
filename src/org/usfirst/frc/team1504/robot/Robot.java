package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * This project is the property of FRC team 1504: The Desperate Penguins, and
 * any unauthorized use will result in vaporization. You have been warned. Good
 * luck.
 */
public class Robot extends SampleRobot {
	RobotDrive myRobot; // class that handles basic drive operations
	Loggable[] classes;
	Logger log;
	Drive drive;
	Aligner aligner;
	BinCapture capture;
	Elevator elevator;
	IO io;
	// Serial serial;
	DriverStation driverstation = DriverStation.getInstance();

	// PowerDistributionPanel pdp;
	// double pdpvoltage;

	// Timer timer;

	// Compressor pcm;
	// float pcm_current;
	// boolean pcm_isEnabled;

	// BuiltInAccelerometer accelerometer;

	SerialPort ser = new SerialPort(9600, SerialPort.Port.kOnboard);

	public Robot() {
		// myRobot = new RobotDrive(0, 1, 2, 3); //frontleft, backleft,
		// frontright, backright
		// myRobot.setExpiration(0.1);
		// leftStick = new Joystick(0);
		// rightStick = new Joystick(1);
		MapXML map = new MapXML();
		classes = new Loggable[5];
		classes[0] = new Aligner();
		classes[1] = new BinCapture();
		classes[2] = new Drive();
		classes[3] = new Elevator();
		classes[4] = new IO();
		// classes[5] = new Serial();

		log = new Logger(classes);

		aligner = (Aligner) classes[0];
		capture = (BinCapture) classes[1];
		drive = (Drive) classes[2];
		elevator = (Elevator) classes[3];
		io = (IO) classes[4];

		// pdp = new PowerDistributionPanel();
		//
		// timer = new Timer();
		//
		// pcm = new Compressor();
		//
		// accelerometer = new BuiltInAccelerometer();

		drive.start(); // Starts outside because needs to WORK during
						// autonomous.
		elevator.start(); // Starts outside because needs to WORK during
							// autonomous.
		aligner.start(); // Starts outside because needs to WORK during
							// autonomous.
		capture.start(); // Starts outside because needs to WORK during
							// autonomous.
		io.startmouse(); // Starts outside because needs to communicate with
							// Arduino even during autonomous.

	}

	public void autonomous() {
		log.start("A");
		switch (IO.get_auton_mode()) {
		case 0:
		default: // stay put
			drive.autonDrive(0.0, 0.0, 0.0);
			break;
		case 1:
			// Pickup a YELLOW TOTE, and turn to the right and drive into
			// the AUTO ZONE with the TOTE.
			elevator.setElevatorMode(1); // tote mode
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			elevator.manual(-.75); // Pick up tote
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elevator.manual(0);
			drive.autonDrive(0.0, 0.0, .35);//rotate 90 degrees to the right
			try {
				Thread.sleep(1750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drive.autonDrive(-.5, 0.0, 0.0);//drive forwards
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e3) {
				e3.printStackTrace();
			}
			drive.autonDrive(0.0, 0.0, -.35);//rotate 90 degrees to the left
			try {
				Thread.sleep(1750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elevator.manual(.75); // set tote down
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elevator.manual(0);
			drive.autonDrive(0.3, 0.0, 0.0);//drive back
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			elevator.setElevatorMode(0); //retract forks.
			drive.autonDrive(0, 0, 0);
			break;
		case 2:
			// Grabs a bin from the STEP, driving forward for 5 seconds into the
			// AUTO ZONE, and then turning 180 degrees in 1 second.
			capture.extend();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException ex) {
			}

			drive.autonDrive(-.25, 0.0, 0.0); // .5?
			try {
				Thread.sleep(1500); // change this later
			} catch (InterruptedException ex) {
			}

			capture.grab();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}

			drive.autonDrive(-.25, 0.0, 0.0);
			try {
				Thread.sleep(3500);
			} catch (InterruptedException ex) {
			}

			drive.autonDrive(.25, 0.0, 0.0);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			capture.grab();
			capture.extend();

			drive.autonDrive(0.0, 0.0, 0.0);

			break;

		case 3:
			// pick up bin from staging zone
			elevator.setElevatorMode(2); // Bin mode
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			elevator.manual(-.75); // Pick up bin
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elevator.manual(0.0); // stop elevator lift from moving
			drive.autonDrive(.5, 0.0, 0.0); // drive backwards
			try {
				Thread.sleep(1750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drive.autonDrive(0.0, 0.0, .35); // rotate 90 degrees to ensure
												// robot is completely in auto
												// zone/for convenience when
												// teleop starts
			try {
				Thread.sleep(1750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drive.autonDrive(0.0, 0.0, 0.0); // stop driving
			try {
				Thread.sleep(250);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			elevator.manual(.75); // elevator down/set down bin
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elevator.manual(0);
			elevator.setElevatorMode(0);
			drive.autonDrive(.25, 0.0, 0.0); // drive backward
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;

		case 4:
			// Drive forward for 6 seconds, into the AUTO ZONE
			drive.autonDrive(-.2, 0.0, 0.0); // Reverse to push bin out of the
												// way (- is forward)
			// drive.autonDrive(0.0, .75, 0.0);
			try {
				Thread.sleep(2900);
			} catch (InterruptedException e) {
			}

			drive.autonDrive(0.0, 0.0, 0.0);

			drive.autonDrive(.25, 0, 0);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			drive.autonDrive(0.0, -.5, 0.0);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		log.stop();
	}

	public void operatorControl() {
		log.start("O");
		while (isOperatorControl() && isEnabled()) {
			try {
				ser.writeString("test\n");
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		log.stop();
	}

	public void disabled() {
	}
}
