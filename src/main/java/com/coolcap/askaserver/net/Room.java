package com.coolcap.askaserver.net;

import com.coolcap.askaserver.Server;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Room
{
	public Map<Integer, Client> clients = new HashMap<>();
	public int code;

	public Room(int code, Client client)
	{
		this.code = code;

		onClientJoin(client);
	}

	public void onClientJoin(Client client)
	{
		client.setRoom(this);

		for (Client roomClient : clients.values())
		{
			roomClient.send("clientJoin", new JSONObject(client.getInfo()));
		}

		clients.put(client.getId(), client);

		client.send("roomJoin", new JSONObject().put("code", code).put("clients",
				clients.values().stream().map(Client::getInfo).collect(Collectors.toList())));
		onClientMessage(0, client.getName() + " entered the room");
	}

	public void onClientLeave(Client client)
	{
		client.setRoom(null);

		if (clients.size() == 1)
		{
			Server.getRooms().remove(code);
			return;
		}

		clients.remove(client.getId());

		for (Client roomClient : clients.values())
		{
			roomClient.send("clientLeave", new JSONObject(client.getInfo()));
		}
		onClientMessage(0, client.getName() + " has left the room!");
	}

	public void onClientMessage(int id, String text)
	{
		for (Client roomClient : clients.values())
		{
			roomClient.send("clientMessage", new JSONObject().put("id", id).put("text", text));
		}
	}
}
