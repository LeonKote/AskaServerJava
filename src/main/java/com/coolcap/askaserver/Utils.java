package com.coolcap.askaserver;

import java.util.Random;

public class Utils
{
	private static final Random rand = new Random();

	public static int random()
	{
		return rand.nextInt();
	}

	public static int randomRange(int min, int max)
	{
		return rand.nextInt(max - min) + min;
	}
}
