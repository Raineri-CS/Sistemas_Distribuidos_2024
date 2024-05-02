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
import com.sisdist.common.messages.MESSAGE_THREE_PARAMETERS_WITH_TOKEN;
import com.sisdist.common.messages.MESSAGE_TWO_PARAMETERS;
import com.sisdist.common.messages.Message;
import com.sisdist.common.messages.MESSAGE_THREE_PARAMETERS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String magicWord = "DISTRIBUIDOS";

    private final Algorithm algorithm = Algorithm.HMAC256(magicWord);

    private static final Logger logger = Logger.getLogger((ClientHandler.class).toString());

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
            if (!clientSocket.isClosed()){
                BufferedReader buf = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
                readMessages(buf);
            }else{
                // Bom, o cliente fechou a socket, logo, pode-se sair seguramente

            }
        } catch (IOException e) {
            System.err.println("Error in ClientHandler read()" + e);
        }
    }

    private void processMessage(String message) {
        logger.info("Arrived at processMessage with client " + clientSocket.getInetAddress() + " message: " + message);
        Gson gson = new Gson();
        // Definicao da mensagem que sempre vai sair
        MESSAGE_THREE_PARAMETERS out = null;
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
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_TWO_PARAMETERS.class);
                    // Para qualquer login, gerar o token para assinar o pacote
                    dataObject = jsonObject.get("data").getAsJsonObject();
                    email = dataObject.get("email").getAsString();
                    senha = dataObject.get("password").getAsString();
                    if (!email.isEmpty() && !senha.isEmpty()) {
                        // Informacoes vieram no pacote
                        sqlResult = DatabaseManager.readClienteCandidatoFromEmail(email);

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
                                out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", tempData);
                            } else {
                                // Infos incorretas, mandar o pacote de recusa
                                // TODO INCONSISTENCIAS DO DOCUMENTO, VERIFICAR COM A TURMA
                                out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                            }
                        }else{
                            // Se entrar aqui nao achou
                            out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_LOGIN", Collections.emptyMap());
                        }
                    } else {
                        // TODO tratar quando o pacote vir faltando dados
                    }

//                    token = genToken()
                    break;
                case "LOGOUT_CANDIDATE":
                case "LOGOUT_RECRUITER":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);
                    // TODO as mensagens atribuindo pra out
                    break;
                case "SIGNUP_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_TWO_PARAMETERS.class);
                    dataObject = jsonObject.getAsJsonObject("data");
                    nome = dataObject.get("name").getAsString();
                    email = dataObject.get("email").getAsString();
                    senha = dataObject.get("password").getAsString();

                    // Verifica se o email eh valido
                    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }

                    sqlResult = DatabaseManager.readClienteCandidatoFromEmail(email);

                    // Read volta nulo se nao existir
                    if (sqlResult == null) {
                        if (!nome.isBlank() && !email.isBlank() && !senha.isBlank()) {
                            // Se os campos vieram de verdade (email ja foi validado aqui
                            DatabaseManager.createClienteCandidato(nome, email, senha);
                            // Montar a mensagem de sucesso pro out
                            out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                        } else {
                            // Se algum dos campos está em branco, não é possível criar o cliente
                            out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        }
                    } else {
                        // FIXME tratar quando o cliente ja existe
                        // Se chegou aqui, significa que o cliente já existe
                    }
                    break;
                case "LOOKUP_ACCOUNT_CANDIDATE":
                    // FIXME usar o gson.fromJson eh uma boa ideia no papel, mas eu to lendo os campos direto do jsonObject, talves pipocar tudo? @joaokrejci
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);
                    // NOTE lembrar que esse email eh o email que o dono do token quer consultar
//                    email = jsonObject.get("email").getAsString();
                    token = jsonObject.get("token").getAsString();
                    if ((decJWT = verifyToken(token)) == null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }
//                    if(email == null){
//                        sqlResult = DatabaseManager.readClienteCandidatoFromEmail(email);
//                    }else{
                    sqlResult = DatabaseManager.readClienteCandidato(Integer.parseInt(decJWT.getClaim("id").asString()));
