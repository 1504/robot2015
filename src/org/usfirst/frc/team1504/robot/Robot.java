package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.Compressor;
//import edu.wpi.first.wpilibj.BuiltInAccelerometer;


/**
 * This project is the property of FRC team 1504: The Desperate Penguins, and any unauthorized use will result in vaporization.
 * You have been warned. Good luck.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
//    Joystick leftStick;  // set to ID 1 in DriverStation
//    Joystick rightStick; // set to ID 2 in DriverStation
    Loggable[] classes;
    Logger log;
    Drive drive;
    Aligner aligner;
    BinCapture capture;
    Elevator elevator;
    IO io;
    
//    PowerDistributionPanel pdp;
//    double pdpvoltage;
    
//    Timer timer;
    
//    Compressor pcm;
//    float pcm_current;	
//    boolean pcm_isEnabled;
    
//    BuiltInAccelerometer accelerometer;
    

    public Robot() {
//        	myRobot = new RobotDrive(0, 1, 2, 3); //frontleft, backleft, frontright, backright
//        	myRobot.setExpiration(0.1);
//        	leftStick = new Joystick(0);
//        	rightStick = new Joystick(1);
        	
        	classes = new Loggable[5];
        	classes[0] = new Aligner();
        	classes[1] = new BinCapture();
        	classes[2] = new Drive();
        	classes[3] = new Elevator();
        	classes[4] = new IO();
        	log = new Logger(classes);
        	
        	aligner = (Aligner) classes[0];
        	capture = (BinCapture) classes[1];
        	drive = (Drive) classes[2];
        	elevator = (Elevator) classes[3];
        	io = (IO) classes[4];

//        	pdp = new PowerDistributionPanel();
//        	
//        	timer = new Timer();
//        	
//        	pcm = new Compressor();
//        	
//        	accelerometer = new BuiltInAccelerometer();
        	
        	drive.start(); //Starts outside because needs to WORK during autonomous.
           	elevator.start(); //Starts outside because needs to WORK during autonomous.
           	aligner.start(); //Starts outside because needs to WORK during autonomous.
          	capture.start(); //Starts outside because needs to WORK during autonomous.
//           	io.startmouse(); //Starts outside because needs to communicate with Arduino even during autonomous.

    }
    
    public void autonomous() {
    	long time = System.currentTimeMillis();
    	
    	//Drive forward for 6 seconds, into the AUTO ZONE
    	while(Math.abs(System.currentTimeMillis() - time) <= 6000)
    	{
    		drive.backleft.set(-.5);
    		drive.backright.set(.5);
    		drive.frontleft.set(-.5);
    		drive.frontright.set(.5);
    	}
    	
//    	//Pickup a YELLOW TOTE, and track to the right for 5 seconds, into the AUTO ZONE with the TOTE.
//    	while(Math.abs(System.currentTimeMillis() - time) <= 15000)
//    	{
//    		elevator.setPoint = 2;
//    		elevator.useSetPoint();	
//    		try{
//    			Thread.sleep(500);
//    		}
//    		catch(InterruptedException ex){}
//    		
//    		long toteautontime = System.currentTimeMillis();
//    		while (Math.abs(System.currentTimeMillis() - toteautontime) <= 5000)
//    		{
//    			drive.backleft.set(.5);
//    			drive.backright.set(.5);
//    			drive.frontleft.set(-.5);
//    			drive.frontright.set(-.5);
//    		}
//    	}
//    	
//    	//Grabs a bin from the STEP, driving forward for 5 seconds into the AUTO ZONE, and then turning 180 degrees in 1 second.
//    	while (Math.abs(System.currentTimeMillis() - time) <= 15000)
//    	{
//    		capture.claw.set(true);
//    		try
//    		{
//    			Thread.sleep(500);
//    		}
//    		catch (InterruptedException ex) {}
//    		capture.arm.set(DoubleSolenoid.Value.kReverse);
//    		
//    		long binautontime = System.currentTimeMillis();
//    		while(Math.abs(System.currentTimeMillis() - binautontime) <= 6000)
//    		{
//    			drive.backleft.set(-.5);
//        		drive.backright.set(.5);
//        		drive.frontleft.set(-.5);
//        		drive.frontright.set(.5);
//        		
//        		try
//        		{
//        			Thread.sleep(5000);
//        		}
//        		catch (InterruptedException ex) {}
//        		
//    			drive.backleft.set(.5);
//        		drive.backright.set(.5);
//        		drive.frontleft.set(.5);
//        		drive.frontright.set(.5);
//
//    		}
//    		
//    	}
    	
    }
    
    public void operatorControl() {
       	log.start("O");       	
    	System.out.println(System.getProperty("user.name"));
        while (isOperatorControl() && isEnabled()) { 
        }
        log.stop();
    }
}
