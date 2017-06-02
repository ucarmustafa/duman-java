package com.ucarmustafa.duman.demo;

import com.ucarmustafa.duman.IDumanListener;

public class MessageToConsole implements IDumanListener {
	
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

}
