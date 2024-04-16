package com.sisdist.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Server {
    // NOTE porta definida aqui
    public static final int PORT = 21234;
    private static final Logger LOGGER = Logger.getLogger(Server.class.toString());

    public static void main(String[] args) {
        ServerApplication app = new ServerApplication();
        app.start();

        handleFinish(app);
    }

    private static void handleFinish(ServerApplication app) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String teste = reader.readLine();
            while (!teste.equals("0")) {
                // Fazer alguma coisa enquanto o usuário não inserir "0"
                // Por exemplo:
                LOGGER.info("Você inseriu: " + teste);
                teste = reader.readLine(); // Lê a próxima linha
            }
        } catch (IOException e) {
            LOGGER.severe("Erro de entrada/saída: " + e.getMessage());
        } finally {
            try {
                reader.close(); // Fechar o BufferedReader
                app.terminate();
            } catch (IOException e) {
                System.err.println("Erro ao fechar o BufferedReader: " + e.getMessage());
            }
        }
    }
}


