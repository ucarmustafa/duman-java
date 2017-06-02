package com.ucarmustafa.duman.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.ucarmustafa.duman.Duman;

public class BufferDemo {

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
		
		new Duman(new ByteArrayInputStream(data), new MessageToConsole());
	}
	
}
