package com.ucarmustafa.duman.demo;

import java.net.ServerSocket;
import java.net.Socket;

import com.ucarmustafa.duman.Duman;

public class TcpServerDemo {

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(2806);
		
		while (true) {
			final Socket newClient = server.accept();
			System.out.println("new client: " + newClient.getRemoteSocketAddress().toString());
			Duman duman = new Duman(newClient.getOutputStream(), newClient.getInputStream(), new MessageToConsole());
			
			duman.send((byte)100);
			duman.send((byte)100, (byte)127);
			duman.send((byte)100, (short)10000);
			duman.send((byte)100, 2000000);
			duman.send((byte)100, 0.25f);
			
			//newClient.close();
		}
	}
	
}
