package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Aligner
{
	//Solenoids
	DoubleSolenoid stage_1;
	DoubleSolenoid stage_2;
	protected int clawStage;
	
	
	//Talon
	CANTalon align;
	
	public Aligner()
	{
		stage_1 = new DoubleSolenoid(Map.STAGE_ONE_SOLENOID_FORWARD_PORT, Map.STAGE_ONE_SOLENOID_REVERSE_PORT);
		stage_2 = new DoubleSolenoid(Map.STAGE_TWO_SOLENOID_FORWARD_PORT, Map.STAGE_TWO_SOLENOID_REVERSE_PORT);
	
		align = new CANTalon(Map.ALIGNER_TALON_PORT);
	}
	
	protected void setPosition(int position)
	{
		switch (position)
		{
		case 0:	//open
			stage_1.set(DoubleSolenoid.Value.kReverse);
			stage_2.set(DoubleSolenoid.Value.kReverse);
			clawStage = 0;
			break;
		case 1:	//almost closed
			if (clawStage == 2)
				setPosition(0);
			stage_1.set(DoubleSolenoid.Value.kForward);
			stage_2.set(DoubleSolenoid.Value.kReverse);
			clawStage = 1;
			break;
		case 2:	//closed
			stage_1.set(DoubleSolenoid.Value.kForward);
			stage_2.set(DoubleSolenoid.Value.kForward);
			clawStage = 2;
			break;
		default:
			
			break;
		}
	}
	
	public void open()
	{setPosition(0);}
	public void half()
	{setPosition(1);}
	public void close()
	{setPosition(2);}
	
	public void setSpeed(boolean speed)
	{		
		if (speed)
			align.set(1);
		else
			align.set(0);
	}
	
	public double[] dump()
	{
		double[] motor = new double[3];
		//motor speed, current, voltage
		motor[0] = align.getSpeed();
		motor[1] = align.getOutputCurrent();
		motor[2] = align.getBusVoltage();
		return motor;
	}
}
