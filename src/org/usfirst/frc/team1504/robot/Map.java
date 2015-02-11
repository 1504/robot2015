package org.usfirst.frc.team1504.robot;	

public class Map 
{
	// Talon Ports
	public static final int FRONT_LEFT_TALON_PORT = 10;
	public static final int BACK_LEFT_TALON_PORT = 11;
	public static final int BACK_RIGHT_TALON_PORT = 12;
	public static final int FRONT_RIGHT_TALON_PORT = 13;
	
	public static final int ALIGNER_TALON_PORT = 20;
	
	public static final int ELEVATOR_TALON_PORT = 30;
	
	public static final int BIN_CAPTURE_TALON_PORT = 40;

	// Joystick raw axes
	public static final int JOYSTICK_LEFT_Y_VALUE= 1;
	public static final int JOYSTICK_LEFT_X_VALUE= 0;
	public static final int JOYSTICK_RIGHT_Y_VALUE= 1;
	public static final int JOYSTICK_RIGHT_X_VALUE= 0;
	
	// TODO: Copterstick axis vals

	// Tote Aligner Solenoids
	public static final int STAGE_ONE_SOLENOID_FORWARD_PORT = 0;
	public static final int STAGE_ONE_SOLENOID_REVERSE_PORT = 1;
	public static final int STAGE_TWO_SOLENOID_FORWARD_PORT = 2;
	public static final int STAGE_TWO_SOLENOID_REVERSE_PORT = 3;
	
	// Elevator Solenoids
	public static final int ELEVATOR_SOLENOID_FORWARD_PORT = 4;
	public static final int ELEVATOR_SOLENOID_REVERSE_PORT = 5;
	
	// Tote Grabber Solenoids
	public static final int EXTEND_SOLENOID_FORWARD_PORT = 6;
	public static final int EXTEND_SOLENOID_REVERSE_PORT = 7;
	public static final int CLAW_SOLENOID_FORWARD_PORT = 8;
	public static final int CLAW_SOLENOID_REVERSE_PORT = 9;
	
	// Servos
	public static final int ELEVATOR_SERVO_ONE_PORT = 0; 
	public static final int ELEVATOR_SERVO_TWO_PORT = 1;
	
	// setpoint down and up (random)
	public static final double SET_POINT_UP = 1.4;
	public static final double SET_POINT_DOWN = 0.6;
	
	// DigitalInput
	public static final int ELEVATOR_DIGITAL_INPUT_PORT = 0;
	public static final double JOYSTICK_DEAD_ZONE = .15;
	
	// Secondary Buttons
	public static final int BIN_CAPTURE_CLAW_TOGGLE_BUTTON = 2;
	public static final int BIN_CAPTURE_ARM_TOGGLE_BUTTON = 3;
	public static final int ELEVATOR_MANUAL_TOGGLE_BUTTON = 1;
	public static final int[] ELEVATOR_CONTROL_BUTTONS = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}; //up,down
	public static final int ELEVATOR_RETRACTED_MODE_BUTTON = 12;
	public static final int ELEVATOR_TOTE_MODE_BUTTON = 13;
	public static final int ELEVATOR_BIN_MODE_BUTTON = 14;
}
