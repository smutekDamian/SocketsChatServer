package com.smutek.chat;

import com.smutek.chat.client.Client;
import com.smutek.chat.connection.Connection;
import com.smutek.chat.connection.UDPListen;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Chat server started");
        int clientsQuantity = 10;
        int actualClientsQuantity = 0;
        BlockingQueue<Client> clients = new ArrayBlockingQueue<Client>(clientsQuantity);
        int port = 12345;
	    ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            UDPListen udpListen = new UDPListen(clients);
            udpListen.start();
            while (true){
                Socket clientSocket = serverSocket.accept();
                if (clients.size() < clientsQuantity){
                    System.out.println("I've got a connection. I'm adding new client");
                    actualClientsQuantity++;
                    int UDPPort = port+actualClientsQuantity+1;
                    //send UDP Port to client
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(UDPPort);
                    //out.close();
                    clients.put(new Client(clientSocket, UDPPort));
                    Connection connection = new Connection(clients,clientSocket, actualClientsQuantity);
                    connection.start();
                }
                else
                    System.out.println("I've got a connection, but I'm busy");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) serverSocket.close();
        }
    }
}
