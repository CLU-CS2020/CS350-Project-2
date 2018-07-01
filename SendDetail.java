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
public class SendDetail {
        
    InetAddress destinationAddress;
    int destinationPort;
    IPPacket outgoingIPPacket;

    public SendDetail(InetAddress destinationAddress, int destinationPort, IPPacket outgoingIPPacket) {
        this.destinationAddress = destinationAddress;
        this.destinationPort = destinationPort;
        this.outgoingIPPacket = outgoingIPPacket;
    }

    public InetAddress getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(InetAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public IPPacket getOutgoingIPPacket() {
        return outgoingIPPacket;
    }

    public void setOutgoingIPPacket(IPPacket outgoingIPPacket) {
        this.outgoingIPPacket = outgoingIPPacket;
    }
    
    
    
}
