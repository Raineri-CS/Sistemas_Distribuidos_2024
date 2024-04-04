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

        // Thread de aceitacao de conexoes
       new Thread(() -> {
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
        }).start();
        // Thread que fica processando os dados
        // FIXME cuidar com concorrencia, talvez uma fila de operacoes a fazer ?
        new Thread(() ->{
            for (ClientHandler sock : clients) {
                // For each socket in clients...
                // FIXME do i create a new clientHandler here or when adding to the clients list?
                // TODO read if theres a pending message
            }
        }).start();
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
