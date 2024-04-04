package com.sisdist.server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;

public class ServerApplication {
    // TODO arrumar a porta para a da doc
    private ServerSocket serverSocket;
    private BufferedReader inStream;
    private PrintWriter outStream;


    private boolean shutdownRequest = false;

//    private ArrayList<Thread> threads = new ArrayList<>;

    private final ClientList clients = new ClientList();

    public void run(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            // NOTE timeout de 1s
            serverSocket.setSoTimeout(1000);
        } catch (IOException e) {
            System.out.println("Couldnt create socket at ServerApplication run()" + e);
        }
        System.out.println("Ouvindo por conexoes na porta " + port);
        // TODO Loop de aceitacao de conexoes

        // TODO CADA THREAD DO SERVIDOR VAI TER UMA THREAD DO CLIENTE PARA CONSEGUIR ITERAR EM CADA CONEXAO POR UM FOR LOOP
//        Socket inConnection;
        // Thread de aceitacao de conexoes
        Thread acceptThread = new Thread(() -> {
            while (!this.shutdownRequest) {
                try {
                    Socket inConnection = serverSocket.accept();
                    new Thread(() -> {
                        ClientHandler localInConnection = new ClientHandler(inConnection);
                        if (!clients.add(localInConnection)) {
                            // Nao adicionou, printar erro aqui?
                        } else {
                            localInConnection.start();
                        }
                    }).start();
                } catch (SocketTimeoutException e) {
                    // Tratar o timeout aqui, fechar a socket?
                } catch (IOException e) {
                    System.out.println("Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        });
        acceptThread.start();
        for (ClientHandler sock : clients) {
            // For each socket in clients...
            // FIXME do i create a new clientHandler here or when adding to the clients list?
            // TODO read if theres a pending message
        }
    }

    public void terminate() {
        this.shutdownRequest = true; // FIXME inutil?
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Couldnt close socket at ServerApplication terminate()" + e);
        }
    }
}
