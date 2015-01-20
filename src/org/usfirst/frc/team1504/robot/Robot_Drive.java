package org.usfirst.frc.team1504.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;

public class Robot_Drive {
	Joystick leftstick;
	Joystick rightstick;
	
	//CanTalonSRX frontleft;
	//CanTalonSRX backleft;
	//CanTalonSRX backright;
	//CanTalonSRX frontright;
	
	double forward;
	double right;
	double rotate;
	double frontleft_val;
	double backleft_val;
	double backright_val;
	double frontright_val;
	double max;
	
	public Robot_Drive() {
		leftstick = new Joystick(0);
		rightstick = new Joystick(1);
		forward = leftstick.getY();
		right = leftstick.getX();
		rotate = rightstick.getX();
		//frontleft = new CanTalonSRX(3);
		//backleft = new CanTalonSRX(2);
		//backright = new CanTalonSRX(0);
		//frontright = new CanTalonSRX(1);
		
		//max = Math.max(1.0, Math.abs(forward) + Math.abs(right) + Math.abs(rotate));
		//frontleft_val = forward + right - rotate;
		//frontright_val = forward - right + rotate;
		//backleft_val = forward - right - rotate;
		//backright_val = forward + right + rotate;
		
		
		
	}

	/* public void mecanum() {
				
		frontleft.Set(frontleft_val);
		frontright.Set(frontright_val);
		backleft.Set(backleft_val);
		backright.Set(backright_val);
	} */

}
