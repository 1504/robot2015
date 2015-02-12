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
				dircns = IO.mecanum_input(); //get y, x w
				
				dircns = detents(dircns); //manipulate
				
				outputCompute(dircns);//calculate for motors
				
				motorOutput();//set
			}
		}

		public void stopMecanum() {
			isRunning = false;
		}

	}
	 protected double[] detents(double[] dircn)
	    {

	        double theta = Math.atan2(dircn[0], dircn[1]);

	        double dx = correct_x(theta) * distance(dircn[1], dircn[0]) * 0.25;
	        double dy = correct_y(theta) * distance(dircn[1], dircn[0]) * 0.25;

	        double[] detented = new double[3];
	        
	        detented[0] = dircn[0] + dy; //y
	        detented[1] = dircn[1] + dx; //x
	        detented[2] = dircn[2];//angular
	        
	        return detented;
	    }
		private double correct_x(double theta){return -Math.sin(theta) * (-Math.sin(8*theta) - 0.25 * Math.sin(4*theta));}
		private double correct_y(double theta){return Math.cos(theta) * (-Math.sin(8*theta) - 0.25 * Math.sin(4*theta));}
		public static double distance(double x, double y){return Math.sqrt(x*x + y*y);}

}
