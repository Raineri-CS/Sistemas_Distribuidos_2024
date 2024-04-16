package com.sisdist.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class ClientHandler implements Runnable {
    public static final int FS = '\u001c';
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void read() {
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
            readMessages(buf);
        } catch (IOException e) {
            System.err.println("Error in ClientHandler read()" + e);
        }
    }

    private void processMessage(String message) {
        Gson gson = new Gson();
        InMessage json;
        OutMessage out;
        try {
            json = gson.fromJson(message, InMessage.class);
            if (json == null) throw new JsonParseException("");

            out = new OutMessage(json.operation(), "ACCEPTED", Collections.emptyMap());
        } catch (Exception e) {
            out = new OutMessage(null, "REJECTED", null);
        }

        String outJson = gson.toJson(out);

        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println(outJson + (char) FS);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readMessages(BufferedReader buf) throws IOException {
        StringBuilder message = new StringBuilder();
        int c;
        while ((c = buf.read()) != -1) {
            if (c == FS){
                processMessage(message.toString());
                message = new StringBuilder();
            } else {
                message.append((char) c);
            }
        }
    }

    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        read();
    }
}
