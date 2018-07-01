package iprouter;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IPRouter {

    static public int destport = 4445;
    static public int bufsize = 512;
    static public final int timeout = 15000; // time in milliseconds

    static public void main(String args[]) {

        DatagramSocket s;               // UDP uses DatagramSockets

        try {
            s = new DatagramSocket(destport);
        } catch (SocketException se) {
            System.err.println("cannot create socket with port " + destport);
            return;
        }
        try {
            s.setSoTimeout(timeout);       // set timeout in milliseconds
        } catch (SocketException se) {
            System.err.println("socket exception: timeout not set!");
        }

        // create DatagramPacket object for receiving data:
        DatagramPacket incomingMSG = new DatagramPacket(new byte[bufsize], bufsize);
        
        // Create Routing Table
        ArrayList<Node> routingTable = new ArrayList<>();

        // *** LISTEN LOOP ***
        while (true) { // read loop
            try {
                incomingMSG.setLength(bufsize);  // max received packet size
                s.receive(incomingMSG);          // the actual receive operation
                System.err.println("message from <"
                        + incomingMSG.getAddress().getHostAddress() + "," + incomingMSG.getPort() + ">");
                byte[] data = incomingMSG.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                // *** USE VARIABLES BELOW FOR METHODS ***
                IPPacket incomingIPPacket = (IPPacket) is.readObject(); // incomingIPPacket = the IP Packet object sent by the source node.

                InetAddress sourceAddress = incomingMSG.getAddress(); // sourceAddress = The IP address of the node that sent the IP Packet object. Must use this address when replying to the original sender.

                int sourcePort = incomingMSG.getPort(); // sourcePort = The port that the source used when sending. Use must use this port when replying to the original sender.

                int messageType = incomingIPPacket.getMessageType(); // messageType = The type of request we are being asked to do. Used to determine which switch case to use.

                System.out.println(incomingIPPacket.toString());

                Boolean needToSend = true;
                
                sendDetail outgoingPacket = new sendDetail(null, -1, null);

                //*******************************
                // ALL SWITCH CASES WILL GO HERE
                // Make sure to modify the outgoingIPPacket, destinationAddress, and destinationPort before the end of each switch case!
                //*******************************
                switch (messageType) {
                    case 0: // DoPing
                        DoPing();
                        break;

                    case 1: // DoExchange
                        DoExchange();
                        break;

                    case 2: // ping
                        Ping();
                        break;

                    case 3: // PingReply
                        PingReply();
                        break;

                    case 4: // RouterTable
                        RouterTable();
                        needToSend = false;
                        break;

                    case 5: // Message
                        Message();
                        break;
                    default:
                        System.out.println("Something went wrong. "
                                + "(switch default execute)");
                        needToSend = false;
                        break;
                } // end switch

                // *** SENDING THE COMPLETE DATAGRAM PACKET ***
                if (needToSend = true) {
                    DatagramSocket Socket;
                    Socket = new DatagramSocket();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // creates new byte output stream
                    ObjectOutputStream os = new ObjectOutputStream(outputStream);          // creates new out stream
                    os.writeObject(outgoingPacket.getOutgoingIPPacket()); // writes new client to send message
                    byte[] b = outputStream.toByteArray(); // writes bytes to array
                    DatagramPacket msg = new DatagramPacket(b, b.length, outgoingPacket.getDestinationAddress(), outgoingPacket.getDestinationPort()); // creates new datagram to send with coordinates
                    Socket.send(msg); // sends message
                }

            } catch (SocketTimeoutException ste) {    // receive() timed out
                System.err.println("Response timed out!");
            } catch (Exception ioe) {                // should never happen!
                System.err.println("Bad receive");
                ioe.printStackTrace();
            }
        }
    }

    public static void DoPing() {
        // insert code here & needed parameters.
    }

    /**
     *
     */
    public static void DoExchange() {
        // insert code here & needed parameters.
    }

    /**
     *
     */
    public static void Ping() {
        // insert code here & needed parameters.
    }

    /**
     *
     */
    public static void PingReply() {
        // insert code here & needed parameters.
    }

    /**
     *
     */
    public static void RouterTable() {
        // insert code here & needed parameters.
    }

    /**
     *
     */
    public static void Message() {
        // insert code here & needed parameters.
    }
}
