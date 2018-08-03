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
    
    InetAddress ipAddress; //Serves as a "key"
    InetAddress forwardingNode;
    long cost;

    public Node(InetAddress ipAddress, InetAddress forwardingNode, long cost) {
        this.ipAddress = ipAddress;
        this.forwardingNode = forwardingNode;
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

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    } 

    @Override
    public String toString() {
        return "Node{" + "ipAddress=" + ipAddress + ", forwardingNode=" + forwardingNode + ", cost=" + cost + '}';
    }
    
    
}
