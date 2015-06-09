package org.usfirst.frc.team1504.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.DriverStation;

public class IO extends Loggable {

	static double forward;
	static double right;
	static double rotate;

	static DigitalInput autoninput_1;
	static DigitalInput autoninput_2;
	static DigitalInput autoninput_3;
	
	boolean is_mouse_enabled;

	I2C arduino;
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
	static Joystick copterstick = new Joystick(4);

	public IO() {

		arduino = new I2C(Port.kOnboard, 2);
		loopcount = 0;

		autoninput_1 = new DigitalInput(2);
		autoninput_2 = new DigitalInput(3);
		autoninput_3 = new DigitalInput(4);
		
		is_mouse_enabled = false;

		
		buffer = new byte[2];
		arduinoOutput = new byte[6];
	}
	
	public static int get_auton_mode()
	{
		int i = 0;
		if (!autoninput_1.get() && autoninput_2.get() && autoninput_3.get())
		{
			i = 0;
		}
		if (autoninput_1.get() && !autoninput_2.get() && autoninput_3.get())
		{
			i = 1;
		}
		if (autoninput_1.get() && autoninput_2.get() && !autoninput_3.get())
		{
			i = 2;
		}
		if (!autoninput_1.get() && !autoninput_2.get() && autoninput_3.get())
		{
			i = 3;
		}
		if (!autoninput_1.get() && autoninput_2.get() && !autoninput_3.get())
		{
			i = 4;
		}
		return 3;	
		//return i;
	}
	
	public static double[] mecanum_input() {
		double[] dircns = new double[3];

		double sticks_y = Math.pow(leftstick.getRawAxis(Map.JOYSTICK_Y_AXIS), 2) * Math.signum(leftstick.getRawAxis(Map.JOYSTICK_Y_AXIS));// y
		double sticks_x = -1.0 * Math.pow(leftstick.getRawAxis(Map.JOYSTICK_X_AXIS), 2) * Math.signum(leftstick.getRawAxis(Map.JOYSTICK_X_AXIS));// x
		double sticks_w = .7 * Math.pow(rightstick.getRawAxis(Map.JOYSTICK_X_AXIS), 2) * Math.signum(rightstick.getRawAxis(Map.JOYSTICK_X_AXIS));// w
		
		double copterstick_y = Math.pow(leftstick.getRawAxis(Map.COPTERSTICK_LEFT_Y_AXIS), 2) * Math.signum(leftstick.getRawAxis(Map.COPTERSTICK_LEFT_Y_AXIS));// y
		double copterstick_x = -1.0 * Math.pow(leftstick.getRawAxis(Map.COPTERSTICK_LEFT_X_AXIS), 2) * Math.signum(leftstick.getRawAxis(Map.COPTERSTICK_LEFT_X_AXIS));// x
		double copterstick_w = .7 * Math.pow(rightstick.getRawAxis(Map.COPTERSTICK_RIGHT_X_AXIS), 2) * Math.signum(rightstick.getRawAxis(Map.COPTERSTICK_RIGHT_X_AXIS));// w
		
		dircns[0] = Math.max(sticks_y, copterstick_y);
		dircns[1] = Math.max(sticks_x, copterstick_x);
		dircns[2] = Math.max(sticks_w, copterstick_w);

		// dircns[0] = copterstick.getRawAxis(Map.JOYSTICK_Y_VALUE);
		// dircns[1] = copterstick.getRawAxis(Map.JOYSTICK_X_VALUE);
		// dircns[2] = copterstick.getRawAxis(Map.JOYSTICK_X_VALUE);

		for (int i = 0; i < dircns.length - 1; i++) {
			dircns[i] = Utils.deadzone(dircns[i]);
		}
		return dircns;
	}
	

	public static boolean osc_toggle() {
		return (leftstick.getRawButton(Map.DRIVE_OSC_BUTTON[0]) || secondary.getRawButton(Map.DRIVE_OSC_BUTTON[1]));
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
	
	public static boolean orbit_point_toggle(){
		return rightstick.getRawButton(Map.ORBIT_POINT_TOGGLE);
	}
	
	public static boolean gain_toggle(){
		return rightstick.getRawButton(Map.GAIN_LIMIT_TOGGLE);
	}

	public static Elevator.ForkMode elevator_mode() { //change to enums
		Elevator.ForkMode mode = Elevator.ForkMode.NULL;
		if (secondary.getRawButton(Map.ELEVATOR_RETRACTED_MODE_BUTTON)) {
			mode = Elevator.ForkMode.retracted;
		} else if (secondary.getRawButton(Map.ELEVATOR_TOTE_MODE_BUTTON)) {
			mode = Elevator.ForkMode.toteMode;
		} else if (secondary.getRawButton(Map.ELEVATOR_BIN_MODE_BUTTON)) {
			mode = Elevator.ForkMode.binMode;
		}
		
		return mode;
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
		boolean[] stuff = new boolean[2];
		for (int i = 0; i < stuff.length; i++) {
			stuff[i] = secondary.getRawButton(Map.ALIGNER_STAGE[i]);
		}
		return stuff;
	}
	
	public static boolean fork_flapper_override() {
		return secondary.getRawButton(Map.ELEVATOR_FLAPPER_OVERRIDE_BUTTON);
	}
	
	public static double[] mouse_values()
	{
		double[] output = {0.0, 0.0, 0.0};
		return output;
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
			int elevmode = 0;//elevator_mode();
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
					
					arduino.writeBulk(buffer);
					
					arduino.read(0, 6, arduinoOutput); // 0-2: x,y,SQUAL of left
														// sensor; 3-5:
														// x,y,SQUAL of right
														// sensor;
				}
			}
		}

		public void stopmouse() {
			isRunning = false;

		}
	}

	public double[] dump() {
		double[] io_inputs = new double[32];

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

		for (int i = 26; i < 2 + 26; i++) {
			io_inputs[i] = Utils.boolconverter(secondary.getRawButton(Map.ALIGNER_STAGE[i - 26]));
		}
		io_inputs[28] = loopcount;
		io_inputs[29] = System.currentTimeMillis() - starttime;
		
		io_inputs[30] = Utils.boolconverter(osc_toggle());
		
		loopcount = 0;
		
		return io_inputs;
	}
	
	public String dumpFormat()
	{
		
		return null;
	}
}
