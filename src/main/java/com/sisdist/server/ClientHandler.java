package com.sisdist.server;

import java.io.*;
import java.net.Socket;
import java.util.Collections;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sisdist.server.messages.IN_THREE_PARAMETERS;
import com.sisdist.server.messages.IN_TWO_PARAMETERS;
import com.sisdist.server.messages.Message;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String magicWord = "DISTRIBUIDOS";

    private Algorithm algorithm = Algorithm.HMAC256(magicWord);

    private String genToken(int id, String role) throws JWTCreationException {
        try {
            return JWT.create().withClaim("id", String.valueOf(id)).withClaim("role", role).sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Falha ao criar JWT", exception);
        }
    }

    private DecodedJWT verifyToken(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

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
        OutMessage out = null;
        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Message json = null;
            String operation = jsonObject.get("operation").getAsString();
            switch (operation) {
                case "LOGIN_CANDIDATE":
                case "LOGIN_RECRUITER":
                    json = gson.fromJson(jsonObject, IN_TWO_PARAMETERS.class);
                    // Para qualquer login, gerar o token para assinar o pacote

                    break;
                case "LOGOUT_CANDIDATE":
                case "LOGOUT_RECRUITER":
                    json = gson.fromJson(jsonObject, IN_THREE_PARAMETERS.class);
                    // TODO as mensagens atribuindo pra out
                    break;
                default:
                    out = new OutMessage("NAO_EXISTE", null , null);
            }
            if (json == null) throw new JsonParseException("");

        } catch (Exception e) {
//            e.printStackTrace();
            out = new OutMessage(null, "REJECTED", Collections.emptyMap());
        }

        String outJson = gson.toJson(out);

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
