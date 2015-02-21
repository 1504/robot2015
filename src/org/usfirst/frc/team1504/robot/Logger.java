package org.usfirst.frc.team1504.robot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import java.util.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.DriverStation;

public class Logger {
	
//	OperatingSystemMXBean sysMX;
	private static LoggerThreadClass loggerThread;

	Compressor compressor;
	BuiltInAccelerometer accelerometer;
	PowerDistributionPanel pdp;
	DriverStation driverstation;
	
	Timer timer;
	Calendar calendar;
	FileOutputStream logStream;
	boolean isEnabled;
	Loggable[] classes;

	public Logger(Loggable[] classes) {
		compressor = new Compressor();
		accelerometer = new BuiltInAccelerometer();
		pdp = new PowerDistributionPanel();
		
		loggerThread = new LoggerThreadClass();
		calendar = new GregorianCalendar();
		timer = new Timer();
		this.classes = classes;
//		sysMX = /*(OperatingSystemMXBean)*/ ManagementFactory.getOperatingSystemMXBean();
	}

	public void start(String prefix) {
		try {
			logStream = new FileOutputStream("/home/lvuser/log/" + prefix + "-" + date());
		} catch (FileNotFoundException e) {
			System.out.println("Cannot write to file " + date());
		}
//		enable();
	}

	public void stop() throws java.lang.NullPointerException {
//		disable();
		try {
//			disable();
			logStream.close();//TODO: check to see if we should close before disable
		} catch (IOException e) {
			System.out.println("I can't stop! Help!");
		}
	}

	public void enable() {
		loggerThread.start(); // TODO: check thread state so that enable doesn't get called twice
	}

	public void disable() {
		loggerThread.canceltimer();
		loggerThread.stop();
	}
	
//	public double getCPUPercent()
//	{
////		return ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()/2;
//		//return sysMX.getProcessCPULoad();
//	}
	
	public void write() {
		// AlignerDump(6)
		// bincapture(7)
		// Drive(14)
		// Elevator(9)
		// IO Dump(31)
		
		try {
			logStream.write(doubleToByte(driverstation.getMatchTime()));
			logStream.write(doubleToByte(pdp.getTotalCurrent()));
			logStream.write(doubleToByte(driverstation.getBatteryVoltage()));
			logStream.write(boolToByte(compressor.getPressureSwitchValue()));
			logStream.write(floatToByte(compressor.getCompressorCurrent()));
			logStream.write(doubleToByte(accelerometer.getX()));
			logStream.write(doubleToByte(accelerometer.getY()));
			logStream.write(doubleToByte(accelerometer.getZ()));
			
		} catch (IOException e1) {
			System.out.println("Unable to write the right thing.");
		}
		
		for (Loggable o : classes) {
			for (double d : o.dump()) {
				try {
					logStream.write(doubleToByte(d));
				} catch (IOException e) {
					System.out.println("Unable to write the right thing.");
				}
			}
		}
		// logStream.write();
	}

	private byte[] doubleToByte(double d) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(d);
		return bytes;
	}

	private byte[] floatToByte(float f) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putFloat(f);
		return bytes;
	}
	
	private byte[] intToByte(int i) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putInt(i);
		return bytes;
	}
	
	private byte boolToByte(boolean b) {
		byte bool = 0;
		if (b)
			bool = 1;
		return bool;
	}

	public String date() {
		return calendar.getTime().toString();
	}

	private class LoggerThreadClass extends Thread {
		protected boolean isRunning = true;
		Timer time = new Timer();
		Task task;// = new Task();
		
		public void run() {
			Task task = new Task();
			time.scheduleAtFixedRate(task, 0, 1000);
		}

		public void canceltimer() {
			time.cancel();
		}

		class Task extends TimerTask {
			public void run() {
				//write();
				System.out.println("tick");
				
			}
			
		}

	}

}
