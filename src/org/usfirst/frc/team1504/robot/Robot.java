package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;

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
	Serial serial;
	DriverStation driverstation = DriverStation.getInstance();

	// PowerDistributionPanel pdp;
	// double pdpvoltage;

	// Timer timer;

	// Compressor pcm;
	// float pcm_current;
	// boolean pcm_isEnabled;

	// BuiltInAccelerometer accelerometer;

	public Robot() {
		// myRobot = new RobotDrive(0, 1, 2, 3); //frontleft, backleft,
		// frontright, backright
		// myRobot.setExpiration(0.1);
		// leftStick = new Joystick(0);
		// rightStick = new Joystick(1);
		MapXML map = new MapXML();
		classes = new Loggable[6];
		classes[0] = new Aligner();
		classes[1] = new BinCapture();
		classes[2] = new Drive();
		classes[3] = new Elevator();
		classes[4] = new IO();
		classes[5] = new Serial();
		
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
//		log.start("A");
		switch(IO.get_auton_mode())
		{
		case 0:
			while (driverstation.isAutonomous()) {
				// Drive forward for 6 seconds, into the AUTO ZONE
				drive.autonDrive(-.5, 0, 0);
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {}
				
				drive.autonDrive(0, 0, 0);
			}

		case 1:	
		 //Pickup a YELLOW TOTE, and track to the right for .5 seconds, into
//		 the AUTO ZONE with the TOTE.
			while(driverstation.isAutonomous())
			{
				 elevator.setPoint = 2;
//				 elevator.useSetPoint();
				 try{
				 Thread.sleep(500);
				 }
				 catch(InterruptedException ex){}
				
				 long toteautontime = System.currentTimeMillis();
				 while (Math.abs(System.currentTimeMillis() - toteautontime) <= 5000)
				 {
				 drive.autonDrive(0, .5, 0);
				 }
				 drive.autonDrive(0, 0, 0);
			}
			
		case 2: 
			//Grabs a bin from the STEP, driving forward for 5 seconds into the
	//		 AUTO ZONE, and then turning 180 degrees in 1 second.
			 while (driverstation.isAutonomous())
			 {
				 capture.claw.set(true);
				 try
				 {
				 Thread.sleep(500);
				 }
				 catch (InterruptedException ex) {}
				 capture.arm.set(DoubleSolenoid.Value.kReverse);
				
				 long binautontime = System.currentTimeMillis();
				 while(Math.abs(System.currentTimeMillis() - binautontime) <= 6000)
				 {
				 drive.autonDrive(.5, .5, .5);
				 try
				 {
				 Thread.sleep(5000);
				 }
				 catch (InterruptedException ex) {}
				
		
				 drive.autonDrive(0, 0, .25);
				 try
				 {
				 Thread.sleep(1000);
				 }
				 catch (InterruptedException ex) {}
				 drive.autonDrive(0, 0, 0);
				 }
			 }
		default:
			while (driverstation.isAutonomous()) {
				// Drive forward for 6 seconds, into the AUTO ZONE
				drive.autonDrive(-.5, 0, 0);
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {}
				
				drive.autonDrive(0, 0, 0);
			}

		}
//		log.stop();
	}
	public void operatorControl() {
		log.start("O");
		System.out.println(System.getProperty("user.name"));
		while (isOperatorControl() && isEnabled()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		log.stop();
	}

	public void disabled() {
		log.start("TD");
	}
}
