package com.ucarmustafa.duman;

public interface IDumanListener {

	public void onSignal(byte messageType);
	
	public void onBool(byte messageType, boolean data);
	
	public void onByte(byte messageType, byte data);
	
	public void onShort(byte messageType, short data);
	
	public void onInt(byte messageType, int data);
	
	public void onFloat(byte messageType, float data);
	
}
