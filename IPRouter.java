package iprouter;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class IPRouter {

    static public int destport = 4445;
    static public int bufsize = 512;
    static public final int timeout = 15000; // time in milliseconds

    static public void main(String args[]) throws UnknownHostException {

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
        
        long startTime = -1;

        long finishTime = -1;

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

                SendDetail outgoingPacket = new SendDetail(null, -1, null);

                //*******************************
                // ALL SWITCH CASES WILL GO HERE
                // Make sure to modify the outgoingIPPacket, destinationAddress, and destinationPort before the end of each switch case!
                //*******************************
                switch (messageType) {
                    case 0: // DoPing (Send a ping to a random node from the routingTable arraylist and mark the start time)
                        try {
                            int randomIndex = -1;
                            if (routingTable.isEmpty()) {
                                System.out.println("No known destinations in routing table.");
                            } else {
                                randomIndex = (int) (Math.random() * routingTable.size());
                                Node randomNode = routingTable.get(randomIndex);
                                System.out.println("Sending Ping Request to " + randomNode.getIpAddress());
                                startTime = new GregorianCalendar().getTimeInMillis();
                                IPPacket outgoingIPPacket = new IPPacket(2, randomNode.getIpAddress(), 0, "This is a ping.");
                                outgoingPacket = new SendDetail(randomNode.getIpAddress(), 4445, outgoingIPPacket);
                            }
                        } catch (Exception e) {
                            System.out.println("Exception:" + e.getMessage());
                        }
                        needToSend = true;
                        break;

                    case 1: // DoExchange
                        try {
                            int randomD = -1;
                            int randomR = -1;
                            if (routingTable.isEmpty()) {
                                System.out.println("No known destinations in routing table.");
                            }
                            else {
                                /*
                                On receipt of a DoExchange message, the router should choose a random 
                                destination, D, from the router table, and a random row, R, in the router 
                                table, and send D a RouterTable message containing the relevant parts of R.
                                */
                                randomR = (int) (Math.random() * routingTable.size());
                                randomD = (int) (Math.random() * routingTable.size());
                                Node randomRow = routingTable.get(randomR);
                                Node randomDestination = routingTable.get(randomD);
                                while (randomRow.getIpAddress() == randomDestination.getIpAddress())
                                {
                                    randomD = (int) (Math.random() * routingTable.size());
                                    randomDestination = routingTable.get(randomD);
                                }
                                System.out.println("Sending Node " + randomRow.getIpAddress() + " to " + 
                                        randomDestination.getIpAddress() + ".");
                                
                                IPPacket outgoingIPPacket = new IPPacket (4, randomRow.getIpAddress(), randomRow.getCost(),
                                        "DoExchange Node from the cool group");
                                outgoingPacket = new SendDetail (randomDestination.getIpAddress(), 4445, outgoingIPPacket);
                            }
                        }
                        catch (Exception e) {
                            System.out.println("Exception:" + e.getMessage());
                        }
                        break;

                    case 2: // Ping (Replying to a ping sent by another node)
                        for (Node node : routingTable) {
                            if (node.getIpAddress() != sourceAddress) {
                                Node newNode = new Node(sourceAddress, sourceAddress, 0);
                                routingTable.add(newNode);
                            }
                        }
                            System.out.println("Sending ping reply to " + sourceAddress);
                            IPPacket outgoingIPPacket = new IPPacket(3, sourceAddress, 0, "This is a ping reply.");
                            outgoingPacket = new SendDetail(sourceAddress, 4445, outgoingIPPacket);
                        break;

                    case 3: // PingReply (Adding the information from a ping reply to our table, including the final cost)
                        for (Node node : routingTable) {
                            if (node.getIpAddress() != sourceAddress) {
                                Node newNode = new Node(sourceAddress, sourceAddress, -1);
                                routingTable.add(newNode);
                                System.out.println("We received a ping reply from a node we didn't request it from, we added them anyway! ^_^");
                            }
                        }
                        finishTime = new GregorianCalendar().getTimeInMillis();
                        long TotalTime = finishTime - startTime;
                        System.out.println("Ping time: " + (TotalTime + "ms"));
                        //FOR LOOP TO GET IP ADDRESS AND UPDATE COST
                        needToSend = false;
                        break;

                    case 4: // RouterTable
                        for (Node node : routingTable) {
                            if (node.getIpAddress() != sourceAddress) {
                                Node newNode = new Node(sourceAddress, sourceAddress, -1);
                                routingTable.add(newNode);
                            }
                        }
                        needToSend = false;
                        break;

                    case 5: // Message
                        for (Node node : routingTable) {
                            if (node.getIpAddress() != sourceAddress) {
                                Node newNode = new Node(sourceAddress, sourceAddress, -1);
                                routingTable.add(newNode);
                            }
                        }
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
}
