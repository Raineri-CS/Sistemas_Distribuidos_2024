package com.sisdist.client;

import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.io.OutputStream;
import java.io.PrintWriter;


public class ClientApplication extends Application {
    private final String IP;
    private final int PORT;

    public ClientApplication(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader((ClientApplication.class.getResource("home_view.fxml")));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();

        // Estabelecer conexão com o servidor aqui
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(IP, PORT));

            // Verifica se o socket está aberto
            if (!socket.isClosed()) {
                // Envia uma mensagem ao servidor
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);

                // Escreva a mensagem que deseja enviar ao servidor
                writer.println("Hello, Server!");

                // Feche o escritor após o uso
                writer.close();
            }
        } catch (IOException e) {
            // Trate exceções de conexão aqui
            e.printStackTrace();
        }

    }
}
