package com.sisdist.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sisdist.common.messages.MESSAGE_THREE_PARAMETERS;
import com.sisdist.common.messages.MESSAGE_THREE_PARAMETERS_WITH_TOKEN;
import com.sisdist.common.messages.MESSAGE_TWO_PARAMETERS;
import com.sisdist.common.messages.Message;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger((ClientHandler.class).toString());
    private final Socket clientSocket;
    private final String magicWord = "DISTRIBUIDOS";
    private final Algorithm algorithm = Algorithm.HMAC256(magicWord);

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    private String genToken(int id, String role) throws JWTCreationException {
        try {
            return JWT.create().withClaim("id", String.valueOf(id)).withClaim("role", role).sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Falha ao criar JWT", exception);
        }
    }

    private DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // Se ele entrou aqui, quer dizer que o token nao eh valido
            return null;
        }
    }

    public void read() {
        try {
            if (!clientSocket.isClosed()) {
                BufferedReader buf = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
                readMessages(buf);
            } else {
                // Bom, o cliente fechou a socket, logo, pode-se sair seguramente
                // TODO =)

            }
        } catch (SocketException se) {
            LOGGER.severe("Error in ClientHandler read()" + se);
        } catch (IOException e) {
            LOGGER.severe("Error in ClientHandler read()" + e);
        }
    }

    private void processMessage(String message) {
        LOGGER.info("Arrived at processMessage with client " + clientSocket.getInetAddress() + " message: " + message);
        Gson gson = new Gson();
        // Definicao da mensagem que sempre vai sair
        MESSAGE_THREE_PARAMETERS out = null;
        // Definicao de variaveis para ajuda na legibilidade
        String email = null;
        String nome = null;
        String senha = null;
        String token = null;
        String ramo = null;
        String descricao = null;
        String operation;
        // Definicao dos objetos a serem usados
        JsonObject jsonObject;
        JsonObject dataObject = null;
        Message jsonMessage = null;
        Cliente sqlResult = null;
        Map<String, String> tempData = new HashMap<>();
        DecodedJWT decJWT;
        int candidateId;
        boolean isCandidate = true;

        try {
            jsonObject = JsonParser.parseString(message).getAsJsonObject();
            operation = jsonObject.get("operation").getAsString();
            switch (operation) {
                case "LOGIN_RECRUITER":
                    isCandidate = false;
                    // Fall through
                case "LOGIN_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_TWO_PARAMETERS.class);
                    if (!jsonObject.has("data")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    dataObject = jsonObject.get("data").getAsJsonObject();
                    if (!dataObject.has("email") || !dataObject.has("password")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    if (dataObject.get("email").isJsonNull() || dataObject.get("password").isJsonNull()) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    email = dataObject.get("email").getAsString();
                    senha = dataObject.get("password").getAsString();
                    if(isCandidate){
                        sqlResult = DatabaseManager.readClienteCandidatoFromEmail(email);
                    }else{
                        sqlResult = DatabaseManager.readClienteEmpresaFromEmail(email);
                    }

                    if (sqlResult != null) {
                        if (senha.equals(sqlResult.getSenha())) {
                            if(isCandidate){
                                token = genToken(sqlResult.getId(), "CANDIDATE");
                            }else{
                                token = genToken(sqlResult.getId(), "RECRUITER");
                            }
                            tempData.put("token", token);
                            out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", tempData);
                        } else {
                            out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_LOGIN", Collections.emptyMap());
                        }
                    } else {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_LOGIN", Collections.emptyMap());
                    }
                    break;
                case "LOGOUT_RECRUITER":
                case "LOGOUT_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);

                    if(!jsonObject.has("token") || jsonObject.get("token").isJsonNull()) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    if(verifyToken(jsonObject.get("token").getAsString()) == null){
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_TOKEN", Collections.emptyMap());
                        break;
                    }
                    // Aqui ja verificou o token, voltar sucesso
                    out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                    break;

                case "SIGNUP_RECRUITER":
                    isCandidate = false;

                case "SIGNUP_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_TWO_PARAMETERS.class);
                    if (jsonObject.has("data")) {
                        dataObject = jsonObject.getAsJsonObject("data");
                    } else {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    if (dataObject == null || (!dataObject.has("name") || dataObject.get("name").isJsonNull() || !dataObject.has("email") || dataObject.get("email").isJsonNull() || !dataObject.has("password") || dataObject.get("password").isJsonNull())) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }


                    nome = dataObject.get("name").getAsString();
                    email = dataObject.get("email").getAsString();
                    senha = dataObject.get("password").getAsString();
                    if(!isCandidate){
                        ramo = dataObject.get("industry").getAsString();
                        descricao = dataObject.get("description").getAsString();
                    }

                    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    sqlResult = DatabaseManager.readClienteCandidatoFromEmail(email);

                    // Se o resultado nao for nulo, o cliente existe
                    if (sqlResult != null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "USER_EXISTS", Collections.emptyMap());
                        break;
                    }

                    // Se algum campo estiver em branco...
                    if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }
                    if(isCandidate){
                        DatabaseManager.createClienteCandidato(nome, email, senha);
                    }else{
                        DatabaseManager.createClienteEmpresa(nome, email, senha, ramo, descricao);
                    }
                    out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                    break;
                case "LOOKUP_ACCOUNT_RECRUITER":
                    isCandidate = false;
                case "LOOKUP_ACCOUNT_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);
                    if (!jsonObject.has("token")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    token = jsonObject.get("token").getAsString();
                    if ((decJWT = verifyToken(token)) == null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_TOKEN", Collections.emptyMap());
                        break;
                    }

                    candidateId = Integer.parseInt(decJWT.getClaim("id").asString());

                    if(isCandidate){
                        sqlResult = DatabaseManager.readClienteCandidato(candidateId);
                    }else{
                        sqlResult = DatabaseManager.readClienteEmpresa(candidateId);
                    }

                    if (sqlResult == null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "USER_NOT_FOUND", Collections.emptyMap());
                    } else {
                        Map<String, String> auxData;
                        if(isCandidate){
                            auxData = Map.of("email", sqlResult.getEmail(), "name", sqlResult.getNome(), "password", sqlResult.getSenha());
                        }else{
                            auxData = Map.of("email", sqlResult.getEmail(), "name", sqlResult.getNome(), "password", sqlResult.getSenha(), "industry", ((Empresa) sqlResult).getRamo(), "description", ((Empresa) sqlResult).getDescricao());
                        }
                        out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", auxData);
                    }
                    break;

                case "UPDATE_ACCOUNT_RECRUITER":
                    isCandidate = false;
                case "UPDATE_ACCOUNT_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);
                    if (!jsonObject.has("token") || !jsonObject.has("data")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }
                    dataObject = jsonObject.getAsJsonObject("data");

                    if (!dataObject.has("name") && !dataObject.has("email") && !dataObject.has("password")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    email = dataObject.has("email") ? dataObject.get("email").getAsString() : null;
                    nome = dataObject.has("name") ? dataObject.get("name").getAsString() : null;
                    senha = dataObject.has("password") ? dataObject.get("password").getAsString() : null;
                    token = jsonObject.get("token").getAsString();
                    if(!isCandidate){
                        ramo = dataObject.has("industry") ? dataObject.get("industry").getAsString() : null;
                        descricao = dataObject.has("description") ? dataObject.get("description").getAsString() : null;
                    }

                    decJWT = verifyToken(token);
                    if (decJWT == null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_TOKEN", Collections.emptyMap());
                        break;
                    }

                    if (email != null && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    candidateId = Integer.parseInt(decJWT.getClaim("id").asString());

                    if (email != null) {
                        if(isCandidate){
                            sqlResult = DatabaseManager.readClienteCandidatoFromEmail(email);
                        }else{
                            sqlResult = DatabaseManager.readClienteEmpresaFromEmail(email);
                        }

                        if (sqlResult != null && sqlResult.getId() != candidateId) {
                            out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_EMAIL", Collections.emptyMap());
                            break;
                        }
                    }

                    // Construir um map<string,string> para os param que vao ser atualizados
                    Map<String, String> camposAtualizados = new HashMap<>();

                    if(email != null){
                        camposAtualizados.put("Email", email);
                    }

                    if(nome != null){
                        camposAtualizados.put("Nome", nome);
                    }

                    if(senha != null){
                        camposAtualizados.put("Senha", senha);
                    }

                    if(!isCandidate){
                        if(ramo != null){
                            camposAtualizados.put("Ramo", ramo);
                        }

                        if(descricao != null){
                            camposAtualizados.put("Descricao", descricao);
                        }
                    }
                    if(isCandidate){
                        if (DatabaseManager.updateClienteCandidato(candidateId, camposAtualizados) < 3) {
                            LOGGER.warning("Either the password or the name are the same and thus they weren't updated");
                        }
                    }else{
                        if (DatabaseManager.updateClienteEmpresa(candidateId, camposAtualizados) < 5) {
                            LOGGER.warning("Either the password or the name are the same and thus they weren't updated");
                        }
                    }

                    out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                    break;

                case "DELETE_ACCOUNT_RECRUITER":
                    isCandidate = false;
                case "DELETE_ACCOUNT_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);
                    if (!jsonObject.has("token")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    } else {
                        // Tem o field token
                        // Checar pra ver se token eh vazio
                        token = jsonObject.get("token").getAsString();
                        if (token.isEmpty()) {
                            out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                            break;
                        }
                    }


                    decJWT = verifyToken(token);
                    if (decJWT == null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_TOKEN", Collections.emptyMap());
                        break;
                    }

                    candidateId = Integer.parseInt(decJWT.getClaim("id").asString());
                    if (isCandidate) {
                        DatabaseManager.deleteClienteCandidato(candidateId);
                    }else{
                        DatabaseManager.deleteClienteEmpresa(candidateId);
                    }

                    out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                    break;
                default:
                    out = new MESSAGE_THREE_PARAMETERS("NAO_EXISTE", "", Collections.emptyMap());
            }
            if (jsonMessage == null) throw new JsonParseException("");

        } catch (Exception e) {
            out = new MESSAGE_THREE_PARAMETERS(null, "REJECTED", Collections.emptyMap());
        }

        String outJson = gson.toJson(out);
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(outJson);
            LOGGER.info("Sending to " + clientSocket.getInetAddress().toString() + " the message: " + outJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readMessages(BufferedReader buf) throws IOException {
        String tempMessage = null;
        try {
            tempMessage = buf.readLine();
        } catch (IOException e) {
            LOGGER.severe("Error reading message from buffer " + e.getMessage());
        }
        if (tempMessage != null) {
            processMessage(tempMessage);
        }
    }

    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                read();
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            // TODO decidir o que eu vou fazer aqui
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.severe("Client socket close error " + e);
            }
        }
    }

}
