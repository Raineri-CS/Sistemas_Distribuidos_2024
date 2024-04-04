package com.sisdist.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    // NOTE porta definida aqui
    public final static int PORT = 21234;

    private final ServerApplication app = new ServerApplication();

    public Server() {

        try {
            app.run(PORT);
        } catch (IOException e) {
            System.out.println("Error in Server() constructor");
        }
        // TODO inicializacao necessaria e configuracoes do server

        // TODO loop de leitura
    }

    public void close() throws IOException {
        app.terminate();
    }

    public static void main(String[] args) {
        Server serv = new Server();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String teste = reader.readLine();
            while (!teste.equals("0")) {
                // Fazer alguma coisa enquanto o usuário não inserir "0"
                // Por exemplo:
                System.out.println("Você inseriu: " + teste);
                teste = reader.readLine(); // Lê a próxima linha
            }
        } catch (IOException e) {
            System.err.println("Erro de entrada/saída: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close(); // Fechar o BufferedReader
                    serv.close();
                }
            } catch (IOException e) {
                System.err.println("Erro ao fechar o BufferedReader: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}


