package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class BinCapture extends Loggable //thread
{
	private static BinCaptureThread BinCap;

	CANTalon motor;

	DoubleSolenoid arm;
	Solenoid claw;

	//retracted = 0; extended = 1
	double armstate;
	double clawstate;

	boolean armtoggle;
	boolean clawtoggle;

	boolean isManual;
	public BinCapture() {

		BinCap = new BinCaptureThread();

		motor = new CANTalon(Map.BIN_CAPTURE_TALON_PORT);

		arm = new DoubleSolenoid(Map.EXTEND_SOLENOID_FORWARD_PORT, Map.EXTEND_SOLENOID_REVERSE_PORT);
		claw = new Solenoid(Map.CLAW_SOLENOID_PORT);

		armtoggle = false;
		clawtoggle = false;
		
		isManual = false;
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
		if (claw.get() == true && !clawtoggle) {
			claw.set(false);
			clawtoggle = true;
			clawstate=0;
		}
		if (claw.get() == false && !clawtoggle) {
			claw.set(true);
			clawtoggle = true;
			clawstate=1;
		}
	}
	private class BinCaptureThread extends Thread {
		protected boolean run = true;
		public void start() {
			while (run) {
				isManual = IO.bincap_manual_toggle();
				if (isManual) {
					if (IO.bincap_manual_toggle()) {
						manual(IO.bincap_manual());
					} else {
						manual(0.0);
					}

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
}
		private void manual(double y) {
			motor.set(y);
		}

	public double[] dump() {
		double[] bin_values = new double[5];
		bin_values[0] = armstate;
		bin_values[1] = clawstate;
		bin_values[2] = motor.getSpeed();
		bin_values[3] = motor.getOutputCurrent();
		bin_values[4] = motor.getOutputVoltage();
		
		return bin_values;
	}
}
