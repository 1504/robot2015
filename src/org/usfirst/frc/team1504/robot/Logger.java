package org.usfirst.frc.team1504.robot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import java.util.Timer;

public class Logger {

	private static LoggerThreadClass loggerThread;

	Timer timer;
	Calendar calendar;
	FileOutputStream logStream;
	boolean isEnabled;
	Loggable[] classes;

	public Logger(Loggable[] classes) {
		loggerThread = new LoggerThreadClass();
		calendar = new GregorianCalendar();
		timer = new Timer();
		this.classes = classes;
	}

	public void start(String prefix) {
		try {
			logStream = new FileOutputStream("/home/lvuser/log/" + prefix + "-" + date());
		} catch (FileNotFoundException e) {
			System.out.println("Cannot write to file " + date());
		}
		enable();
	}

	public void stop() throws java.lang.NullPointerException {
//		disable();
		try {
			disable();
			logStream.close();
		} catch (IOException e) {
			System.out.println("I can't stop! Help!");
		}
	}

	public void enable() {
		loggerThread.start(); // TODO: check thread state so that enable doesn't get called twice
	}

	public void disable() {
		loggerThread.cancel();
	}

	public void write() {
		// AlignerDump(4)
		// bincapture(4)
		// Drive(12)
		// Elevator*
		// IO Dump(3)

		for (Loggable o : classes) {
			for (double d : o.dump()) {
				try {
					logStream.write(doubleToByte(d));
				} catch (IOException e) {
					System.out.println("Something faild. Srry.");
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

	private byte[] intToByte(int i) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putInt(i);
		return bytes;
	}

	public String date() {
		return calendar.getTime().toString();
	}

	private class LoggerThreadClass extends Thread {
		protected boolean isRunning = true;
		Timer time = new Timer();
		Task task = new Task();

		public void run() {
			time.scheduleAtFixedRate(task, 0, 1000);
		}

		public void cancel() {
			time.cancel();
		}

		class Task extends TimerTask {
			public void run() {
				// write();
				System.out.println("tick");
			}
		}

	}

}
