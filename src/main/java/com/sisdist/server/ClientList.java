package com.sisdist.server;

import java.net.Socket;
import java.util.ArrayList;

public class ClientList extends ArrayList<ClientHandler> {
    @Override
    public boolean contains(Object obj){
        if(obj instanceof Socket){
            Socket sock = (Socket) obj;
            for (ClientHandler client: this){
                if (client.getSocket().equals(sock)){
                    return true;
                }
            }
        }
        return false;
    }

}
