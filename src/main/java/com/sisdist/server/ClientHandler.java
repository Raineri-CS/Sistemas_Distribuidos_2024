package com.sisdist.server;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import com.sisdist.server.messages.IN_THREE_PARAMETERS;
import com.sisdist.server.messages.IN_TWO_PARAMETERS;
import com.sisdist.server.messages.Message;
import com.sisdist.server.messages.OUT_THREE_PARAMETERS;

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

    private DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // Se ele entrou aqui, quer dizer que o token nao eh valido
            return null;
        }
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
        // Definicao da mensagem que sempre vai sair
        OUT_THREE_PARAMETERS out = null;
        // Definicao de variaveis para ajuda na legibilidade
        String email = null;
        String nome = null;
        String senha = null;
        String token = null;
        String operation = null;
        // Definicao dos objetos a serem usados
        JsonObject jsonObject = null;
        JsonObject dataObject = null;
        Message jsonMessage = null;
        Candidato sqlResult = null;
        Map<String, String> tempData = new HashMap<>();
        DecodedJWT decJWT = null;


        try {
            jsonObject = JsonParser.parseString(message).getAsJsonObject();
            operation = jsonObject.get("operation").getAsString();
            switch (operation) {
                case "LOGIN_CANDIDATE":
                case "LOGIN_RECRUITER":
                    jsonMessage = gson.fromJson(jsonObject, IN_TWO_PARAMETERS.class);
                    // Para qualquer login, gerar o token para assinar o pacote
                    dataObject = jsonObject.get("data").getAsJsonObject();
                    email = dataObject.get("email").getAsString();
                    senha = dataObject.get("password").getAsString();
                    if (!email.isEmpty() && !senha.isEmpty()) {
                        // Informacoes vieram no pacote
                        sqlResult = DatabaseManager.readClienteCandidato(email);

                        if (sqlResult != null) {
                            if (senha.equals(sqlResult.getSenha())) {
                                // Senha correta, mandar o pacote de aceitacao
                                switch (operation) {
                                    case "LOGIN_CANDIDATE":
                                        token = genToken(sqlResult.getId(), "CANDIDATE");
                                        break;
                                    case "LOGIN_RECRUITER":
                                        token = genToken(sqlResult.getId(), "RECRUITER");
                                        break;
                                    default:
                                        System.err.println("ERRO NO OPERATION\n");
                                }
                                tempData.put("token", token);
                                out = new OUT_THREE_PARAMETERS(operation, "SUCCESS", tempData);
                            } else {
                                // Infos incorretas, mandar o pacote de recusa
                                out = new OUT_THREE_PARAMETERS(operation, "INVALID_FIELD", null);
                            }
                        }
                    } else {
                        // TODO tratar quando o pacote vir faltando dados
                    }

//                    token = genToken()
                    break;
                case "LOGOUT_CANDIDATE":
                case "LOGOUT_RECRUITER":
                    jsonMessage = gson.fromJson(jsonObject, IN_THREE_PARAMETERS.class);
                    // TODO as mensagens atribuindo pra out
                    break;
                case "SIGNUP_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, IN_TWO_PARAMETERS.class);
                    dataObject = jsonObject.getAsJsonObject("data");
                    nome = dataObject.get("name").getAsString();
                    email = dataObject.get("email").getAsString();
                    senha = dataObject.get("password").getAsString();

                    // Verifica se o email eh valido
                    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        out = new OUT_THREE_PARAMETERS(operation, "INVALID_FIELD", null);
                        break;
                    }

                    sqlResult = DatabaseManager.readClienteCandidato(email);

                    // Read volta nulo se nao existir
                    if (sqlResult == null) {
                        if (!nome.isBlank() && !email.isBlank() && !senha.isBlank()) {
                            // Se os campos vieram de verdade (email ja foi validado aqui
                            DatabaseManager.createClienteCandidato(nome, email, senha);
                            // Montar a mensagem de sucesso pro out
                            out = new OUT_THREE_PARAMETERS(operation, "SUCCESS", null);
                        } else {
                            // Se algum dos campos está em branco, não é possível criar o cliente
                            out = new OUT_THREE_PARAMETERS(operation, "INVALID_FIELD", null);
                        }
                    } else {
                        // FIXME tratar quando o cliente ja existe
                        // Se chegou aqui, significa que o cliente já existe
                    }
                    break;
                case "UPDATE_ACCOUNT_CANDIDATE":

                    jsonMessage = gson.fromJson(jsonObject, IN_THREE_PARAMETERS.class);

                    // Esses caras foram subidos, para que nao seja necessario colocar um if do email ali em baixo
                    dataObject = jsonObject.getAsJsonObject("data");
                    email = dataObject.get("email").getAsString();

                    token = jsonObject.get("token").getAsString();
                    if ((decJWT = verifyToken(token)) == null && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        // Deu merda, isso aqui nao eh um token valido
                        out = new OUT_THREE_PARAMETERS(operation, "INVALID_FIELD", null);
                    }
                    // Os caras a serem atualizados vem por aqui
                    nome = dataObject.get("name").getAsString();
                    senha = dataObject.get("password").getAsString();

                    // Nesse ponto, como o dataObject nao jogou nenhuma excecao, veio todos os campos
                    // TODO VER QUEM EH O USUARIO E SE O MESMO TEM PREVILEGIO DE EDICAO DE QUALQUER PORRA
                    // TODO Isso tem que ver com o professor provavelmente

                    DatabaseManager.updateClienteCandidato(Integer.parseInt(decJWT.getClaim("id").asString()), nome, email, senha);
                    break;
                default:
                    out = new OUT_THREE_PARAMETERS("NAO_EXISTE", null, null);
            }
            if (jsonMessage == null) throw new JsonParseException("");

        } catch (Exception e) {
//            e.printStackTrace();
            out = new OUT_THREE_PARAMETERS(null, "REJECTED", Collections.emptyMap());
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
