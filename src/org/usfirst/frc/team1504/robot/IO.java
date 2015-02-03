package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.Joystick;

public class IO 
{
	static double forward;
	static double right;
	static double rotate;
	
//	Dual Stick
	static Joystick leftstick;
	static Joystick rightstick;
	
//	Quadcopter
	static Joystick copterstick;
	
	public IO() 
	{
		
		leftstick = new Joystick(0);
		rightstick = new Joystick(1);
	
		copterstick = new Joystick(0); 
	}
	
	public static double[] mecanum_input(double[] dircns)
	{
		
		dircns[0] = leftstick.getRawAxis(Map.JOYSTICK_LEFT_Y_VALUE); //y
		dircns[1] = leftstick.getRawAxis(Map.JOYSTICK_LEFT_X_VALUE); //x
		dircns[2] = rightstick.getRawAxis(Map.JOYSTICK_RIGHT_X_VALUE); //x	
		
		dircns[0] = copterstick.getRawAxis(Map.JOYSTICK_LEFT_Y_VALUE);
		dircns[1] = copterstick.getRawAxis(Map.JOYSTICK_LEFT_X_VALUE);
		dircns[2] = copterstick.getRawAxis(Map.JOYSTICK_RIGHT_X_VALUE);
		
		return dircns;
	}

}
