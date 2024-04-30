package com.sisdist.server;

import java.io.*;
import java.net.Socket;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sisdist.server.messages.IN_THREE_PARAMETERS;
import com.sisdist.server.messages.IN_TWO_PARAMETERS;
import com.sisdist.server.messages.Message;

public class ClientHandler implements Runnable {
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
        OutMessage out;
        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Message json;
            String operation = jsonObject.get("operation").getAsString();
            switch(operation){
                case "LOGIN_CANDIDATE":
                case "LOGIN_RECRUITER":
                    json = gson.fromJson(jsonObject, IN_TWO_PARAMETERS.class);
                    break;
                case "LOGOUT_CANDIDATE":
                case "LOGOUT_RECRUITER":
                    json = gson.fromJson(jsonObject, IN_THREE_PARAMETERS.class);
                    break;
                default:
                    throw new IllegalArgumentException("Operação desconhecida: " + operation);
            }
            if (json == null) throw new JsonParseException("");

            out = new OutMessage(json.getOperation(), "ACCEPTED", Collections.emptyMap());
        } catch (Exception e) {
//            e.printStackTrace();
            out = new OutMessage(null, "REJECTED", Collections.emptyMap());
        }

        String outJson = gson.toJson(out);

        outJson = outJson.concat("\n");
        try {
//            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( clientSocket.getOutputStream() ) );
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println(outJson);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readMessages(BufferedReader buf) throws IOException {
//        StringBuilder message = new StringBuilder();
//        int c;
//        while ((c = buf.read()) != -1) {
//            if(c == '\0')
//                break;
//            message.append((char) c);
//        }
//        message.append("\n");
        processMessage(buf.readLine());
    }

    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        read();
    }
}
