package com.coolcap.askaserver.net;

import com.coolcap.askaserver.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
		client.room = this;

		for (Client roomClient : clients.values())
		{
			roomClient.send("clientJoin", new JSONObject(client));
		}

		clients.put(client.id, client);

		client.send("roomJoin", new JSONObject().put("code", code).put("clients", new JSONArray(clients.values())));
		onClientMessage(0, client.getName() + " entered the room");
	}

	public void onClientLeave(Client client)
	{
		client.room = null;

		if (clients.size() == 1)
		{
			Server.rooms.remove(code);
			return;
		}

		clients.remove(client.id);

		for (Client roomClient : clients.values())
		{
			roomClient.send("clientLeave", new JSONObject(client));
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
