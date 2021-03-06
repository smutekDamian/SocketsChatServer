package com.smutek.chat.connection;

import com.smutek.chat.client.Client;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by damian on 11.03.17.
 */
public class UDPListen extends Thread {
    private DatagramSocket socket = null;
    private int serverPort = 12346;
    private BlockingQueue<Client> clients;
    private InetAddress address;
    private static int bufferSize = 3000;

    public UDPListen(BlockingQueue<Client> clients) {
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
                broadcastMessage(receiveBuffer, receivePacket.getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    private void broadcastMessage(byte[] receiveBuffer, int senderPort){
        System.out.println("I am sending UDP message to all clients");
        for (Client client: clients) {
            if (client.getUDPPort() != senderPort) {
                DatagramPacket sendPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length, address, client.getUDPPort());
                try {
                    socket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
