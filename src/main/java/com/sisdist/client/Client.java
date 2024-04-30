package com.sisdist.client;


public class Client {
    private final static String IP = "127.0.0.1";
    private final static int PORT = 21234;

    public static void main(String[] args) throws Exception{
        ClientApplication clientApp = new ClientApplication(IP, PORT);
        clientApp.start();
    }

}