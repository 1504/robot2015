package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class BinCapture extends Loggable //thread
{
	private static BinCaptureClass BinCap;

	CANTalon motor;

	DoubleSolenoid arm;
	DoubleSolenoid claw;

	//retracted = 0; extended = 1
	double armstate;
	double clawstate;

	boolean armtoggle;
	boolean clawtoggle;


	public BinCapture() {

		BinCap = new BinCaptureClass();

		motor = new CANTalon(Map.TOTE_CAPTURE_TALON_PORT);

//		arm = new DoubleSolenoid(Map.EXTEND_SOLENOID_FORWARD_PORT, Map.EXTEND_SOLENOID_REVERSE_PORT);
//		claw = new DoubleSolenoid(Map.CLAW_SOLENOID_FORWARD_PORT, Map.CLAW_SOLENOID_REVERSE_PORT);

		armtoggle = false;
		clawtoggle = false;
	}

	public void start() {
		BinCap.start();
	}

	public void extend() {
		if (arm.get() == DoubleSolenoid.Value.kForward && !armtoggle) {
			armtoggle = true;
			arm.set(DoubleSolenoid.Value.kReverse);
			armstate=0;
		}
		if (arm.get() == DoubleSolenoid.Value.kReverse && !armtoggle) {
			armtoggle = true;
			arm.set(DoubleSolenoid.Value.kForward);
			armstate=1;
		}

	}

	public void grab() {
		if (claw.get() == DoubleSolenoid.Value.kForward && !clawtoggle) {
			claw.set(DoubleSolenoid.Value.kReverse);
			clawtoggle = true;
			clawstate=0;
		}
		if (claw.get() == DoubleSolenoid.Value.kReverse && !clawtoggle) {
			claw.set(DoubleSolenoid.Value.kForward);
			clawtoggle = true;
			clawstate=1;
		}
	}
	private class BinCaptureClass extends Thread {
		protected boolean run = true;
		public void start() {
			while (run) {
				if (IO.bincapture_input()[0]) {
					extend();
				} else {
					armtoggle = false;
				}
				if (IO.bincapture_input()[1]) {
					grab();
				} else {
					clawtoggle = false;
				}
			}
		}
	}

	public double[] dump() {
		double[] bin_values = new double[4];
		bin_values[0] = armstate;
		bin_values[1] = clawstate;
		bin_values[2] = motor.getOutputCurrent();
		bin_values[3] = motor.getOutputVoltage();
		return bin_values;
	}
}