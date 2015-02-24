package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class BinCapture extends Loggable // thread
{
	private static BinCaptureThread BinCap;

	CANTalon motor;

	DoubleSolenoid arm;
	Solenoid claw;

	DigitalInput limitswitchup;
	DigitalInput limitswitchdown;

	Relay photonCannon;

	// retracted = 0; extended = 1
	boolean armstate;
	double clawstate;

	boolean armtoggle;
	boolean clawtoggle;

	int loopcount;
	long starttime;

	boolean isManual;

	public BinCapture() {

		BinCap = new BinCaptureThread();

		motor = new CANTalon(Map.BIN_CAPTURE_TALON_PORT);

		arm = new DoubleSolenoid(Map.EXTEND_SOLENOID_FORWARD_PORT, Map.EXTEND_SOLENOID_REVERSE_PORT);
		arm.set(DoubleSolenoid.Value.kForward);

		claw = new Solenoid(Map.CLAW_SOLENOID_PORT);

		photonCannon = new Relay(Map.RELAY_PORT, Relay.Direction.kForward);
		photonCannon.set(Relay.Value.kOff);

		limitswitchup=new DigitalInput(Map.LIMITSWITCHUP_PORT);
		limitswitchdown=new DigitalInput(Map.LIMITSWITCHDOWN_PORT);
		
		armtoggle = false;
		clawtoggle = false;

		loopcount = 0;

		isManual = false;
		
		armstate = true;
	}

	public void start() {
		BinCap.start();
	}

	public void extend() {
		if (arm.get() == DoubleSolenoid.Value.kForward && !armtoggle) {
			armtoggle = true;
			arm.set(DoubleSolenoid.Value.kReverse);
			photonCannon.set(Relay.Value.kOn);
			armstate = false;
		}
		if (arm.get() == DoubleSolenoid.Value.kReverse && !armtoggle) {
			armtoggle = true;
			arm.set(DoubleSolenoid.Value.kForward);
			photonCannon.set(Relay.Value.kOff);
			armstate = true;
		}

	}

	public void grab() {
		if (claw.get() == true && !clawtoggle) {
			claw.set(false);
			clawtoggle = true;
			clawstate = 0;
		}
		if (claw.get() == false && !clawtoggle) {
			claw.set(true);
			clawtoggle = true;
			clawstate = 1;
		}
	}

	private class BinCaptureThread extends Thread {
		protected boolean running = true;

		public void run() {

			starttime = System.currentTimeMillis();

			while (running) {
				if (loopcount == 0) {
					starttime = System.currentTimeMillis();
				}
				loopcount++;

				isManual = IO.bincap_manual_toggle() || (isManual && !IO.bincapture_input()[0]);

				if (isManual) {
					if (IO.bincap_manual_toggle()) {
						manual(IO.bincap_manual());
					}
				} else {
					if (!armstate && !limitswitchdown.get())
						motor.set(-1);
					else if (armstate && !limitswitchup.get())
						motor.set(1);
					else
						motor.set(0);
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

	private void manual(double y) {
		motor.set(y);
	}

	public double[] dump() {
		double[] bin_values = new double[7];
		bin_values[0] = Utils.boolconverter(armstate);
		bin_values[1] = clawstate;
		bin_values[2] = motor.getSpeed();
		bin_values[3] = motor.getOutputCurrent();
		bin_values[4] = motor.getOutputVoltage();
		bin_values[5] = loopcount;
		bin_values[6] = System.currentTimeMillis() - starttime;

		loopcount = 0;

		return bin_values;
	}
}
