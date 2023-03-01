package com.coolcap.askaserver;

import com.coolcap.askaserver.net.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends ServerSocket
{
	public static Map<Integer, Room> rooms = new HashMap<>();

	public Server(int port) throws IOException
	{
		super(port);

		System.out.println("Server started on port: " + port);

		while (true)
		{
			Socket socket = this.accept();
			new ClientThread(socket).start();
		}
	}
}
