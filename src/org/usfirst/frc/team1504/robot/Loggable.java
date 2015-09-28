package org.usfirst.frc.team1504.robot;

import java.nio.ByteBuffer;

public abstract class Loggable {

	public Loggable() {

	}

	public abstract double[] dump();
	
	private byte[] doubleToByte(double d) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(d);
		return bytes;
	}
	private byte[] longToByte(long l){
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putLong(l);
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

}
