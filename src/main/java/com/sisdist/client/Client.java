package com.sisdist.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    private final static int PORT = 21234;

    public static void main(String[] args) throws Exception{
        String IP ;
        IP = readIP();
        ClientApplication clientApp = new ClientApplication(IP, PORT);
        clientApp.start();
    }

    private static String readIP(){
        BufferedReader reader = new BufferedReader((new InputStreamReader(System.in)));
        System.out.println("Informe o ip ");
        String out = "";
        try{
            out= reader.readLine();
        }catch (IOException e){
            // Blabla
        }
        return out;
    }

}