package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CANTalon;
//import edu.wpi.first.wpilibj.RobotBase;
//import edu.wpi.first.wpilibj.can.CANNotInitializedException;
//import edu.wpi.first.wpilibj.command.PrintCommand;


/**
 * This project is the property of FRC team 1504: The Desperate Penguins, and any unauthorized use will result in vaporization.
 * You have been warned. Good luck.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation
    Loggable[] classes;
    Logger log;
    Drive drive;
    Aligner aligner;
    BinCapture capture;
    Elevator elevator;
    IO io;
    
    PowerDistributionPanel pdp;
    double pdpvoltage;
    
    Timer timer;
    
    Compressor pcm;
    float pcm_current;	
    boolean pcm_isEnabled;
    
    BuiltInAccelerometer accelerometer;
    
    
    //Talons
    CANTalon frontleft;
    double fl_current;
    
    CANTalon backleft;
    double bl_current;
    
    CANTalon backright;
    double br_current;
    
    CANTalon frontright;
    double fr_current;
    
    
//CAN Request things
//    double current_backright;
//    double current_backleft;
//    double current_frontleft;
//    double current_frontright;
//    int backright_channel;
//    int backleft_channel;
//    int frontleft_channel;
//    int frontright_channel;


    public Robot() {
//        	myRobot = new RobotDrive(0, 1, 2, 3); //frontleft, backleft, frontright, backright
//        	myRobot.setExpiration(0.1);
        	leftStick = new Joystick(0);
        	rightStick = new Joystick(1);
        	
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
        			
        	pdp = new PowerDistributionPanel();
        	
        	timer = new Timer();
        	
        	pcm = new Compressor();
        	
        	accelerometer = new BuiltInAccelerometer();
        	
        	frontleft = new CANTalon(10);
        	backleft = new CANTalon(11);
        	backright = new CANTalon(12);
        	frontright = new CANTalon(13);
        	log.start("O");
        	aligner.start();
        	capture.start();
        	drive.start();
        	elevator.start();
        	io.startmouse();

    }
    
//    public void autonomous() {
//    	
//    }
    
    public void operatorControl() {
    	System.out.println(System.getProperty("user.name"));
        while (isOperatorControl() && isEnabled()) { 

        }
    }
}