package org.usfirst.frc.team1504.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

	// OperatingSystemMXBean sysMX;
	// private static LoggerThreadClass loggerThread;

	Compressor compressor;
	BuiltInAccelerometer accelerometer;
	PowerDistributionPanel pdp;
	DriverStation driverstation = DriverStation.getInstance();
	FileOutputStream fileStream;
	Timer time;
	Task task;

	Timer timer;
	Calendar calendar;
	boolean isEnabled;
	Loggable[] classes;

	public Logger(Loggable[] classes) {
		compressor = new Compressor();
		accelerometer = new BuiltInAccelerometer();
		pdp = new PowerDistributionPanel();

		// loggerThread = new LoggerThreadClass();
		calendar = new GregorianCalendar();
		timer = new Timer();
		this.classes = classes;
		// sysMX = /*(OperatingSystemMXBean)*/
		// ManagementFactory.getOperatingSystemMXBean();
	}

	public void start(String prefix) {
		File outfile = new File("/home/lvuser/log/" + prefix + "-" + date() + ".log");

		try {
			fileStream = new FileOutputStream(outfile);
		} catch (FileNotFoundException e1) {
			System.out.println("Could not open file.");
			e1.printStackTrace();
		}

		enable();
	}

	public void stop() throws java.lang.NullPointerException {
		disable();
		try {
			// disable();
			if (fileStream == null) {
				System.out.println("disable called on null");
			} else
				fileStream.close();// TODO: check to see if we should close
									// before
		} catch (IOException e) {
			System.out.println("I can't stop! Help!");
		}
	}

	public void enable() {
		// loggerThread.start(); // TODO: check thread state so that enable
		// doesn't get called twice
		time = new Timer();
		task = new Task();
		time.scheduleAtFixedRate(task, 0, Map.TICK_INTERVAL);

	}

	public void disable() {
		task.cancel();
		time.cancel();

		// loggerThread.stop();
	}

	// public double getCPUPercent()
	// {
	// // return
	// ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()/2;
	// //return sysMX.getProcessCPULoad();
	// }

	public void write() {
		// AlignerDump(6)
		// bincapture(7)
		// Drive(14)
		// Elevator(9)
		// IO Dump(31)

		byte[] chirp = {67, 72, 73, 82, 80};
		byte[] chirpbreak = {61, 94, 61};
		
		try {
			fileStream.write(chirp);
			fileStream.write(doubleToByte(driverstation.getMatchTime()));
			fileStream.write(chirpbreak);
			fileStream.write(doubleToByte(pdp.getTotalCurrent()));
			fileStream.write(chirpbreak);
			fileStream.write(doubleToByte(driverstation.getBatteryVoltage()));
			fileStream.write(chirpbreak);
			fileStream.write(boolToByte(compressor.getPressureSwitchValue()));
			fileStream.write(chirpbreak);
			fileStream.write(floatToByte(compressor.getCompressorCurrent()));
			fileStream.write(chirpbreak);
			fileStream.write(doubleToByte(accelerometer.getX()));
			fileStream.write(chirpbreak);
			fileStream.write(doubleToByte(accelerometer.getY()));
			fileStream.write(chirpbreak);
			fileStream.write(doubleToByte(accelerometer.getZ()));
			fileStream.write(chirpbreak);

		} catch (IOException e1) {
			System.out.println(e1.getStackTrace());
			System.out.println("Unable to write the right thing. - Logger methods");
		}

		for (Loggable o : classes) {
			for (double d : o.dump()) {
				try {
					// System.out.println(d);
					fileStream.write(doubleToByte(d));
				} catch (IOException e) {
				//	System.out.println(e.);
					System.out.println(e.getStackTrace());
					System.out.println("Unable to write the right thing - loggable class methods in " + o.getClass().toString());
				}
			}
		}
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

	private byte[] boolToByte(boolean b) {
		byte[] bool = { 0 };
		if (b)
			bool[0] = 1;
		return bool;
	}

	public String date() {
		return calendar.getTime().toString();
	}

	// private class LoggerThreadClass extends Thread {
	// protected boolean isRunning = true;
	// Timer time = new Timer();
	// Task task;// = new Task();
	//
	//
	// public void canceltimer() {
	// time.cancel();
	// }
	//
	class Task extends TimerTask {
		public void run() {
			write();
//			System.out.println("tick");

		}

	}

	// }

}
