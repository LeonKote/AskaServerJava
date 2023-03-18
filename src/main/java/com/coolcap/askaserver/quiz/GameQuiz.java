package com.coolcap.askaserver.quiz;

import java.util.*;

public class GameQuiz
{
	private final Queue<QuizQuestion> questions;

	public GameQuiz(Quiz quiz)
	{
		questions = new LinkedList<>();
		for (QuizQuestion q : quiz.getQuestions())
		{
			QuizQuestion question = new QuizQuestion(q);
			question.shuffleAnswers();
			questions.add(question);
		}
		Collections.shuffle((List<?>) questions);
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
