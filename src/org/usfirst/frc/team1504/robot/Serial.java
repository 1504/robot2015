package org.usfirst.frc.team1504.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;

public class Serial extends Loggable implements SerialPortEventListener {
	SerialPort serialPort;
	// The port we're normally going to use.
	private static final String portName = "/dev/ttyS0"; // Internal
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	private InputStream in;
	private OutputStream out;

	CommPortIdentifier portIdentifier;
	CommPort commPort;
	
	public double[] dump() {
		double[] number = new double[1];
		number[0] = 0.0;
		return number;
	}

	public Serial() {
		initialize();
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyS0");
		} catch (NoSuchPortException e) {}
		 if( portIdentifier.isCurrentlyOwned() ) {
		      System.out.println( "Error: Port is currently in use" );
		    } else {
		    	commPort = portIdentifier.open( this.getClass().getName(), timeout );
		    }
	}

	public void initialize() {

	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

}