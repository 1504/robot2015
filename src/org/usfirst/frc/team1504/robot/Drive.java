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
		//DriveThread.run();
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
	public double[] dumpDrive()
	{
		double[] motors = new double[12];
		
		motors[0] = frontleft_val;
		motors[1] = frontleft.getOutputCurrent();
		motors[2] = frontleft.getOutputVoltage();
		
		motors[3] = backleft_val;
		motors[4] = backleft.getOutputCurrent();
		motors[5] = backleft.getOutputVoltage();
		
		motors[6] = backright_val;
		motors[7] = backright.getOutputCurrent();
		motors[8] = backright.getOutputVoltage();
		
		motors[9] = frontright_val;
		motors[10] = frontright.getOutputCurrent();
		motors[11] = frontright.getOutputVoltage();
		
		return motors;
	}
	
	private class DriveThreadClass extends Thread
	{
		protected boolean isRunning = true;
		public void run() 
		{
			while(isRunning)
				{				
					dircns = IO.mecanum_input();
				
					outputCompute(dircns);
				
					motorOutput();
				}
		}
		
		
		public void stopMecanum()
		{
			isRunning = false;
		}

	}

}
