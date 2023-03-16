package com.coolcap.askaserver.net;

public class ClientInfo
{
	private final int id;
	private String name;

	public ClientInfo(int id)
	{
		this.id = id;
	}

	public int getId()
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
}
