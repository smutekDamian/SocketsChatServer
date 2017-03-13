package com.smutek.chat.connection;

import com.smutek.chat.client.Client;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * Created by damian on 11.03.17.
 */
public class UDPListen extends Thread {
    private DatagramSocket socket = null;
    int serverPort = 12346;
    private List<Client> clients;
    private InetAddress address;
    private static int bufferSize = 3000;

    public UDPListen(List<Client> clients) {
        this.clients = clients;
        try {
            this.address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            socket = new DatagramSocket(serverPort);
            byte[] receiveBuffer = new byte[bufferSize];
            while(true){
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                System.out.println("I've got UDP request");
                broadcastMessage(receiveBuffer);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    public void broadcastMessage(byte[] receiveBuffer){
        System.out.println("I am sending UDP message to all clients");
        for (Client client: clients) {
            DatagramPacket sendPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length, address, client.getUDPPort());
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
