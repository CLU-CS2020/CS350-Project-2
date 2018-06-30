package iprouter;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author graham
 */
public class IPPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    //	0 = DoPing, 1 = DoExchange, 2 = Ping, 3 = PingReply, 4 = RouterTable, 5 = Message
    public final int messageType;

    //	used in RouterTable
    public final InetAddress address;
    public final long cost;

    //	used in Message
    public final String message;

    public IPPacket(int messageType, InetAddress address, long cost, String message) {
        this.messageType = messageType;
        this.address = address;
        this.cost = cost;
        this.message = message;
    }

    @Override
    public String toString() {
        return "IPPacket{" + "messageType=" + messageType + ", address="
                + address + ", cost=" + cost + ", message=" + message + '}';
    }

    public int getMessageType() {
        return messageType;
    }

    public InetAddress getAddress() {
        return address;
    }

    public long getCost() {
        return cost;
    }

    public String getMessage() {
        return message;
    }

}
