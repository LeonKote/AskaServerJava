package com.coolcap.askaserver.net;

import com.coolcap.askaserver.threads.ClientThread;
import com.coolcap.askaserver.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;

import static com.coolcap.askaserver.Server.getQuizzes;
import static com.coolcap.askaserver.Server.getRooms;

public class Client
{
	private final ClientThread thread;
	private final Socket socket;
	private final ClientInfo info;
	private boolean isAuth;
	private Room room;

	public Client(ClientThread thread, Socket socket)
	{
		this.thread = thread;
		this.socket = socket;

		info = new ClientInfo(Utils.randomRange(1, Integer.MAX_VALUE));
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

		if (request.isEmpty()) return;

		switch (request.names().getString(0))
		{
			case "auth":
				if (!isAuth)
					Auth.Execute(this, request.getString("auth"));
				break;
			case "create":
				if (isAuth && room == null)
				{
					String quiz = request.getString("create");

					if (getQuizzes().containsKey(quiz))
					{
						int code = Utils.randomRange(100000, 1000000);
						Room room = new Room(code, getQuizzes().get(quiz), this);
						getRooms().put(code, room);
					}
				}
				break;
			case "join":
				if (isAuth && request.get("join") instanceof Integer
						&& getRooms().containsKey(request.getInt("join")))
				{
					Room room = getRooms().get(request.getInt("join"));
					if (!room.isStarted())
						room.onClientJoin(this);
				}
				break;
			case "leave":
				if (room != null)
					room.onClientLeave(this);
				break;
			case "start":
				if (room != null)
					room.onGameStarted(this);
				break;
			case "answer":
				if (room != null)
					room.onAnswer(this, request.getInt("answer"));
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
		if (value == null) value = JSONObject.NULL;
		thread.send(new JSONObject().put(key, value).toString());
	}

	public Socket getSocket()
	{
		return socket;
	}

	public int getId()
	{
		return info.getId();
	}

	public String getName()
	{
		if (info.getName() == null)
			return ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().getHostAddress();
		else
			return info.getName();
	}

	public void setName(String name)
	{
		info.setName(name);
	}

	public ClientInfo getInfo()
	{
		return info;
	}

	public void setAuth(boolean auth)
	{
		isAuth = auth;
	}

	public void setRoom(Room room)
	{
		this.room = room;
	}

	public boolean isHost()
	{
		return room.getHost() == this;
	}
}
