package zzz;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author graham
 */
public class IPPacket implements Serializable
{
	private static final long serialVersionUID = 1L;

	//	0 = Timer, 1 = DoExchange (not using anymore), 2 = Ping, 3 = PingReply, 4 = RouterTable, 5 = Message
	public final int messageType;

	public final InetAddress source;
	public final InetAddress dest;

	//	used in RouterTable
	public final InetAddress address;
	public final long cost;

	//	used in Message
	public final String message;

	public IPPacket(InetAddress source, InetAddress dest, int messageType, InetAddress address, long cost, String message)
	{
		this.source = source;
		this.dest = dest;
		this.messageType = messageType;
		this.address = address;
		this.cost = cost;
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "IPPacket{" + "messageType=" + messageType + ", source=" + source + ", dest=" + dest + ", address=" + address + ", cost=" + cost + ", message=" + message + '}';
	}
}
