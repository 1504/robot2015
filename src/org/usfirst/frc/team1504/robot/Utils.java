package org.usfirst.frc.team1504.robot;

public class Utils {

	public static double boolconverter(boolean bool) {
		double boolconverted = 0;
		if (bool) {
			boolconverted = 1;
		} else if (!bool) {
			boolconverted = 0;
		}
		return boolconverted;
	}
	public static double distance(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}
	
	public static double deadzone(double input) {
		if(Math.abs(input) < Map.JOYSTICK_DEAD_ZONE)
			input *= 0;
		input = input / (1.0 - Map.JOYSTICK_DEAD_ZONE) + (-1.0 * Map.JOYSTICK_DEAD_ZONE * Math.signum(input)); // Restore linearity
		return input;
	}
}
