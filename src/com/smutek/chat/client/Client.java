package com.smutek.chat.client;

import java.net.Socket;

/**
 * Created by damian on 11.03.17.
 */
public class Client {
    private String nick;
    private Socket socket;
    private int UDPPort;


    public Client(Socket socket, int UDPPort) {
        this.nick = null;
        this.socket = socket;
        this.UDPPort = UDPPort;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getUDPPort() {
        return UDPPort;
    }

    public void setUDPPort(int UDPPort) {
        this.UDPPort = UDPPort;
    }
}
