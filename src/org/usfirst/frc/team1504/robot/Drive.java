package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Drive 
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

	public Drive() 
	{
		DriveThread = new DriveThreadClass();
		leftstick = new Joystick(0);
		rightstick = new Joystick(1);
		
		frontleft = new CANTalon(Map.FRONT_LEFT_TALON_PORT);
		backleft = new CANTalon(Map.BACK_LEFT_TALON_PORT);
		backright = new CANTalon(Map.BACK_RIGHT_TALON_PORT);
		frontright = new CANTalon(Map.FRONT_RIGHT_TALON_PORT);
		
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
	
	
	public void joystickCompute()
	{
		double max = Math.max(1.0, Math.abs(forward) + Math.abs(right) + Math.abs(rotate));
		frontleft_val = (forward + right - rotate)/max;
		frontright_val = (forward - right + rotate)/max;
		backleft_val = (forward - right - rotate)/max;
		backright_val = (forward + right + rotate)/max;
	}
	
	
	public void motorOutput()
	{
		frontleft.set(frontleft_val);
		frontright.set(frontright_val);
		backleft.set(backleft_val);
		backright.set(backright_val);
	}
	
	
	private class DriveThreadClass extends Thread
	{
		protected boolean run = true;
		public void startMecanum() 
		{
			while(run)
				{				
					forward = leftstick.getY();
					right = leftstick.getX();
					rotate = rightstick.getX();	
				
					joystickCompute();
				
					motorOutput();
				}
		}
		
		
		public void stopMecanum()
		{
			run = false;
		}

	}

}