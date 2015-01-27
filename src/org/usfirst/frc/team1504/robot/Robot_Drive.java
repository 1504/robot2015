package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;

public class Robot_Drive {
	private static DriveThreadClass DriveThread;
	Joystick leftstick;
	Joystick rightstick;
	
	CanTalonSRX frontleft;
	CanTalonSRX backleft;
	CanTalonSRX backright;
	CanTalonSRX frontright;
	
	double forward;
	double right;
	double rotate;
	double frontleft_val;
	double backleft_val;
	double backright_val;
	double frontright_val;
	double max;

	public Robot_Drive() {
		DriveThread = new DriveThreadClass();
		leftstick = new Joystick(0);
		rightstick = new Joystick(1);
		forward = leftstick.getY();
		right = leftstick.getX();
		rotate = rightstick.getX();
		frontleft = new CanTalonSRX(3);
		backleft = new CanTalonSRX(2);
		backright = new CanTalonSRX(0);
		frontright = new CanTalonSRX(1);
		
		max = Math.max(1.0, Math.abs(forward) + Math.abs(right) + Math.abs(rotate));
		frontleft_val = (forward + right - rotate)/max;
		frontright_val = (forward - right + rotate)/max;
		backleft_val = (forward - right - rotate)/max;
		backright_val = (forward + right + rotate)/max;
		
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
		public void startMecanum() {
		while(run)
			{
				frontleft.Set(frontleft_val);
				frontright.Set(frontright_val);
				backleft.Set(backleft_val);
				backright.Set(backright_val);
			}
		}
		public void stopMecanum()
		{
			run = false;
		}
	}

}
