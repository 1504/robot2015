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
    
    Drive drive;
    
    PowerDistributionPanel pdp;
    double pdpvoltage;
    
    Timer timey;
    
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
        	
        	drive = new Drive();
        	
        	pdp = new PowerDistributionPanel();
        	
        	timey = new Timer();
        	
        	pcm = new Compressor();
        	
        	accelerometer = new BuiltInAccelerometer();
        	
        	frontleft = new CANTalon(10);
        	backleft = new CANTalon(11);
        	backright = new CANTalon(12);
        	frontright = new CANTalon(13);
        	
        	drive.start();

//For CAN Requests        	
// 	   		backright_channel = 12; //motor 3
//  	  	backleft_channel = 13; //motor 1
//    		frontleft_channel = 14; //motor 0
//    		frontright_channel = 15; //motor 2

    }
    
    /**
     * Runs the motors with tank steering, also asking for 54 CAN requests, and finally printing out all sorts of knowledge.
     */
    public void startCompetition()
    {
    	
    } 
    
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        timey.start();
        while (isOperatorControl() && isEnabled()) { 
        	
//        	myRobot.tankDrive(leftStick, rightStick);
//            Timer.delay(0.005);		// wait for a motor update time        	
        	
//CAN Request Test  
//            printy.start();
//        	try {
//        		current_backright = pdp.getCurrent(backright_channel);
//        		current_backleft = pdp.getCurrent(backleft_channel); 
//        		current_frontleft = pdp.getCurrent(frontleft_channel);
//        		current_frontright = pdp.getCurrent(frontright_channel);
//        		pdpvoltage = pdp.getVoltage();
//        		pcm_current = pcm.getCompressorCurrent();
//            	pcm_isEnabled = pcm.enabled();
//        		for (int i=0; i<=15; i++)
//        		{
//        			pdp.getCurrent(i);
//        		}
//        		for (int j=0; j<=15; j++)
//        		{
//        			pdp.getCurrent(j);
//        		}
//        		for (int k=0; k<=15; k++)
//        		{
//        			pdp.getCurrent(k);
//        		}
//        		
//        	}
//        	catch (CANNotInitializedException e) {}
//            System.out.println(pcm_current + "    " + pcm_isEnabled + "    " + pdpvoltage + "   " + current_backright + "   " + current_backleft + "   " + current_frontleft + "   " + current_frontright);
        	
            System.out.println("Looptime: " + timey.get());        	
//        	System.out.println("Accelerometer Orientation:" + accelerometer.getX() + accelerometer.getY() + accelerometer.getZ());
        	
        	fl_current = frontleft.getOutputCurrent();
        	bl_current = backleft.getOutputCurrent();
        	br_current = backright.getOutputCurrent();
        	fr_current = frontright.getOutputCurrent();
        	
        	System.out.println("Current: " + fl_current + bl_current + br_current + fr_current);
        	
        	timey.reset();
        }
//        drive.stop();
    }

}