package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class Aligner extends Loggable {
	// Solenoids
	DoubleSolenoid stage_1;
	Solenoid stage_2;
	protected int clawStage;

	// Talon
	CANTalon align;
	
	AlignerThreadClass aligner;

	public Aligner() {
		aligner = new AlignerThreadClass();
		
		stage_1 = new DoubleSolenoid(Map.STAGE_ONE_SOLENOID_FORWARD_PORT, Map.STAGE_ONE_SOLENOID_REVERSE_PORT);
		stage_2 = new Solenoid(Map.STAGE_TWO_SOLENOID_PORT);

		align = new CANTalon(Map.ALIGNER_TALON_PORT);
	}

	public void start()
	{
		aligner.start();
	}
	
	protected void setPosition(int position) {
		switch (position) {
		case 0: // open
			stage_1.set(DoubleSolenoid.Value.kReverse);
			stage_2.set(false);
			clawStage = 0;
			break;
		case 1: // almost closed
			if (clawStage == 2) {
				setPosition(0);
				// Robot_Main.timer.delay(0.005);
				// or add break, which requires 2 button presses
			}

			stage_1.set(DoubleSolenoid.Value.kForward);
			stage_2.set(false);
			clawStage = 1;
			break;
		case 2: // closed
			stage_1.set(DoubleSolenoid.Value.kForward);
			stage_2.set(true);
			clawStage = 2;
			break;
		default:

			break;
		}
	}

	public void open() {
		setPosition(0);
	}

	public void half() {
		setPosition(1);
	}

	public void close() {
		setPosition(2);
	}

	public void setSpeed(boolean speed) {
		if (speed)
			align.set(1.0);
		else
			align.set(0.0);
	}

	public double[] dump() {
		double[] motor = new double[4];
		// motor speed, current, voltage
		motor[0] = align.getSpeed();
		motor[1] = align.getOutputCurrent();
		motor[2] = align.getBusVoltage();
		motor[3] = clawStage;
		return motor;
	}
	
	private class AlignerThreadClass extends Thread {
		protected boolean isRunning = true;
		protected boolean[] buttons;

		public void run() {
			while (isRunning) {
				buttons = IO.alignerButtons();
				if (buttons[0]) {
					setPosition(0);
				} else if (buttons[1]) {
					setPosition(1);
				} else if (buttons[2]) {
					setPosition(2);
				}
			}
		}

		public void stopAligner() {
			isRunning = false;
		}

	}
}
