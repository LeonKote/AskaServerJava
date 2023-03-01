package com.coolcap.askaserver.net;

import com.coolcap.askaserver.ClientThread;
import com.coolcap.askaserver.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;

import static com.coolcap.askaserver.Server.rooms;

public class Client
{
	public final ClientThread thread;
	public final Socket socket;
	public final int id;
	public boolean isAuth;
	public String name;
	public Room room;

	public Client(ClientThread thread, Socket socket)
	{
		this.thread = thread;
		this.socket = socket;

		id = Utils.randomRange(1, Integer.MAX_VALUE);
	}

	public void onMessage(String message)
	{
		JSONObject request;
		try
		{
			request = new JSONObject(message);
		}
		catch (JSONException e)
		{
			return;
		}

		switch (request.names().getString(0))
		{
			case "auth":
				if (!isAuth)
					Auth.Execute(this, request.getString("auth"));
				break;
			case "create":
				if (room == null)
				{
					int code = Utils.randomRange(100000, 1000000);
					Room room = new Room(code, this);
					rooms.put(code, room);
				}
				break;
			case "join":
				if (isAuth && request.get("join") instanceof Integer
						&& rooms.containsKey(request.getInt("join")))
					rooms.get(request.getInt("join")).onClientJoin(this);
				break;
			case "leave":
				if (room != null)
					room.onClientLeave(this);
				break;
			case "message":
				if (room != null)
					room.onClientMessage(id, request.getString("message"));
				break;
		}
		System.out.println(getName() + ": " + message);
	}

	public void onConnect()
	{
		System.out.println(getName() + " entered the server!");
	}

	public void onDisconnect()
	{
		if (room != null)
			room.onClientLeave(this);

		System.out.println(getName() + " has left the server!");
	}

	public void send(String key, Object value)
	{
		thread.send(new JSONObject().put(key, value).toString());
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		if (name == null)
			return ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().getHostAddress();
		else
			return name;
	}
}
