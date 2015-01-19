package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;

public class Robot_Drive {
	Joystick leftstick;
	Joystick rightstick;
	
	CanTalonSRX frontleft;
	CanTalonSRX backleft;
	CanTalonSRX backright;
	CanTalonSRX frontright;
	
	double forward;
	double right;
	double rotate;
	
	public Robot_Drive() {
		leftstick = new Joystick(0);
		rightstick = new Joystick(1);
		forward = leftstick.getY();
		right = leftstick.getX();
		rotate = rightstick.getX();
		frontleft = new CanTalonSRX(3);
		backleft = new CanTalonSRX(2);
		backright = new CanTalonSRX(0);
		frontright = new CanTalonSRX(1);
		
	}

	public void mecanum(double y, double x, double w) {
		
		double max = Math.max(1.0, Math.abs(y) + Math.abs(x) + Math.abs(w));
		
		frontleft.Set((y + x - w)/max);
		frontright.Set((y - x + w)/max);
		backleft.Set((y - x - w)/max);
		backright.Set((y + x + w)/max);
	}

}
