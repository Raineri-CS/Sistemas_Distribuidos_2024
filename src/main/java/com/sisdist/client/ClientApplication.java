package com.sisdist.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.logging.Logger;


public class ClientApplication {
    private final String IP;
    private final int PORT;

    public ClientApplication(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    public void start() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(IP, PORT));

            if (socket.isConnected()) {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("{\"operation\":\"LOGIN_CANDIDATE\",\"data\":{\"email\":\"some1@email.com\",\"password\":\"some_password\",\"name\":\"SomeName\",\"industry\":\"SomeBranch\",\"description\":\"Somedescription\"},\"token\":\"some_token\"}");
                System.out.println("Mensagem enviada ao servidor");

                BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                while ((line = buf.readLine()) != null) {
                    System.out.println("Mensagem do servidor: " + line);
                }
            } else {
                System.err.println("Não foi possível conectar ao servidor.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao tentar criar a socket " + e.getMessage());
        }
    }

}
