package com.coolcap.askaserver.net;

import com.coolcap.askaserver.Server;
import com.coolcap.askaserver.quiz.Quiz;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;

public class Auth
{
	public static void Execute(Client client, String name)
	{
		if (!name.matches("^[A-Za-zА-Яа-я0-9_]{3,18}$"))
		{
			return;
		}

		client.setAuth(true);
		client.setName(name);

		JSONArray quizzes = new JSONArray();
		for (Quiz quiz : Server.getQuizzes().values())
		{
			quizzes.put(new JSONObject().put("id", quiz.getId()).put("name", quiz.getName()));
		}

		client.send("auth", new JSONObject().put("result", true).put("quizzes", quizzes));

		System.out.println(((InetSocketAddress)client.getSocket().getRemoteSocketAddress()).getAddress().getHostAddress() + " authed as " + name);
	}
}
