package com.coolcap.askaserver;

import com.coolcap.askaserver.net.Client;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread
{
	private final Socket socket;
	private final Client client;
	private PrintWriter writer;

	public ClientThread(Socket socket)
	{
		this.socket = socket;
		client = new Client(this, socket);
		client.onConnect();
	}

	@Override
	public void run()
	{
		try
		{
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			while (true)
			{
				String message = reader.readLine();
				if (message == null)
				{
					client.onDisconnect();
					return;
				}
				client.onMessage(message);
			}
		}
		catch (IOException e)
		{
			client.onDisconnect();
			e.printStackTrace();
		}
	}

	public void send(String message)
	{
		writer.println(message);
	}
}
