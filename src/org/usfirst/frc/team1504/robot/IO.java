package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.DriverStation;

public class IO extends Loggable {

	static double forward;
	static double right;
	static double rotate;

	boolean is_mouse_enabled;

	//SerialPort arduino;
	DriverStation driverstation = DriverStation.getInstance();
	Port kOnboard;
	byte[] buffer;
	byte[] arduinoOutput;
	boolean[] buttons;
	boolean isElevator;

	int loopcount;
	long starttime;

	static int elevatorMode;

	// Dual Stick
	static Joystick leftstick = new Joystick(0);
	static Joystick rightstick = new Joystick(1);

	// Secondary Driver
	static Joystick secondary = new Joystick(2);

	// Quadcopter
	static Joystick copterstick = new Joystick(0);

	public IO() {

		//arduino = new SerialPort(9600, SerialPort.Port.kOnboard);
		loopcount = 0;

		is_mouse_enabled = false;

		buffer = new byte[2];
		arduinoOutput = new byte[9];
	}

	public static double[] mecanum_input() {
		double[] dircns = new double[3];

		dircns[0] = Math.pow(leftstick.getRawAxis(Map.JOYSTICK_Y_AXIS), 2) * Math.signum(leftstick.getRawAxis(Map.JOYSTICK_Y_AXIS));// y
		dircns[1] = -1.0 * Math.pow(leftstick.getRawAxis(Map.JOYSTICK_X_AXIS), 2) * Math.signum(leftstick.getRawAxis(Map.JOYSTICK_X_AXIS));// x
		dircns[2] = .6 * Math.pow(rightstick.getRawAxis(Map.JOYSTICK_X_AXIS), 2) * Math.signum(rightstick.getRawAxis(Map.JOYSTICK_X_AXIS));// w

		// dircns[0] = copterstick.getRawAxis(Map.JOYSTICK_Y_VALUE);
		// dircns[1] = copterstick.getRawAxis(Map.JOYSTICK_X_VALUE);
		// dircns[2] = copterstick.getRawAxis(Map.JOYSTICK_X_VALUE);

		for (int i = 0; i < dircns.length - 1; i++) {
			dircns[i] = Utils.deadzone(dircns[i]);
		}
		return dircns;
	}

	public static boolean osc_toggle() {
		return leftstick.getRawButton(Map.DRIVE_OSC_BUTTON);
	}

	public static double front_side_check() {
		double side = -1.0;

		if (rightstick.getRawButton(Map.FRONT_SIDE_BINCAP[0]) || leftstick.getRawButton(Map.FRONT_SIDE_BINCAP[1])) {
			side = 180.0;
		} else if (rightstick.getRawButton(Map.FRONT_SIDE_RIGHT)) {
			side = 270.0;
		} else if (rightstick.getRawButton(Map.FRONT_SIDE_ELEV[0]) || rightstick.getRawButton(Map.FRONT_SIDE_ELEV[1])) {
			side = 0.0;
		} else if (rightstick.getRawButton(Map.FRONT_SIDE_LEFT)) {
			side = 90.0;
		}
		return side;
	}

	public static double elevator_manual() {
		return -1.0 * Math.pow(secondary.getRawAxis(Map.JOYSTICK_Y_AXIS), 2) * Math.signum(secondary.getRawAxis(Map.JOYSTICK_Y_AXIS));
	}

