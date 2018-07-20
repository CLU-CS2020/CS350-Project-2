/*
 * Copyright OrangeDog LLC.
 * All rights reserved.
 */
package csc.pkg350;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import zzz.IPPacket;

public class Timer
{
	public final static int DESTPORT = 5432;

	static public void main(String args[]) throws IOException
	{
		//new Thread(new ServerThread()).start();

		String[] desthosts =
		{
			//	Alex
		//	"10.100.31.77"
			//	middle 5
			//"10.100.31.74", "10.100.30.176", "10.100.31.73", "10.100.31.93", "10.100.31.167"
		//	front 3
		"10.100.23.253", "10.16.0.107", "10.100.31.67"
		};

		for (String desthost : desthosts)
		{
			InetAddress dest;
			System.err.print("Looking up address of " + desthost + "...");
			try
			{
				dest = InetAddress.getByName(desthost);        // DNS query
			}
			catch (UnknownHostException uhe)
			{
				System.err.println("unknown host: " + desthost);
				return;
			}

			System.err.println(" got it!");

			DatagramSocket s;
			try
			{
				s = new DatagramSocket();
			}
			catch (SocketException ioe)
			{
				System.err.println("socket could not be created");
				return;
			}

			IPPacket m = IPPacket.createDoExchange();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(m);
			byte[] data = outputStream.toByteArray();
			DatagramPacket msg = new DatagramPacket(data, data.length, dest, DESTPORT);
			System.out.println("Sending " + m);
			s.send(msg);
		}
	}
}
