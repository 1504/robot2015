package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;

public class Aligner extends Loggable {
	// Solenoids
	DoubleSolenoid stage_1;
	protected int clawStage;

	DriverStation ds = DriverStation.getInstance();
	
	
	int loopcount;
	long starttime;
	
	AlignerThreadClass aligner;

	public Aligner() {
		loopcount = 0;
		
		aligner = new AlignerThreadClass();
		
		stage_1 = new DoubleSolenoid(Map.STAGE_ONE_SOLENOID_FORWARD_PORT, Map.STAGE_ONE_SOLENOID_REVERSE_PORT);
	}

	public void start()
	{
		aligner.start();
	}
	
	protected void setPosition(int position) {
		switch (position) {
		case 0: //retracted
			stage_1.set(DoubleSolenoid.Value.kReverse);
			clawStage = 0;
			break;
		case 1: //extended
			stage_1.set(DoubleSolenoid.Value.kForward);
			clawStage = 1;
			break;
		default:

			break;
		}
	}

	public void open() {
		setPosition(0);
	}

	public void close() {
		setPosition(1);
	}

	public double[] dump() {
		double[] motor = new double[3];
		// motor speed, current, voltage
		motor[0] = clawStage;
		motor[1] = loopcount;
		motor[2] = System.currentTimeMillis() - starttime;
		loopcount = 0;
		return motor;
	}
	
	private class AlignerThreadClass extends Thread {
		protected boolean isRunning = true;
		protected boolean[] buttons;
		public void run() {
			starttime = System.currentTimeMillis();
			while (isRunning) {
				if (loopcount == 0)
				{
					starttime = System.currentTimeMillis();
				}
				loopcount++;
				buttons = IO.alignerButtons();
				if(ds.isOperatorControl())
				{
					if (buttons[0]) {
						open();
					} else if (buttons[1]) {
						close();
					}
				}
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void stopAligner() {
			isRunning = false;
		}

	}
}
