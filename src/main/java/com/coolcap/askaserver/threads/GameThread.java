package com.coolcap.askaserver.threads;

import com.coolcap.askaserver.net.Client;
import com.coolcap.askaserver.net.Room;
import com.coolcap.askaserver.quiz.GameQuiz;
import com.coolcap.askaserver.quiz.Quiz;
import com.coolcap.askaserver.quiz.QuizQuestion;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameThread extends Thread
{
	private final Room room;
	private final GameQuiz gameQuiz;
	private final Map<Integer, Integer> globalScore;
	private final Set<Client> answers;

	private QuizQuestion currentQuestion;
	private boolean isAnswerAllowed;
	private long answerStartTime;

	public GameThread(Room room, Quiz quiz)
	{
		this.room = room;
		gameQuiz = new GameQuiz(quiz);
		globalScore = new HashMap<>();
		answers = new HashSet<>();
	}

	@Override
	public void run()
	{
		for (Client client : room.getClients().values())
		{
			globalScore.put(client.getId(), 0);
		}

		room.broadcast("gameStarted");
		wait(3000);

		room.broadcast("startTimer");
		wait(4000);

		while (gameQuiz.getQuestionCount() > 0)
		{
			currentQuestion = gameQuiz.getNextQuestion();
			room.broadcast("roundStarted", new JSONObject(currentQuestion));

			wait(3000);

			isAnswerAllowed = true;
			answerStartTime = System.currentTimeMillis();

			int time = currentQuestion.getTime();
			for (int i = 0; i < time; i++)
			{
				if (answers.size() == room.getClients().size())
					break;

				wait(1000);
			}

			isAnswerAllowed = false;

			room.broadcast("rightAnswer", currentQuestion.rightAnswer());
			wait(3000);

			room.broadcast("roundEnded", globalScore);
			answers.clear();

			wait(5000);
		}

		room.broadcast("gameEnded", globalScore);
	}

	public void onAnswer(Client client, int id)
	{
		if (!isAnswerAllowed) return;

		long time = System.currentTimeMillis();

		boolean right = id == currentQuestion.rightAnswer();
		int score = right ? (int) (30000 - (time - answerStartTime)) : 0;

		globalScore.put(client.getId(), globalScore.get(client.getId()) + score);
		answers.add(client);
	}

	public void wait(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
