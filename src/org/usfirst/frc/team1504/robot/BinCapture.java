package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.CANTalon;

public class BinCapture //thread
{
//		private static BinCaptureClass BinCap;
		
		CANTalon motor;
		
		DoubleSolenoid arm;
		DoubleSolenoid claw;
		
	public BinCapture() 
	{
//		BinCap = new BinCaptureClass();
		
		motor = new CANTalon(Map.TOTE_CAPTURE_TALON_PORT);
		
		arm = new DoubleSolenoid(Map.EXTEND_SOLENOID_FORWARD_PORT, Map.EXTEND_SOLENOID_REVERSE_PORT);
		claw = new DoubleSolenoid(Map.CLAW_SOLENOID_FORWARD_PORT, Map.CLAW_SOLENOID_REVERSE_PORT);
	}
	
	public void startArm()
	{
		motor.setPID(1, 0, 0);
	}
	
	public void extension(boolean bool)
	{
		if (bool)
		{
			arm.set(DoubleSolenoid.Value.kForward);
		}
		else
		{
			arm.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	public void grab(boolean bool)
	{
		if (bool)
		{
			claw.set(DoubleSolenoid.Value.kForward);
		}
		else
		{
			claw.set(DoubleSolenoid.Value.kReverse);
		}
	}
	/*private class BinCaptureClass extends Thread
	{
		protected boolean run = true;
		public void start() 
		{
			while(run)
				{				
					// closed loop
				}
		}
		
		
	}*/
}

