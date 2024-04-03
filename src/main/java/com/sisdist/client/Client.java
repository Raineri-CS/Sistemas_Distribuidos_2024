package com.sisdist.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {
    private final static String IP = "127.0.0.1";
    private final static int PORT = 21234;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientApplication clientApp = new ClientApplication(IP, PORT);
        clientApp.start(primaryStage);
    }
}
