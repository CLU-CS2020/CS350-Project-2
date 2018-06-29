/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprouter;

import java.net.InetAddress;

/**
 *
 * @author jake
 */
public class Node {
    
    InetAddress ipAddress;
    InetAddress forwardingNode;
    long cost;

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
    
}
