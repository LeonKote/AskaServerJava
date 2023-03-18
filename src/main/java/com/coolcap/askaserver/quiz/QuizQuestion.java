package com.coolcap.askaserver.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestion
{
	private String question;
	private List<String> answers;
	private String image;
	private int time;
	private int rightAnswer;

	public QuizQuestion()
	{
		answers = new ArrayList<>();
		time = 30;
	}

	public QuizQuestion(QuizQuestion question)
	{
		this.question = question.question;
		this.answers = new ArrayList<>(question.answers);
		this.image = question.image;
		this.time = question.time;
		this.rightAnswer = question.rightAnswer;
	}

	public void shuffleAnswers()
	{
		String answer = answers.get(0);
		Collections.shuffle(answers);
		for (int i = 0; i < answers.size(); i++)
		{
			if (!answers.get(i).equals(answer))
				continue;

			rightAnswer = i;
			break;
		}
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	public List<String> getAnswers()
	{
		return answers;
	}

	public void setAnswers(List<String> answers)
	{
		this.answers = answers;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public int rightAnswer()
	{
		return rightAnswer;
	}
}
