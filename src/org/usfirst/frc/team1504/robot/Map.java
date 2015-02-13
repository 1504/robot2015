package org.usfirst.frc.team1504.robot;

public class Map {
	// Talon Ports
	public static final int FRONT_LEFT_TALON_PORT = 10;
	public static final int BACK_LEFT_TALON_PORT = 11;
	public static final int BACK_RIGHT_TALON_PORT = 12;
	public static final int FRONT_RIGHT_TALON_PORT = 13;

	public static final int ALIGNER_TALON_PORT = 20;

	public static final int ELEVATOR_TALON_PORT = 30;

	public static final int BIN_CAPTURE_TALON_PORT = 40;

	// Joystick raw axes
	public static final int JOYSTICK_Y_AXIS = 1;
	public static final int JOYSTICK_X_AXIS = 0;

	public static final int COPTERSTICK_LEFT_Y_AXIS = 1;
	public static final int COPTERSTICK_LEFT_X_AXIS = 0;
	public static final int COPTERSTICK_RIGHT_Y_AXIS = 2;
	public static final int COPTERSTICK_RIGHT_X_AXIS = 3;

	// Primary Driver Buttons, Where ELEVATOR is the front of the robot by
	// default.
	public static final int[] FRONT_SIDE_BINCAP = {2,6};
	public static final int FRONT_SIDE_RIGHT = 5;
	public static final int[] FRONT_SIDE_ELEV = {3,11};
	public static final int FRONT_SIDE_LEFT = 4;

	// Tote Aligner Solenoids
	public static final int STAGE_ONE_SOLENOID_FORWARD_PORT = 6;
	public static final int STAGE_ONE_SOLENOID_REVERSE_PORT = 7;
	public static final int STAGE_TWO_SOLENOID_PORT = 5;

	// Elevator Solenoids
	public static final int ELEVATOR_SOLENOID_FORWARD_PORT = 3;
	public static final int ELEVATOR_SOLENOID_REVERSE_PORT = 4;

	// Bin Capture Solenoids
	public static final int EXTEND_SOLENOID_FORWARD_PORT = 0;
	public static final int EXTEND_SOLENOID_REVERSE_PORT = 1;
	public static final int CLAW_SOLENOID_PORT = 2;

	// Servos
	public static final int ELEVATOR_SERVO_ONE_PORT = 0;
	public static final int ELEVATOR_SERVO_TWO_PORT = 1;

	public static final int ELEVATOR_SERVO_1_OPEN_ANGLE = 0;
	public static final int ELEVATOR_SERVO_1_DOWN_ANGLE = 0;

	public static final int ELEVATOR_SERVO_2_OPEN_ANGLE = 0;
	public static final int ELEVATOR_SERVO_2_DOWN_ANGLE = 0;

	// setpoint down and up (random)
	public static final double SET_POINT_UP = 1.4;
	public static final double SET_POINT_DOWN = 0.6;

	// DigitalInput
	public static final int ELEVATOR_DIGITAL_INPUT_PORT = 0;
	public static final double JOYSTICK_DEAD_ZONE = .1;

	// Secondary Buttons
	public static final int BIN_CAPTURE_CLAW_TOGGLE_BUTTON = 3;
	public static final int BIN_CAPTURE_ARM_TOGGLE_BUTTON = 2;
	
	public static final int BIN_CAPTURE_MANUAL_TOGGLE_BUTTON = 8;
	public static final int ELEVATOR_MANUAL_TOGGLE_BUTTON = 1;
	public static final int[] ELEVATOR_CONTROL_BUTTONS = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }; // up,down, length = 10
	public static final int ELEVATOR_RETRACTED_MODE_BUTTON = 12;
	public static final int ELEVATOR_TOTE_MODE_BUTTON = 13;
	public static final int ELEVATOR_BIN_MODE_BUTTON = 14;

	// Aligner
	public static final int[] ALIGNER_STAGE = { 0, 0, 0 };// open, almost, closed

	// Elevator Mode values
	public static final int ELEVATOR_UP_SPEED = 1;
	public static final int ELEVATOR_DOWN_SPEED = -1;
	public static final int ELEVATOR_NONE_SPEED = 0;

}
