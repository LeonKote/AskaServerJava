package com.coolcap.askaserver;

import com.coolcap.askaserver.net.Room;
import com.coolcap.askaserver.quiz.Quiz;
import com.coolcap.askaserver.quiz.QuizQuestion;
import com.coolcap.askaserver.threads.ClientThread;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server extends ServerSocket
{
	private static ConcurrentMap<Integer, Room> rooms;
	private static Map<String, Quiz> quizzes;

	public Server(int port) throws IOException
	{
		super(port);

		rooms = new ConcurrentHashMap<>();
		quizzes = new HashMap<>();

		File quizzesFolder = new File("quizzes");

		for (File quizFolder : quizzesFolder.listFiles())
		{
			Quiz quiz = new Quiz(quizFolder.getName());

			Path path = quizFolder.toPath();
			String text = new String(Files.readAllBytes(path.resolve("quiz.json")));

			JSONObject jObj = new JSONObject(text);

			quiz.setName(jObj.getString("name"));

			jObj = jObj.getJSONObject("questions");

			int count = jObj.length() + 1;
			for (int i = 1; i < count; i++)
			{
				String id = Integer.toString(i);

				JSONObject q = jObj.getJSONObject(id);

				QuizQuestion question = new QuizQuestion();

				question.setQuestion(q.getString("q"));

				JSONArray a = q.getJSONArray("a");

				List<String> answers = new ArrayList<>();

				for (int j = 0; j < a.length(); j++)
				{
					answers.add(a.getString(j));
				}

				question.setAnswers(answers);

				question.setImage(Base64.getEncoder().encodeToString(Files.readAllBytes(path.resolve(id + ".jpg"))));

				quiz.getQuestions().add(question);
			}

			quizzes.put(quiz.getId(), quiz);
		}

		System.out.println("Server started on port: " + port);

		while (true)
		{
			Socket socket = this.accept();
			new ClientThread(socket).start();
		}
	}

	public static Map<Integer, Room> getRooms()
	{
		return rooms;
	}

	public static Map<String, Quiz> getQuizzes()
	{
		return quizzes;
	}
}
