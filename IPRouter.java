package zzz;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class IPRouter {

    static public int destport = 5432;
    static public int bufsize = 512;
    static public final int timeout = 15000; // time in milliseconds
    static public final int UNIVERSAL_PORT = 5432;

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
        // public Node(InetAddress ipAddress, InetAddress forwardingNode, InetAddress address, long cost)
        // #EDIT
        Node kevinsNode = new Node((InetAddress.getByName("10.100.31.74")), (InetAddress.getByName("10.100.31.74")), (InetAddress.getByName("10.100.31.74")), 500);
        Node mileysNode = new Node((InetAddress.getByName("10.100.30.176")), (InetAddress.getByName("10.100.30.176")), (InetAddress.getByName("10.100.30.176")), 500);
        Node jakesNode = new Node((InetAddress.getByName("10.100.31.73")), (InetAddress.getByName("10.100.31.73")), (InetAddress.getByName("10.100.31.73")), 500);
        Node ryansNode = new Node((InetAddress.getByName("10.100.31.93")), (InetAddress.getByName("10.100.31.93")), (InetAddress.getByName("10.100.31.93")), 500);
        routingTable.add(kevinsNode);
        routingTable.add(ryansNode);

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

                // IPPacket(InetAddress source, InetAddress dest, int messageType, InetAddress address, long cost, String message)
                
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
                // 0 = Timer, 1 = DoExchange (not using anymore), 2 = Ping, 3 = PingReply, 4 = RouterTable, 5 = Message
                
                // On receipt of a Timer message, the router should: 
                //    1. choose a random destination, D, from the router table, and a random row, R, in the router table, and send D a RouterTable message containing the relevant parts of R.
                //    2. remember the current time, choose a random neighbor, N, from the router table, and send them a Ping message.
                
                // On receipt of a Ping message, the router should reply to the sender with a PingReply message.
                
                // On receipt of a PingReply or RouterTable message, the router should update its router table.
                
                // On receipt of a Message message, M, the router should forward M to the correct next hop according to the router table.
                //*******************************
                switch (messageType) {
                    case 0: // Timer / DoExchange

                        try {
                            int randomD = -1;
                            int randomR = -1;
                            if (routingTable.isEmpty()) {
                                System.out.println("No known destinations in routing table.");
                            } else {
                                /*
                                    On receipt of a DoExchange message, the router should choose a random 
                                    destination, D, from the router table, and a random row, R, in the router 
                                    table, and send D a RouterTable message containing the relevant parts of R.
                                 */
                                randomR = (int) (Math.random() * routingTable.size());
                                randomD = (int) (Math.random() * routingTable.size());
                                Node randomRow = routingTable.get(randomR);
                                Node randomDestination = routingTable.get(randomD);
                                /*while (randomRow.getIpAddress() == randomDestination.getIpAddress()) {
                                        randomD = (int) (Math.random() * routingTable.size());
                                        randomDestination = routingTable.get(randomD);
                                    }*/
                                System.out.println("Sending Node " + randomRow.getIpAddress() + " to "
                                        + randomDestination.getIpAddress() + ".");
                                // IPPacket(InetAddress source, InetAddress dest, int messageType, InetAddress address, long cost, String message)
                                // Old = Message type, address, cost, message
                                
                                IPPacket outgoingIPPacket = new IPPacket(4, randomRow.getIpAddress(), randomRow.getCost(),
                                        "DoExchange Node from the cool group");
                                outgoingPacket = new SendDetail(randomDestination.getIpAddress(), UNIVERSAL_PORT, outgoingIPPacket);
                            }
                        } catch (Exception e) {
                            System.out.println("Exception:" + e.getMessage());
                        }
                        break;
                    
                    case 1: // DoPing / DoExchange (Send a ping to a random node from the routingTable arraylist and mark the start time)
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
                                outgoingPacket = new SendDetail(randomNode.getIpAddress(), 5432, outgoingIPPacket);
                            }
                        } catch (Exception e) {
                            System.out.println("Exception:" + e.getMessage());
                        }
                        needToSend = true;
                        break;

                    

                    case 2: // Ping
                        outgoingPacket = Ping(sourceAddress, sourcePort, incomingIPPacket);
                        needToSend = true;
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
                        if (incomingIPPacket.getAddress() == (InetAddress.getByName("10.100.31.73"))) {
                            System.out.println("Ignoring entry for self");
                        } else {
                            System.out.println("Printing existing routing table:");
                            for (Node nodeOld : routingTable) {
                                System.out.println(nodeOld);
                            }
                            boolean needToAdd = true;
                            for (Node node : routingTable) {
                                if (node.getIpAddress() == incomingIPPacket.getAddress()) {

                                    if (incomingIPPacket.getCost() > node.getCost()) {
                                        System.out.println("Ignoring router table entry as cost is higher than existing record!");
                                    } else {
                                        node.setForwardingNode(sourceAddress);
                                        node.setCost(incomingIPPacket.getCost());
                                        System.out.println("Printing new routing table:");
                                        for (Node nodeNew : routingTable) {
                                            System.out.println(nodeNew);
                                        }
                                    }
                                    needToAdd = false;
                                }
                            }
                            if (needToAdd) {
                                Node newEntry = new Node(incomingIPPacket.getAddress(), sourceAddress, incomingIPPacket.getCost());
                                routingTable.add(newEntry);
                                System.out.println("Printing new routing table:");
                                for (Node nodeNew : routingTable) {
                                    System.out.println(nodeNew);
                                }
                            }
                        }
                        needToSend = false;
                        break;

                    case 5: // Message
                        System.out.println("Received Message from " + sourceAddress);
                        InetAddress destAddress = incomingIPPacket.getAddress(); //gets destination address from IPPacket
                        InetAddress nextHop = null; //address to forward the IPPacket to
                        // compare destination address to nodes in routingTabe and set destination address to nexthop
                        for (Node node : routingTable) {
                            if (node.getIpAddress() == destAddress) {
                                nextHop = node.getForwardingNode();//setting msg address
                                System.out.println("Setting forwarding Node to " + nextHop);
                            }
                        }
                        //creating SendDetail
                        SendDetail sd = new SendDetail(nextHop, UNIVERSAL_PORT, incomingIPPacket);
                        System.out.println("Forwarding message from " + sourceAddress + " to " + nextHop);
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
                    DatagramPacket msg = new DatagramPacket(b, b.length, outgoingPacket.getDestinationAddress(), UNIVERSAL_PORT); // creates new datagram to send with coordinates
                    Socket.send(msg); // sends message
                    System.out.println(outgoingPacket.getOutgoingIPPacket().toString());
                }

            } catch (SocketTimeoutException ste) {    // receive() timed out
                System.err.println("Response timed out!");
            } catch (Exception ioe) {                // should never happen!
                System.err.println("Bad receive");
                ioe.printStackTrace();
            }
        }
    }

    public static SendDetail Ping(InetAddress sourceAddress, int sourcePort, IPPacket incomingIPPacket) {
        InetAddress destinationAddress = sourceAddress; //sets destination address to source for reply
        int destinationPort = sourcePort; //sets destination port to source port for reply
        IPPacket outgoingIPPacket = new IPPacket(3, destinationAddress, 0, null); //defaults outgoing message to "PingReply" and creates IPPacket with null cost and message
        SendDetail sd = new SendDetail(destinationAddress, destinationPort, outgoingIPPacket);// creates new SendDetail
        return sd;
    }
}
