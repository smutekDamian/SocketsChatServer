package com.smutek.chat.connection;

import com.smutek.chat.client.Client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by damian on 11.03.17.
 */
public class Connection extends Thread {
    private BlockingQueue<Client> clients;
    private Socket clientSocket;
    private BufferedReader in;
    private String nick;
    private int actualClientsQuantity;

    public Connection(BlockingQueue<Client> clients, Socket clientSocket, int actualClientsQuantity){
        this.clients = clients;
        this.clientSocket = clientSocket;
        this.actualClientsQuantity = actualClientsQuantity;
    }

    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            nick = in.readLine();
            System.out.println("Received first msg with nick: " + nick);
            setClientNick(nick);
            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    deleteClient();
                    break;
                }
                System.out.println("Received msg: " + msg + " from: " + nick + " Sending it to another clients");
                broadcastMessage(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String msg){
        for (Client client: clients) {
            if (!client.getSocket().equals(clientSocket)) {
                try {
                    PrintWriter out = new PrintWriter(client.getSocket().getOutputStream(), true);
                    out.println(nick + ": " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setClientNick(String nick){
        for (Client client: clients){
            if (client.getSocket().equals(clientSocket)){
                client.setNick(nick);
            }
        }
    }
    public void deleteClient() {
        Client temporaryClient = null;
        for (Client client : clients) {
            if (client.getSocket().equals(clientSocket)) {
                temporaryClient = client;
                break;
            }
        }
        if (temporaryClient != null) {
            clients.remove(temporaryClient);
        }
        actualClientsQuantity = clients.size();
    }
}
