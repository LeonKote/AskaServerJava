package com.coolcap.askaserver.quiz;

import java.util.*;

public class GameQuiz
{
	private final String name;
	private final Queue<QuizQuestion> questions = new LinkedList<>();

	public GameQuiz(Quiz quiz)
	{
		this.name = quiz.getName();
		for (QuizQuestion q : quiz.getQuestions())
		{
			QuizQuestion question = new QuizQuestion(q);
			question.shuffleAnswers();
			questions.add(question);
		}
		Collections.shuffle((List<?>) questions);
	}

	public String getName()
	{
		return name;
	}

	public QuizQuestion getNextQuestion()
	{
		return questions.poll();
	}

	public int getQuestionCount()
	{
		return questions.size();
	}
}
