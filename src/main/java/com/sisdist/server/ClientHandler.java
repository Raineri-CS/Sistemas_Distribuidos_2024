package com.sisdist.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;


    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void read() {
        try {
            if (!clientSocket.isClosed()) {
                BufferedReader buf = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
                // TODO fazer o processamento dos dados que vieram
                System.out.println(buf);
            } else {
                System.out.println("Socket is closed, cannot read.");
            }
        } catch (IOException e) {
            System.out.println("Error in ClientHandler read()" + e);
        }
    }

    public void terminate() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Couldnt close socket in ClientHandler terminate()" + e);
        }
    }

    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        // TODO o loop da thread aqui
        while (true) {
            read();
        }
    }
}
