package com.ucarmustafa.duman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class DumanDemo {

	public static void main(String[] args) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Duman d1 = new Duman(baos); // write data to byte array output stream
		d1.send((byte)1);
		d1.send((byte)4, (byte)5);
		d1.send((byte)4, (byte)-100);
		d1.send((byte)4, (byte)200);
		d1.send((byte)5, (short)100);
		d1.send((byte)6, (short)200);
		d1.send((byte)7, (short)500);
		d1.send((byte)8, 1);
		d1.send((byte)9, 1000000);
		d1.send((byte)10, -1000000);
		d1.send((byte)11, 1);
		d1.send((byte)12, 1.01f);
		
		byte[] data = baos.toByteArray();
		
		for (byte b : data) {
			if (b >= 32 && b <= 126)
				System.out.println(b + " " + (char)b);
			else
				System.out.println(b);
		}
		
		new Duman(new ByteArrayInputStream(data), new IDumanListener() {
			
			@Override
			public void onSignal(byte messageType) {
				System.out.println("signal mt: " + messageType);
			}
			
			@Override
			public void onBool(byte messageType, boolean data) {
				System.out.println("bool mt: " + messageType + " data: " + data);
			}
			
			@Override
			public void onByte(byte messageType, byte data) {
				System.out.println("byte mt: " + messageType + " data: " + data);
			}
			
			@Override
			public void onShort(byte messageType, short data) {
				System.out.println("short mt: " + messageType + " data: " + data);
			}
			
			@Override
			public void onInt(byte messageType, int data) {
				System.out.println("int mt: " + messageType + " data: " + data);
			}
			
			@Override
			public void onFloat(byte messageType, float data) {
				System.out.println("float mt: " + messageType + " data: " + data);
			}
			
		});
	}
	
}
