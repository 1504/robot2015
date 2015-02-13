package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Drive extends Loggable {
	private static DriveThreadClass DriveThread;

	CANTalon frontleft;
	CANTalon backleft;
	CANTalon backright;
	CANTalon frontright;

	double frontleft_val;
	double backleft_val;
	double backright_val;
	double frontright_val;

	double rotation_offset;

	double[] dircns;

	public Drive() {
		DriveThread = new DriveThreadClass();

		frontleft = new CANTalon(Map.FRONT_LEFT_TALON_PORT);
		backleft = new CANTalon(Map.BACK_LEFT_TALON_PORT);
		backright = new CANTalon(Map.BACK_RIGHT_TALON_PORT);
		frontright = new CANTalon(Map.FRONT_RIGHT_TALON_PORT);

		dircns = new double[3];
	}

	public void start() {
		DriveThread.start();
		// DriveThread.run();
	}

	public void stop() {
		DriveThread.stopMecanum();
	}

	public void outputCompute(double[] input) {
		double max = Math.max(1.0, Math.abs(input[0]) + Math.abs(input[1]) + Math.abs(input[2]));

		frontleft_val = (input[0] + input[1] - input[2]) / max;
		frontright_val = (input[0] - input[1] + input[2]) / max;
		backleft_val = (input[0] - input[1] - input[2]) / max;
		backright_val = (input[0] + input[1] + input[2]) / max;
	}

	public void motorOutput() {
		frontleft.set(frontleft_val * -1);
		frontright.set(frontright_val);
		backleft.set(backleft_val * -1);
		backright.set(backright_val);
	}

	public double[] dump() {
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

	private class DriveThreadClass extends Thread {
		protected boolean isRunning = true;

		public void run() {
			while (isRunning) {
				dircns = IO.mecanum_input(); // get y, x w

//				dircns = detents(dircns); // manipulate

//				set_front(IO.front_side_check());

//				dircns = front_side(dircns); // checks for pressed buttons;

				outputCompute(dircns);// calculate for motors

				motorOutput();// set
			}
		}

		public void stopMecanum() {
			isRunning = false;
		}

	}

	protected double[] detents(double[] dircn) {

		double theta = Math.atan2(dircn[0], dircn[1]);

		double dx = correct_x(theta) * Utils.distance(dircn[1], dircn[0]) * 0.25;
		double dy = correct_y(theta) * Utils.distance(dircn[1], dircn[0]) * 0.25;

		double[] detented = new double[3];

		detented[0] = dircn[0] + dy; // y
		detented[1] = dircn[1] + dx; // x
		detented[2] = dircn[2];// angular

		return detented;
	}

	public double[] front_side(double[] dircn) {
		double[] dir_offset = new double[3];
		dir_offset[0] = dircn[0] * Math.cos(rotation_offset) + dircn[1] * Math.sin(rotation_offset);
		dir_offset[1] = dircn[1] * Math.cos(rotation_offset) - dircn[0] * Math.sin(rotation_offset);
		dir_offset[2] = dircn[2];
		return dir_offset;
	}

	public double[] orbit_point(double[] dircn) {
		double x = 0.0;
		double y = 1.15;
		// TODO: IO method for, based on buttons, what the orbit point is (as it
		// may change for bincapture for easier grip)

		double[] k = { y - 1, y + 1, 1 - x, -1 - x };

		double p = Math.sqrt((k[0] * k[0] + k[2] * k[2]) / 2) * Math.cos((Math.PI / 4) + Math.atan2(k[0], k[2]));
		double r = Math.sqrt((k[1] * k[1] + k[2] * k[2]) / 2) * Math.cos(-(Math.PI / 4) + Math.atan2(k[1], k[2]));
		double q = -Math.sqrt((k[1] * k[1] + k[3] * k[3]) / 2) * Math.cos((Math.PI / 4) + Math.atan2(k[1], k[3]));

		double[] corrected = new double[3];
		corrected[0] = (dircn[2] * r + (dircn[0] - dircn[2]) * q + dircn[0] * p) / (q + p);
		corrected[1] = (-dircn[2] * r + dircn[1] * q - (-dircn[1] - dircn[2]) * p) / (q + p);
		corrected[2] = (2 * dircn[2]) / (q + p);
		return corrected;
	}

	public void set_front(double rot_offset) {
		rotation_offset = rot_offset * Math.PI / 180;
	}

	private double correct_x(double theta) {
		return -Math.sin(theta) * (-Math.sin(8 * theta) - 0.25 * Math.sin(4 * theta));
	}

	private double correct_y(double theta) {
		return Math.cos(theta) * (-Math.sin(8 * theta) - 0.25 * Math.sin(4 * theta));
	}

}