	public static boolean elevator_manual_toggle() {
		return secondary.getRawButton(Map.ELEVATOR_MANUAL_TOGGLE_BUTTON);
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

	public static boolean[] elevatorButtonValues() {
		boolean[] button = new boolean[Map.ELEVATOR_CONTROL_BUTTONS.length];

		for (int i = 0; i < Map.ELEVATOR_CONTROL_BUTTONS.length; i++) {
			button[i] = secondary.getRawButton(Map.ELEVATOR_CONTROL_BUTTONS[i]);
		}
		return button;
	}

	public static boolean bincap_manual_toggle() {
		return secondary.getRawButton(Map.BIN_CAPTURE_MANUAL_TOGGLE_BUTTON);
	}

	public static double bincap_manual() {
		return Math.pow(secondary.getRawAxis(Map.JOYSTICK_Y_AXIS), 2) * Math.signum(secondary.getRawAxis(Map.JOYSTICK_Y_AXIS));
	}

	public static boolean[] bincapture_input() {
		boolean buttons[] = new boolean[2];
		buttons[0] = secondary.getRawButton(Map.BIN_CAPTURE_ARM_TOGGLE_BUTTON);
		buttons[1] = secondary.getRawButton(Map.BIN_CAPTURE_CLAW_TOGGLE_BUTTON);
		return buttons;
	}

	public static boolean[] alignerButtons() {
		boolean[] stuff = new boolean[3];
		for (int i = 0; i < stuff.length; i++) {
			stuff[i] = secondary.getRawButton(Map.ALIGNER_STAGE[i]);
		}
		return stuff;
	}

	public void startmouse() {
		IOThread thread = new IOThread();
		thread.start();
		is_mouse_enabled = true;
	}

	public byte setBit(byte a, int pos) {
		a = (byte) (a | (1 << pos));
		return a;
	}

	private class IOThread extends Thread {
		protected boolean isRunning = true;

		public byte[] bitWrite() {
			byte[] infos = new byte[2];
			infos[0] = 0;
			infos[1] = 0;
			double[] dircns = mecanum_input();
			int elevmode = elevator_mode();
			int elevlvl = 0;
			double timeelapsed = 135;// - driverstation.getMatchTime();
			// Byte 1 Bit 7 - Alliance
			// 0 = Red Team
			// 1 = Blue Team
			if (driverstation.getAlliance() == DriverStation.Alliance.Blue) {
				infos[0] = setBit(infos[0], 7);
			}

			// Byte 1 Bit 6-5 - Station
			// 11 = Station 1
			// 10 = Station 2
			// 01 = Station 3
			switch (driverstation.getLocation()) {
			case 1: // Station 1
				infos[0] = setBit(infos[0], 6);
				infos[0] = setBit(infos[0], 5);
				break;
			case 2: // Station 2
				infos[0] = setBit(infos[0], 6);
				break;
			case 3: // Station 3
				infos[0] = setBit(infos[0], 5);
				break;
			default:
				break;
			}

			// Byte 1 Bit 4 - Game Mode
			// 1 = Auton
			// 0 = Teleop
			if (driverstation.isAutonomous())
				infos[0] = setBit(infos[0], 4);

			// Byte 1 Bit 3-2 - X-Direction
			// 11 = Right
			// 10 = Neutral
			// 01 = Left
			if (dircns[1] == 0) {
				infos[0] = setBit(infos[0], 3);
			} else if (dircns[1] > 0) {
				infos[0] = setBit(infos[0], 3);
				infos[0] = setBit(infos[0], 2);
			} else if (dircns[1] < 0) {
				infos[0] = setBit(infos[0], 2);
			}

			// Byte 1 Bit 1-0 - Y-Direction
			// 11 = Forward
			// 10 = Neutral
			// 01 = Backward
			if (dircns[0] == 0) {
				infos[0] = setBit(infos[0], 1);
			} else if (dircns[0] > 0) {
				infos[0] = setBit(infos[0], 1);
				infos[0] = setBit(infos[0], 0);
			} else if (dircns[0] < 0) {
				infos[0] = setBit(infos[0], 0);
			}

			// Byte 2 Bit 7-6 - Rotation
			// 10 = Neutral
			// 11 = CW
			// 01 = CCW
			if (dircns[2] == 0) {
				infos[1] = setBit(infos[0], 7);
			} else if (dircns[2] > 0) {
				infos[1] = setBit(infos[0], 7);
				infos[1] = setBit(infos[0], 6);
			} else if (dircns[2] < 0) {
				infos[1] = setBit(infos[0], 6);
			}

			// Byte 2 Bit 5-3 - Elevator Level
			// 100 = 0
			// 010 = 1
			// 011 = 2
			// 110 = 3
			// 111 = 4
			// 001 = 5
			// 000 = 6
			switch (elevmode) {
			case 0: // fork retracted
				infos[1] = setBit(infos[1], 5);
				break;
			case 1: // tote mode
				switch (elevlvl) {
				case 0:
					infos[1] = setBit(infos[1], 5);
					break;
				case 1:
					infos[1] = setBit(infos[1], 4);
					break;
				case 2:
					infos[1] = setBit(infos[1], 4);
					infos[1] = setBit(infos[1], 3);
					break;
				case 3:
					infos[1] = setBit(infos[1], 5);
					infos[1] = setBit(infos[1], 4);
					break;
				case 4:
					infos[1] = setBit(infos[1], 5);
					infos[1] = setBit(infos[1], 4);
					infos[1] = setBit(infos[1], 3);
					break;
				case 5:
					infos[1] = setBit(infos[1], 3);
					break;
				case 6:
					break;
				default:
					break;
				}
			case 2:// bin mode
				switch (elevlvl) {
				case 0:
					infos[1] = setBit(infos[1], 5);
					break;
				case 1:
					infos[1] = setBit(infos[1], 4);
					break;
				case 2:
					infos[1] = setBit(infos[1], 4);
					infos[1] = setBit(infos[1], 3);
					break;
				case 3:
					infos[1] = setBit(infos[1], 5);
					infos[1] = setBit(infos[1], 4);
					break;
				case 4:
					infos[1] = setBit(infos[1], 5);
					infos[1] = setBit(infos[1], 4);
					infos[1] = setBit(infos[1], 3);
					break;
				case 5:
					infos[1] = setBit(infos[1], 3);
					break;
				case 6:
					break;
				default:
					break;
				}
			default:
				break;
			}
			// Byte 2 Bit 2-0 - Current Time
			// 000 = 0
			// 010 = 15
			// 011 = 35
			// 110 = 55
			// 111 = 75
			// 001 = 90
			// 100 = 105
			// 101 = 120
			if (!driverstation.isAutonomous()) {
				if (timeelapsed == 15) {
					infos[1] = setBit(infos[1], 1);
				} else if (timeelapsed == 35) {
					infos[1] = setBit(infos[1], 1);
					infos[1] = setBit(infos[1], 0);
				} else if (timeelapsed == 55) {
					infos[1] = setBit(infos[1], 2);
					infos[1] = setBit(infos[1], 1);
				} else if (timeelapsed == 75) {
					infos[1] = setBit(infos[1], 2);
					infos[1] = setBit(infos[1], 1);
				} else if (timeelapsed == 90) {
					infos[1] = setBit(infos[1], 2);
					infos[1] = setBit(infos[1], 1);
					infos[1] = setBit(infos[1], 0);
				} else if (timeelapsed == 105) {
					infos[1] = setBit(infos[1], 2);
				} else if (timeelapsed == 120) {
					infos[1] = setBit(infos[1], 2);
					infos[1] = setBit(infos[1], 0);
				}
			}
			return infos;
		}

		public void run() {
			starttime = System.currentTimeMillis();
			while (isRunning) {
				while (driverstation.isAutonomous() || driverstation.isOperatorControl()) {
					if (loopcount == 0) {
						starttime = System.currentTimeMillis();
					}
					loopcount++;

					buffer = bitWrite();
					//arduino.write(buffer, 2);
					//arduinoOutput = arduino.read(9); // 0-2: x,y,SQUAL of left
														// sensor; 3-5:
														// x,y,SQUAL of right
														// sensor; 6-8: x,y,z
														// from magnetometer

					//System.out.println(arduino.getBytesReceived());
					
				}
			}
		}

		public void stopmouse() {
			isRunning = false;

		}
	}

	public double[] dump() {
		double[] io_inputs = new double[31];

		io_inputs[0] = leftstick.getRawAxis(Map.JOYSTICK_Y_AXIS);
		io_inputs[1] = leftstick.getRawAxis(Map.JOYSTICK_X_AXIS);
		io_inputs[2] = rightstick.getRawAxis(Map.JOYSTICK_X_AXIS);

		io_inputs[3] = Utils.boolconverter(rightstick.getRawButton(Map.FRONT_SIDE_BINCAP[0]));
		io_inputs[4] = Utils.boolconverter(leftstick.getRawButton(Map.FRONT_SIDE_BINCAP[1]));
		io_inputs[5] = Utils.boolconverter(rightstick.getRawButton(Map.FRONT_SIDE_RIGHT));
		io_inputs[6] = Utils.boolconverter(rightstick.getRawButton(Map.FRONT_SIDE_ELEV[0]));
		io_inputs[7] = Utils.boolconverter(rightstick.getRawButton(Map.FRONT_SIDE_ELEV[1]));
		io_inputs[8] = Utils.boolconverter(rightstick.getRawButton(Map.FRONT_SIDE_LEFT));

		io_inputs[9] = secondary.getRawAxis(Map.JOYSTICK_Y_AXIS);

		io_inputs[10] = Utils.boolconverter(secondary.getRawButton(Map.BIN_CAPTURE_ARM_TOGGLE_BUTTON));
		io_inputs[11] = Utils.boolconverter(secondary.getRawButton(Map.BIN_CAPTURE_CLAW_TOGGLE_BUTTON));

		io_inputs[12] = Utils.boolconverter(secondary.getRawButton(Map.ELEVATOR_MANUAL_TOGGLE_BUTTON));
		io_inputs[13] = Utils.boolconverter(secondary.getRawButton(Map.ELEVATOR_RETRACTED_MODE_BUTTON));
		io_inputs[14] = Utils.boolconverter(secondary.getRawButton(Map.ELEVATOR_TOTE_MODE_BUTTON));
		io_inputs[15] = Utils.boolconverter(secondary.getRawButton(Map.ELEVATOR_BIN_MODE_BUTTON));

		for (int i = 16; i < (Map.ELEVATOR_CONTROL_BUTTONS.length + 16); i++) {
			io_inputs[i] = Utils.boolconverter(secondary.getRawButton(Map.ELEVATOR_CONTROL_BUTTONS[i - 16]));
		}

		for (int i = 26; i < 3 + 26; i++) {
			io_inputs[i] = Utils.boolconverter(secondary.getRawButton(Map.ALIGNER_STAGE[i - 26]));
		}
		io_inputs[29] = loopcount;
		io_inputs[30] = System.currentTimeMillis() - starttime;
		loopcount = 0;
		return io_inputs;
	}

}
