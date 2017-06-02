package com.ucarmustafa.duman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class Duman {

	private static final int[] DATA_LENGTHS = { 0, 1, 2, 4, 4 };
	
	private static final byte DT_VOID = 0;
	private static final byte DT_INT8 = 1;
	private static final byte DT_INT16 = 2;
	private static final byte DT_INT32 = 3;
	private static final byte DT_FLOAT = 4;
	//private static final byte DT_DOUBLE = 5;
	
	private OutputStream os;
	private InputStream is;
	private byte[] buffer = new byte[1024];
	private int bufferIdx = 0;
	private boolean closeRequested = false;
	private List<IDumanListener> listeners;
	
	public Duman(OutputStream os) {
		this(os, null, null);
	}
	
	public Duman(InputStream is, IDumanListener l) {
		this(null, is, l);
	}
	
	public Duman(OutputStream os, InputStream is, IDumanListener l) {
		this.os = os;
		this.is = is;
		this.listeners = new LinkedList<>();
		
		if (l != null)
			this.listeners.add(l);
		
		if (this.is != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (!closeRequested)
						try {
							int data = Duman.this.is.read();
							if (data < 0)
								closeRequested = true;
							else if (bufferIdx == 0 && data != '<')
								continue;
							else {
								buffer[bufferIdx++] = (byte)data;
								if (data == '>')
									checkMessageEnd();
							}
						} catch (SocketException e) {
							e.printStackTrace();
							
							if (e.getMessage().equals("Socket closed"))
								break;
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}).start();
		}
	}
	
	public void addListener(IDumanListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(IDumanListener listener) {
		this.listeners.remove(listener);
	}
	
	public void close() {
		closeRequested = true;
	}
	
	private void checkMessageEnd() {
		byte dataType = buffer[1];
		int messageEnd = DATA_LENGTHS[dataType] + 3;
		if (bufferIdx < messageEnd)
			return;
		
		if (buffer[messageEnd] == '>') { // message ok
			/*for (int i = 0; i <= messageEnd; i++)
				System.out.print(buffer[i] + " ");
			System.out.println();*/
			
			byte messageType = buffer[2];
			
			switch (dataType) {
				case DT_VOID:
					for (IDumanListener l : listeners)
						l.onSignal(messageType);
					break;
				case DT_INT8:
					for (IDumanListener l : listeners)
						l.onByte(messageType, buffer[3]);
					break;
				case DT_INT16:
					short sData = (short)((buffer[4] << 8) | ((short)buffer[3] & 0x00FF));
					for (IDumanListener l : listeners)
						l.onShort(messageType, sData);
					break;
				case DT_INT32:
					int iData = ((int)buffer[6] << 24 & 0xFF000000) | 
								((int)buffer[5] << 16 & 0x00FF0000) | 
								((int)buffer[4] << 8 & 0x0000FF00) | 
								(int)buffer[3] & 0x000000FF;
					for (IDumanListener l : listeners)
						l.onInt(messageType, iData);
					break;
				case DT_FLOAT:
					int ifData = ((int)buffer[6] << 24 & 0xFF000000) | 
								((int)buffer[5] << 16 & 0x00FF0000) | 
								((int)buffer[4] << 8 & 0x0000FF00) | 
								(int)buffer[3] & 0x000000FF;
					float fData = Float.intBitsToFloat(ifData);
					for (IDumanListener l : listeners)
						l.onFloat(messageType, fData);
					break;
				
			}
			
			for (int i = messageEnd + 1, j = 0; i < bufferIdx; i++, j++)
				buffer[j] = buffer[i];
			bufferIdx -= messageEnd + 1;
		} else {
			// false message head. find next
			int nextStart = -1;
			for (int i = 0; i < bufferIdx && nextStart == -1; i++)
				if (buffer[i] == '<')
					nextStart = i;
			
			if (nextStart == -1) // header not found
				bufferIdx = 0;
			else { // move next start to head
				for (int i = nextStart; i < bufferIdx; i++)
					buffer[i - nextStart] = buffer[i];
				bufferIdx = bufferIdx - nextStart;
			}
		}
	}
	
	private void fillBufferAndSend(byte messageType, byte dataType, byte... data) throws IOException {
		if (this.os == null)
			throw new NullPointerException("Output stream is not set");
		
		this.os.write('<');
		this.os.write(dataType);
		this.os.write(messageType);
		this.os.write(data, 0, data.length);
		this.os.write('>');
		this.os.flush();
	}
	
	public void send(byte messageType) throws IOException {
		fillBufferAndSend(messageType, DT_VOID);
	}

	public void send(byte messageType, byte data) throws IOException {
		fillBufferAndSend(messageType, DT_INT8, data);
	}

	public void send(byte messageType, short data) throws IOException {
		fillBufferAndSend(messageType, DT_INT16, (byte)data, (byte)(data >> 8));
	}

	public void send(byte messageType, int data) throws IOException {
		fillBufferAndSend(messageType, DT_INT32,
				(byte)data,
				(byte)(data >> 8),
				(byte)(data >> 16),
				(byte)(data >> 24));
	}

	public void send(byte messageType, float data) throws IOException {
		int dataInt = Float.floatToRawIntBits(data);
		
		fillBufferAndSend(messageType, DT_FLOAT,
				(byte)dataInt,
				(byte)(dataInt >> 8),
				(byte)(dataInt >> 16),
				(byte)(dataInt >> 24));
	}
	
}
