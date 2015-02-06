package org.usfirst.frc.team1504.robot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;

public class Elevator { //thread
	DigitalInput hallSensor;
	DoubleSolenoid elevatorSolenoid;
	Servo servo_1;
	Servo servo_2;
	CANTalon elevatorMotor;
	//InterruptHandlerFunction<Double> handler;
	//Counter hallCounter;
//	HallHandlerClass handler;
	public Elevator() {
//		handler = new HallHandlerClass();
		hallSensor = new DigitalInput(Map.ELEVATOR_DIGITAL_INPUT_PORT);
		//hallCounter = new Counter(hallSensor);
		elevatorSolenoid = new DoubleSolenoid(Map.ELEVATOR_SOLENOID_FORWARD_PORT, Map.ELEVATOR_SOLENOID_REVERSE_PORT);
		//hallCounter.setUpDownCounterMode();
		//hallCounter.setUpSource(hallSensor);///// may neeed to goooo befoooore.
		servo_1 = new Servo(Map.ELEVATOR_SERVO_ONE_PORT);
		servo_2 = new Servo(Map.ELEVATOR_SERVO_TWO_PORT);
		elevatorMotor = new CANTalon(Map.ELEVATOR_TALON_PORT);
		hallSensor.enableInterrupts();
//		hallSensor.requestInterrupts(this.handler);
	}
	public void manual(double y)
	{
		elevatorMotor.set(y);
	}
//	public class HallHandlerClass extends InterruptHandlerFunction<Double>
//	{
//		void interruptFired(int i, Double param)
//		{
//			return;
//		}
//		Double overrideableParameter()
//		{
//			Double hi = new Double(2.2);
//			return hi;
//		}
//	}
	

}
