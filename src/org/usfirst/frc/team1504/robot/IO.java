package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class IO extends Loggable {

	static double forward;
	static double right;
	static double rotate;

	boolean is_mouse_enabled;

	SerialPort arduino;
	Port kOnboard;
	byte[] buffer;
	byte[] arduinoOutput;
	boolean[] buttons;
	boolean isElevator;

	static int elevatorMode;

	// Dual Stick
	static Joystick leftstick = new Joystick(0);
	static Joystick rightstick = new Joystick(1);

	// Secondary Driver
	static Joystick secondary = new Joystick(2);

	// Quadcopter
	static Joystick copterstick = new Joystick(0);

	public IO() {

		// arduino = new SerialPort(9600, kOnboard);

		is_mouse_enabled = false;

		buffer = new byte[2];
		arduinoOutput = new byte[9];
	}

	public static double[] mecanum_input() {
		double[] dircns = new double[3];

		dircns[0] = leftstick.getRawAxis(Map.JOYSTICK_LEFT_Y_VALUE);
		dircns[1] = leftstick.getRawAxis(Map.JOYSTICK_LEFT_X_VALUE);
		dircns[2] = rightstick.getRawAxis(Map.JOYSTICK_RIGHT_X_VALUE);

		// dircns[0] = copterstick.getRawAxis(Map.JOYSTICK_LEFT_Y_VALUE);
		// dircns[1] = copterstick.getRawAxis(Map.JOYSTICK_LEFT_X_VALUE);
		// dircns[2] = copterstick.getRawAxis(Map.JOYSTICK_RIGHT_X_VALUE);

		for (int i = 0; i < dircns.length - 1; i++) {
			if (Math.abs(dircns[i]) < Map.JOYSTICK_DEAD_ZONE)
				dircns[i] = 0.0;
		}

		return dircns;
	}

	public static boolean[] bincapture_input() {
		boolean buttons[] = new boolean[2];
		buttons[0] = secondary.getRawButton(Map.BIN_CAPTURE_ARM_TOGGLE_BUTTON);
		buttons[1] = secondary.getRawButton(Map.BIN_CAPTURE_CLAW_TOGGLE_BUTTON);
		return buttons;
	}

	public static double elevator_manual() {
		return secondary.getY();
	}

	public static boolean elevator_manual_toggle() {
		// return secondary.getRawButton(Map.ELEVATOR_MANUAL_TOGGLE_BUTTON);
		return true;
	}

	public void startmouse() {
		IOThread thread = new IOThread();
		thread.start();
		is_mouse_enabled = true;
	}

	public static int elevator_mode() {
		if (secondary.getRawButton(Map.ELEVATOR_RETRACTED_MODE_BUTTON)) {
			elevatorMode = 0;
		} else if (secondary.getRawButton(Map.ELEVATOR_TOTE_MODE_BUTTON)) {
			elevatorMode = 1;
		} else if (secondary.getRawButton(Map.ELEVATOR_BIN_MODE_BUTTON)) {
			elevatorMode = 2;
		}
		return elevatorMode;
	}

	public static boolean[] buttonValues() {
		boolean[] button = new boolean[Map.ELEVATOR_CONTROL_BUTTONS.length];

		for (int i = 0; i < Map.ELEVATOR_CONTROL_BUTTONS.length; i++) {
			button[i] = secondary.getRawButton(Map.ELEVATOR_CONTROL_BUTTONS[i]);
		}
		return button;
	}

	private class IOThread extends Thread {
		protected boolean isRunning = true;

		public void run() {
			while (isRunning) {
				buffer[0] = 0;
				buffer[1] = 1;
				arduino.write(buffer, 2);
				arduinoOutput = arduino.read(9);

				/*
				 * arduinoOutput bytes: 0-2: x, y, SQUAL of left sensor 3-5: x,
				 * y, SQUAL of right sensor 6-8: x, y, z, from magnetometer
				 */

			}
		}

		public void stopmouse() {
			isRunning = false;

		}
	}

	public double[] dump() {
		double[] relevant_inputs = new double[3];

		relevant_inputs[0] = leftstick.getRawAxis(Map.JOYSTICK_LEFT_Y_VALUE);
		relevant_inputs[1] = leftstick.getRawAxis(Map.JOYSTICK_LEFT_X_VALUE);
		relevant_inputs[2] = rightstick.getRawAxis(Map.JOYSTICK_LEFT_X_VALUE);

		return relevant_inputs;
	}

}
