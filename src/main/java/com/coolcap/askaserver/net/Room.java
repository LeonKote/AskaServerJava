package com.coolcap.askaserver.net;

import com.coolcap.askaserver.threads.GameThread;
import com.coolcap.askaserver.Server;
import com.coolcap.askaserver.quiz.Quiz;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Room
{
	private final Map<Integer, Client> clients = new HashMap<>();
	private final int code;
	private final Quiz quiz;
	private final Client host;
	private final GameThread gameThread;

	private boolean isStarted;

	public Room(int code, Quiz quiz, Client client)
	{
		this.code = code;
		this.quiz = quiz;
		this.host = client;

		gameThread = new GameThread(this, quiz);

		onClientJoin(client);
	}

	public void onClientJoin(Client client)
	{
		client.setRoom(this);

		broadcast("clientJoin", new JSONObject(client.getInfo()));

		clients.put(client.getId(), client);

		client.send("roomJoin", new JSONObject().put("code", code).put("quiz", quiz.getName()).put("clients",
				clients.values().stream().map(Client::getInfo).collect(Collectors.toList())));
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

		broadcast("clientLeave", new JSONObject(client.getInfo()));
	}

	public void onGameStarted(Client client)
	{
		if (!client.isHost()) return;

		isStarted = true;
		gameThread.start();
	}

	public void onAnswer(Client client, int id)
	{
		gameThread.onAnswer(client, id);
	}

	public void broadcast(String key)
	{
		broadcast(key, null);
	}

	public void broadcast(String key, Object value)
	{
		for (Client roomClient : clients.values())
		{
			roomClient.send(key, value);
		}
	}

	public Map<Integer, Client> getClients()
	{
		return clients;
	}

	public Client getHost()
	{
		return host;
	}

	public boolean isStarted()
	{
		return isStarted;
	}
}
