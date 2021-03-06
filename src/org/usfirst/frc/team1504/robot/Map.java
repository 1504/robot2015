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

	// Bin Capture Solenoids
	public static final int EXTEND_SOLENOID_FORWARD_PORT = 0;
	public static final int EXTEND_SOLENOID_REVERSE_PORT = 1;
	public static final int CLAW_SOLENOID_PORT = 2;

	// Elevator Solenoids
	public static final int ELEVATOR_SOLENOID_FORWARD_PORT = 3;
	public static final int ELEVATOR_SOLENOID_REVERSE_PORT = 4;
	public static final int ELEVATOR_FLAPPER_SOLENOID_PORT = 5;

	// Tote Aligner Solenoids
	public static final int STAGE_ONE_SOLENOID_FORWARD_PORT = 6;
	public static final int STAGE_ONE_SOLENOID_REVERSE_PORT = 7;

	public static final int RELAY_PORT = 0; // Relay

	public static final int LIMITSWITCHUP_PORT = 7;
	public static final int LIMITSWITCHDOWN_PORT = 8;

	// Servos
	public static final int ELEVATOR_SERVO_LEFT_PORT = 0;// left
	public static final int ELEVATOR_SERVO_RIGHT_PORT = 1;// right

	public static final int ELEVATOR_SERVO_LEFT_OPEN_ANGLE = 10;
	public static final int ELEVATOR_SERVO_LEFT_DOWN_ANGLE = 180;

	public static final int ELEVATOR_SERVO_RIGHT_OPEN_ANGLE = 170;
	public static final int ELEVATOR_SERVO_RIGHT_DOWN_ANGLE = 0;

	// Setpoint down and up (random)
	public static final double SET_POINT_UP = 1.4;
	public static final double SET_POINT_DOWN = 0.6;

	// DigitalInput
	public static final int ELEVATOR_DIGITAL_INPUT_PORT = 9;
	public static final double JOYSTICK_DEAD_ZONE = .1;

	// Elevator Mode values
	public static final int ELEVATOR_UP_SPEED = -1;
	public static final int ELEVATOR_DOWN_SPEED = 1;
	public static final int ELEVATOR_NONE_SPEED = 0;

	// Primary Driver Buttons, Where ELEVATOR is the front of the robot by
	// default.
	public static final int[] FRONT_SIDE_BINCAP = { 2, 6 };
	public static final int FRONT_SIDE_RIGHT = 5;
	public static final int[] FRONT_SIDE_ELEV = { 3, 11 };
	public static final int FRONT_SIDE_LEFT = 4;
	public static final int[] DRIVE_OSC_BUTTON = { 9, 4 };
	public static final int GAIN_LIMIT_TOGGLE = 1;
	public static final int ORBIT_POINT_TOGGLE = 8;
	
	// Secondary Buttons
	public static final int BIN_CAPTURE_CLAW_TOGGLE_BUTTON = 5;
	public static final int BIN_CAPTURE_ARM_TOGGLE_BUTTON = 3;
	public static final int BIN_CAPTURE_MANUAL_TOGGLE_BUTTON = 2;

	public static final int ELEVATOR_MANUAL_TOGGLE_BUTTON = 1;
	public static final int[] ELEVATOR_CONTROL_BUTTONS = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }; // array.length
																								// =
																								// 10

	public static final int ELEVATOR_RETRACTED_MODE_BUTTON = 11;
	public static final int ELEVATOR_TOTE_MODE_BUTTON = 9;
	public static final int ELEVATOR_BIN_MODE_BUTTON = 7;

	public static final int[] ALIGNER_STAGE = { 12, 10 }; // aligner --
															// retracted,
															// extended
	public static final int ELEVATOR_FLAPPER_OVERRIDE_BUTTON = 8;
	public static final int ELEVATOR_RETRACTION_TIMEOUT = 1000;
	public static final double ELEVATOR_OVERCURRENT_LIMIT = 75.0;
	public static final int ELEVATOR_OVERCURRENT_DETECTION_TIME = 100;
	public static final int ELEVATOR_OVERCURRENT_TIMEOUT = 600;
	
	public static final double[][] DRIVE_GAIN = {{0.001, 0.001, 0.001}, {0.01, 0.01, 0.01}}; //first array acceleration, second de-acceleration
												// 1 second to accelerate, .1 to decelrate
	
	// Tote wiggle values
	public static final long DRIVE_OSC_TIME = 100;
	public static final double[] DRIVE_OSC_INTENSITY = { 0.0, .25, .3 };

	// Drive class magic numbers
	public static final double[] DRIVE_OUTPUT_MAGIC_NUMBERS = { -1.0, -1.0, 1.0, 1.0 };
	public static final double[] DRIVE_GROUNDSPEED_MULTIPLIERS = { 1.0, 1.0, 1.0 };
	public static final long TICK_INTERVAL = 20;
}