//                    }

                    if(sqlResult == null){
                        // FIXME QUE ODIO, OS MLK ESQUECERAM DESSE CASO
                        out = new MESSAGE_THREE_PARAMETERS(operation, "USER_NOT_FOUND", Collections.emptyMap());
                    }else{
                        // Reutilizando o pointer de dataObject que nao eh nem usado nessa thread se cair nesse caso mesmo
                        tempData = Map.of("email", sqlResult.getEmail(),"name", sqlResult.getNome(),"password",sqlResult.getSenha());
                        out = new MESSAGE_THREE_PARAMETERS(operation,"SUCCESS",tempData);
                    }


                    break;
                case "UPDATE_ACCOUNT_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);

                    // Esses caras foram subidos, para que nao seja necessario colocar um if do email ali em baixo
                    dataObject = jsonObject.getAsJsonObject("data");
                    email = dataObject.get("email").getAsString();
                    token = jsonObject.get("token").getAsString();

                    // FIXME porra caralho buceta os dois documentos falam coisas diferentes
                    if ((decJWT = verifyToken(token)) == null && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        // Deu merda, isso aqui nao eh um token valido
                        // FIXME tem inconsistencias nos documentos, confirmar se vai voltar INVALID_FIELD pra todo erro ou se o erro do token vai mesmo voltar INVALID_TOKEN
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_FIELD", Collections.emptyMap());
                        break;
                    }
                    // Os caras a serem atualizados vem por aqui
                    nome = dataObject.get("name").getAsString();
                    senha = dataObject.get("password").getAsString();

                    // Nesse ponto, como o dataObject nao jogou nenhuma excecao, veio todos os campos
                    // TODO VER QUEM EH O USUARIO E SE O MESMO TEM PREVILEGIO DE EDICAO DE QUALQUER PORRA
                    // TODO Isso tem que ver com o professor provavelmente

                    // NOTE sim, isso eh HORRIVEL, mas o intellij ficava me ENCHENDO O SACO de getClaim poder soltar um nullpointerexception
                    Integer tempInt = null;
                    if (decJWT != null) {
                        tempInt = decJWT.getClaim("id").asInt();
                    }
                    sqlResult = DatabaseManager.readClienteCandidato(tempInt != null ? tempInt : -1);
                    if (sqlResult != null) {
                        // Fodeu, quer dizer que encontrou um usuario com esse email que eu quero colocar (email eh um valor unico no banco)
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_EMAIL", Collections.emptyMap());
                    } else {
                        // Po, os dados tao certos, decJWT veio sem ser nulo, e o email que eu quero colocar nesse novo user nao esta sendo utilizado, entao manda bala
                        DatabaseManager.updateClienteCandidato(tempInt != null ? tempInt : -1, nome, email, senha);
                        out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                    }
                    break;
                case "DELETE_ACCOUNT_CANDIDATE":
                    jsonMessage = gson.fromJson(jsonObject, MESSAGE_THREE_PARAMETERS_WITH_TOKEN.class);

                    token = jsonObject.get("token").getAsString();
//                    dataObject = jsonObject.getAsJsonObject("data");

                    if ((decJWT = verifyToken(token)) == null) {
                        out = new MESSAGE_THREE_PARAMETERS(operation, "INVALID_TOKEN", Collections.emptyMap());
                        break;
                    }

                    DatabaseManager.deleteClienteCandidato(Integer.parseInt(decJWT.getClaim("id").asString()));

                    out = new MESSAGE_THREE_PARAMETERS(operation, "SUCCESS", Collections.emptyMap());
                    break;
                default:
                    out = new MESSAGE_THREE_PARAMETERS("NAO_EXISTE", "", Collections.emptyMap());
            }
            if (jsonMessage == null) throw new JsonParseException("");

        } catch (Exception e) {
//            e.printStackTrace();
            out = new MESSAGE_THREE_PARAMETERS(null, "REJECTED", Collections.emptyMap());
        }

        String outJson = gson.toJson(out);
        logger.info("Sending to " + clientSocket.getInetAddress().toString() + " the message: " + outJson);
        try {
//            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( clientSocket.getOutputStream() ) );
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println((outJson.strip()));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readMessages(BufferedReader buf) throws IOException {
        processMessage(buf.readLine());
    }

    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try{
            if(!Thread.currentThread().isInterrupted()){
                read();
            }else{
                throw new InterruptedException();
            }
        }catch (InterruptedException e){
            // TODO decidir o que eu vou fazer aqui
        }
    }
}
