package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Robot_Drive 
{
	private static DriveThreadClass DriveThread;
	Joystick leftstick;
	Joystick rightstick;
	
	CANTalon frontleft;
	CANTalon backleft;
	CANTalon backright;
	CANTalon frontright;
	
	double frontleft_val;
	double backleft_val;
	double backright_val;
	double frontright_val;
	
	double forward;
	double right;
	double rotate;
	double max;

	public Robot_Drive() 
	{
		DriveThread = new DriveThreadClass();
		leftstick = new Joystick(0);
		rightstick = new Joystick(1);
		
		frontleft = new CANTalon(10);
		backleft = new CANTalon(11);
		backright = new CANTalon(12);
		frontright = new CANTalon(13);
		
	}
	public void start()
	{
		DriveThread.start();
		DriveThread.startMecanum();
	}
	public void stop()
	{
		DriveThread.stopMecanum();
	}
	private class DriveThreadClass extends Thread
	{
		protected boolean run = true;
		public void startMecanum() 
		{
		while(run)
			{
				max = Math.max(1.0, Math.abs(forward) + Math.abs(right) + Math.abs(rotate));
				
				forward = leftstick.getY();
				right = leftstick.getX();
				rotate = rightstick.getX();	
			
				frontleft_val = (forward + right - rotate)/max;
				frontright_val = (forward - right + rotate)/max;
				backleft_val = (forward - right - rotate)/max;
				backright_val = (forward + right + rotate)/max;
				
				frontleft.set(frontleft_val);
				frontright.set(frontright_val);
				backleft.set(backleft_val);
				backright.set(backright_val);
			}
		}
		public void stopMecanum()
		{
			run = false;
		}
	}

}
