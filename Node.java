/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zzz;

import java.net.InetAddress;

/**
 *
 * @author jake
 */
public class Node {
    
    InetAddress ipAddress;
    InetAddress forwardingNode;
    InetAddress address;
    long cost;

    
    // IPPacket(InetAddress source, InetAddress dest, int messageType, InetAddress address, long cost, String message)
    
    // ipAddress = source address
    // forwardingNode = destination address
    // address = who we are sending node to
    public Node(InetAddress ipAddress, InetAddress forwardingNode, InetAddress address, long cost) {
        this.ipAddress = ipAddress;
        this.forwardingNode = forwardingNode;
        this.address = address;
        this.cost = cost;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public InetAddress getForwardingNode() {
        return forwardingNode;
    }

    public void setForwardingNode(InetAddress forwardingNode) {
        this.forwardingNode = forwardingNode;
    }
    
    public InetAddress getAddress() {
        return forwardingNode;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    } 
    
}
