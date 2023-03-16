package com.coolcap.askaserver.quiz;

import java.util.ArrayList;
import java.util.List;

public class Quiz
{
	private final String id;
	private String name;
	private final List<QuizQuestion> questions;

	public Quiz(String id)
	{
		this.id = id;
		questions = new ArrayList<>();
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<QuizQuestion> getQuestions()
	{
		return questions;
	}
}
