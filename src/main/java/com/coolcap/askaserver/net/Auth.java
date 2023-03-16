package com.coolcap.askaserver.net;

import java.net.InetSocketAddress;

public class Auth
{
	public static void Execute(Client client, String name)
	{
		if (!name.matches("^[A-Za-zА-Яа-я0-9_]{3,18}$"))
		{
			client.send("authResult", "ERR_WRONGNAME");
			return;
		}

		client.setAuth(true);
		client.setName(name);
		client.send("authResult", "OK");

		System.out.println(((InetSocketAddress)client.getSocket().getRemoteSocketAddress()).getAddress().getHostAddress() + " authed as " + name);
	}
}
