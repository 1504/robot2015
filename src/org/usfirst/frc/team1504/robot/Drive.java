package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.CANTalon;

public class Drive 
{
	private static DriveThreadClass DriveThread;
	
	CANTalon frontleft;
	CANTalon backleft;
	CANTalon backright;
	CANTalon frontright;
	
	double frontleft_val;
	double backleft_val;
	double backright_val;
	double frontright_val;

	
	double[] dircns;

	public Drive() 
	{
		DriveThread = new DriveThreadClass();

		frontleft = new CANTalon(Map.FRONT_LEFT_TALON_PORT);
		backleft = new CANTalon(Map.BACK_LEFT_TALON_PORT);
		backright = new CANTalon(Map.BACK_RIGHT_TALON_PORT);
		frontright = new CANTalon(Map.FRONT_RIGHT_TALON_PORT);
		
		dircns = new double[3];
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
	
	
	public void outputCompute(double[] input)
	{
		double max = Math.max(1.0, Math.abs(input[0]) + Math.abs(input[1]) + Math.abs(input[2]));
		
		frontleft_val = (input[0] + input[1] - input[2])/max;
		frontright_val = (input[0] - input[1] + input[2])/max;
		backleft_val = (input[0] - input[1] - input[2])/max;
		backright_val = (input[0] + input[1] + input[2])/max;
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
					IO.mecanum_input(dircns);
				
					outputCompute(dircns);
				
					motorOutput();
				}
		}
		
		
		public void stopMecanum()
		{
			run = false;
		}

	}

}
