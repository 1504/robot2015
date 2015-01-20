package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
//import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;

/**
 * This is NOT a demo program showing the use of the RobotDrive class, specifically it 
 * contains the code necessary to operate a robot with tank drive.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation
    PowerDistributionPanel pdp;
    Timer timey;
    CanTalonSRX backright;
    Compressor pcm;
    double looptime;
    double current_backright;
    double current_backleft;
    double current_frontleft;
    double current_frontright;
    int backright_channel;
    int backleft_channel;
    int frontleft_channel;
    int frontright_channel;
    double pdpvoltage;
    float pcm_current;
    boolean pcm_isEnabled;
    BuiltInAccelerometer accelerometer;
    //PrintCommand printy;
    public Robot() {
        	myRobot = new RobotDrive(0, 1, 2, 3); //frontleft, backleft, frontright, backright
        	myRobot.setExpiration(0.1);
        	leftStick = new Joystick(0);
        	rightStick = new Joystick(1);
        	pdp = new PowerDistributionPanel();
        	timey = new Timer();
        	pcm = new Compressor();
        	backright = new CanTalonSRX(0);
    	
    	backright_channel = 12; //motor 3
    	backleft_channel = 13; //motor 1
    	frontleft_channel = 14; //motor 0
    	frontright_channel = 15; //motor 2
    	//printy = new PrintCommand("TEST");
    	accelerometer = new BuiltInAccelerometer();
    }
    /**
     * Runs the motors with tank steering, also asking for 54 CAN requests, and finally printing out all sorts of knowledge.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        timey.start();
        while (true) {
        	myRobot.tankDrive(leftStick, rightStick);
            //Timer.delay(0.005);		// wait for a motor update time
            //printy.start();
        	try {
        		current_backright = pdp.getCurrent(backright_channel);
        		current_backleft = pdp.getCurrent(backleft_channel);
        		current_frontleft = pdp.getCurrent(frontleft_channel);
        		current_frontright = pdp.getCurrent(frontright_channel);
        		pdpvoltage = pdp.getVoltage();
        		pcm_current = pcm.getCompressorCurrent();
            	pcm_isEnabled = pcm.enabled();
        		for (int i=0; i<=15; i++)
        		{
        			pdp.getCurrent(i);
        		}
        		for (int j=0; j<=15; j++)
        		{
        			pdp.getCurrent(j);
        		}
        		for (int k=0; k<=15; k++)
        		{
        			pdp.getCurrent(k);
        		}
        		backright.Set(leftStick.getY());
        	}
        	catch (CANNotInitializedException e) {}
            //System.out.println(timey.get());
            //System.out.println(pcm_current + "    " + pcm_isEnabled + "    " + pdpvoltage + "   " + current_backright + "   " + current_backleft + "   " + current_frontleft + "   " + current_frontright);
        	System.out.println("Orientation:" + accelerometer.getX() + accelerometer.getY() + accelerometer.getZ());
        	//System.out.println("X: " + backright.get() + "Y: " + backright.getY() + "Z: " + backright.getZ())
        	timey.reset();
        }
    }

}