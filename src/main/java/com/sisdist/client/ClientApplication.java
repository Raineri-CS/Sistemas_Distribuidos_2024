package com.sisdist.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sisdist.common.messages.MESSAGE_THREE_PARAMETERS_WITH_TOKEN;
import com.sisdist.common.messages.MESSAGE_TWO_PARAMETERS;
import com.sisdist.common.messages.Message;
import com.sisdist.common.util.ConsoleUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

// TODO verificar os cases de novo antes da apresentacao

public class ClientApplication {
    private static final Logger LOGGER = Logger.getLogger("ClientApplication");
    private final String IP;
    private final int PORT;
    private Integer option = 999;
    private String token = "";

    private boolean isCandidate = true;

    private Socket sock = null;

    private Message msg;
    private JsonObject jsonObject;

    // FIXME tirar isso quando a UI ficar pronta
    private BufferedReader reader;

    public ClientApplication(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
        try {
            sock = new Socket();
            sock.connect(new InetSocketAddress(this.IP, this.PORT));
        } catch (IOException e) {
            LOGGER.severe("Error creating socket");

        }
    }

    public void start() {
        // FIXME tirar isso quando a ui estiver pronta
        String input;
        reader = new BufferedReader((new InputStreamReader(System.in)));

        System.out.println("1 - Candidato\n2 - Empresa");
        try{
            input = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (input.equalsIgnoreCase("2")) {
            isCandidate = false;
        }

        do{
            // NOTE, NAO LIMPAR O CONSOLE DEPOIS QUE SAIR DO CRUD PARA PODER VER O QUE VEIO NO LOGGER (ESTA PRINTANDO NO STDOUT)
            if(isCandidate){
                System.out.println("MODO CANDIDATO");
            }else{
                System.out.println("MODO EMPRESA");
            }

            printMenu();
            try {
                input = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ConsoleUtils.clearConsole();

            if (input == null) {
                System.out.println("Entrada inválida. Por favor, tente novamente.");
                continue;
            } else {
                option = Integer.parseInt(input.trim());
            }

            switch (option) {
                case 1:
                    if(isCandidate){
                        cadastrarClienteCandidato();
                    }else{
                        cadastrarClienteEmpresa();
                    }
                    break;
                case 2:
                    if (!token.isBlank()){
                        if(isCandidate){
                            lerClienteCandidato();
                        }else{
                            lerClienteEmpresa();
                        }
                    }
                    else System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                    break;
                case 3:
                    if (!token.isBlank()) {
                        if(isCandidate){
                            atualizarClienteCandidato();
                        }else{
                            atualizarClienteEmpresa();
                        }
                    }
                    else System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                    break;
                case 4:
                    if (!token.isBlank()) {
                        if(isCandidate){
                            deletarClienteCandidato();
                        }else{
                            deletarClienteEmpresa();
                        }
                    }
                    else System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                    break;
                case 5:
                    if (!token.isBlank()) {
                        logout();
                    } else {
                        login();
                    }
                    break;
                case 0:
                    System.out.println("Saindo do programa...");
                    try{
                        sock.close();
                    }catch (IOException e){
                        LOGGER.severe("Error closing socket");
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                    break;
            }
        }while (option != 0);
    }

    private void printMenu() {
        System.out.println("Escolha uma das opções abaixo:");
        System.out.println("1 - Cadastrar \"Cliente\" (C)");
        if (!token.isBlank()) {
            System.out.println("2 - Ler um \"Cliente\" (R)");
            System.out.println("3 - Atualizar um \"Cliente\" (U)");
            System.out.println("4 - Deletar um \"Cliente\" (D)");
            System.out.println("5 - Logout da conta \"Cliente\" (Logout)");
        } else {
            System.out.println("5 - Logar em uma conta \"Cliente\" (Login)");
        }

        System.out.println("0 - Sair");
        System.out.println("\nInsira a opção:");
    }

    private void cadastrarClienteCandidato() {
        // Vou precisar processar JSON aqui
        Gson gson = new Gson();
        String nome, email, senha;
        System.out.println("Para cadastrar um usuario do tipo \"Cliente_Candidato\" vao ser necessarias as seguintes informacoes");
        System.out.println("Confirme com enter");
        System.out.println("Nome ");
        try {
            nome = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Email");
        try {
            email = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Senha");
        try {
            senha = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (senha.isBlank() || email.isBlank() || nome.isBlank()) {
            System.out.println("Os dados foram informados incorretamente, aperte enter para voltar para o menu principal");
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Map<String, String> data = Map.of("name", nome, "email", email, "password", senha);
        msg = new MESSAGE_TWO_PARAMETERS("SIGNUP_CANDIDATE", data);
        String outMsg = gson.toJson(msg);


        // Finalmente, enviar a mensagem formada ao servidor
        String response = sendMsg(outMsg);

        jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String result = jsonObject.get("status").getAsString();
        if (!result.isBlank()) {
            switch (result) {
                case "SUCCESS":
                    // Tudo certo
                    return;
                case "INVALID_FIELD":
                    System.out.println("Email inválido, isso pode ocorrer se o email estiver mal formatado ou se já estiver registrado.");
                    System.out.println("(pressione enter para continuar)");
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                case "REJECTED":
                    // NOTE isso nao esta na spec da turma, eh pra teste meu
                    LOGGER.severe("UEPA, ERROU AI FOI? NUM CONXEGUE");
                    return;
                default:
                    // Putz, dai o server errou
                    LOGGER.severe("Server sent malformed message, check logs to learn more.");
            }
        } else {
            // Just... how did you arrive here?
            LOGGER.severe("This piece of code should NEVER be executed @cadastrarClienteCandidato");
        }

    }

    private void lerClienteCandidato() {
        // FIXME aqui seria onde eu perguntaria quem pesquisar, mas no doc a gente so manda o token, entao ele so pode se procurar
        Gson gson = new Gson();

        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("LOOKUP_ACCOUNT_CANDIDATE", this.token, Collections.emptyMap());
        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);

        if (!response.isBlank()) {
            jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonObject tempData = jsonObject.get("data").getAsJsonObject();
            switch (jsonObject.get("status").getAsString()) {
                case "SUCCESS":
                    // Tudo certo, dale
                    System.out.println("----------------------");
                    System.out.println("Candidato encontrado!");
                    System.out.println("----------------------");
                    System.out.println("Nome");
                    System.out.println(tempData.get("name").getAsString());
                    System.out.println("Email");
                    System.out.println(tempData.get("email").getAsString());
                    System.out.println("Senha");
                    System.out.println(tempData.get("password").getAsString());
                    System.out.println("----------------------");
                    return;
                case "INVALID_FIELD":
                case "USER_NOT_FOUND":
                    System.out.println("Erro ao procurar usuario.");
                    return;
                default:
                    break;
            }

        } else {
            LOGGER.severe("This piece of code should NEVER be executed @lerClienteCandidato");
        }
    }

    private void atualizarClienteCandidato() {
        Gson gson = new Gson();
        String nome, email, senha;
        System.out.println("[UPDATE]");
        System.out.println("Confirme com enter");
        System.out.println("Novo nome ");

        try {
            nome = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Novo email");
        try {
            email = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Nova senha");
        try {
            senha = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (senha.isBlank() || email.isBlank() || nome.isBlank()) {
            System.out.println("Os dados foram informados incorretamente, aperte enter para voltar para o menu principal");
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Map<String, String> data = Map.of("name", nome, "email", email, "password", senha);

        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("UPDATE_ACCOUNT_CANDIDATE", this.token, data);

        String outMsg = gson.toJson(msg);


        String response = sendMsg(outMsg);

        jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String result = jsonObject.get("status").getAsString();
        if (!result.isBlank()) {
            switch (result) {
                case "SUCCESS":
                    // Tudo certo
                    System.out.println("Candidato atualizado com sucesso!");
                    return;
                case "INVALID_EMAIL":
                case "INVALID_FIELD":
                    System.out.println("Email inválido, isso pode ocorrer se o email estiver mal formatado ou se já estiver registrado.");
                    System.out.println("(pressione enter para continuar)");
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                case "REJECTED":
                    // NOTE isso nao esta na spec da turma, eh pra teste meu
                    LOGGER.severe("UEPA, ERROU AI FOI? NUM CONXEGUE");
                    return;
                default:
                    // Putz, dai o server errou
                    LOGGER.severe("Server sent malformed message, check logs to learn more.");
            }
        } else {
            // Just... how did you arrive here?
            LOGGER.severe("This piece of code should NEVER be executed @cadastrarClienteCandidato");
        }
    }

    private void deletarClienteCandidato() {
        // Thats stupid but the docs say for you to pass the jwt token as a param, that means you have to delete yourself
        // NOTE sudoku
        Gson gson = new Gson();

        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("DELETE_ACCOUNT_CANDIDATE", this.token, Collections.emptyMap());
        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);

        if (!response.isBlank()) {
            jsonObject = JsonParser.parseString(response).getAsJsonObject();
            switch (jsonObject.get("status").getAsString()) {
                case "SUCCESS":
                    // Tudo certo, dale
                    System.out.println("----------------------");
                    System.out.println("Candidato deletado!");
                    System.out.println("----------------------");
                    logout();
//                    this.token = "";
                    return;
                case "INVALID_FIELD":
                case "USER_NOT_FOUND":
                case "INVALID_TOKEN":
                    System.out.println("Erro ao deletar usuario.");
                    return;
                default:
                    break;
            }

        } else {
            LOGGER.severe("This piece of code should NEVER be executed @lerClienteCandidato");
        }
    }

    private void cadastrarClienteEmpresa() {
        Gson gson = new Gson();
        String nome, email, senha, ramo, descricao;
        System.out.println("Para cadastrar um usuario do tipo \"Cliente Empresa\" vao ser necessarias as seguintes informacoes");
        System.out.println("Confirme com enter");
        System.out.println("Nome");
        try {
            nome = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Email");
        try {
            email = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Senha");
        try {
            senha = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Ramo");
        try {
            ramo = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Descricao");
        try {
            descricao = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (senha.isBlank() || email.isBlank() || nome.isBlank() || ramo.isBlank() || descricao.isBlank()) {
            System.out.println("Os dados foram informados incorretamente, aperte enter para voltar para o menu principal");
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Map<String, String> data = Map.of("name", nome, "email", email, "password", senha, "industry", ramo, "description", descricao);
        msg = new MESSAGE_TWO_PARAMETERS("SIGNUP_RECRUITER", data);
        String outMsg = gson.toJson(msg);


        // Finalmente, enviar a mensagem formada ao servidor
        String response = sendMsg(outMsg);

        jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String result = jsonObject.get("status").getAsString();
        if (!result.isBlank()) {
            switch (result) {
                case "SUCCESS":
                    // Tudo certo
                    return;
                case "INVALID_FIELD":
                    System.out.println("Email inválido, isso pode ocorrer se o email estiver mal formatado ou se já estiver registrado.");
                    System.out.println("(pressione enter para continuar)");
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                case "REJECTED":
                    // NOTE isso nao esta na spec da turma, eh pra teste meu
                    LOGGER.severe("UEPA, ERROU AI FOI? NUM CONXEGUE");
                    return;
                default:
                    // Putz, dai o server errou
                    LOGGER.severe("Server sent malformed message, check logs to learn more.");
            }
        } else {
            // Just... how did you arrive here?
            LOGGER.severe("This piece of code should NEVER be executed @cadastrarClienteCandidato");
        }

    }

    private void lerClienteEmpresa() {
        // FIXME aqui seria onde eu perguntaria quem pesquisar, mas no doc a gente so manda o token, entao ele so pode se procurar
        Gson gson = new Gson();

        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("LOOKUP_ACCOUNT_RECRUITER", this.token, Collections.emptyMap());
        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);

        if (!response.isBlank()) {
            jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonObject tempData = jsonObject.get("data").getAsJsonObject();
            switch (jsonObject.get("status").getAsString()) {
                case "SUCCESS":
                    // Tudo certo, dale
                    System.out.println("----------------------");
                    System.out.println("Candidato encontrado!");
                    System.out.println("----------------------");
                    System.out.println("Nome");
                    System.out.println(tempData.get("name").getAsString());
                    System.out.println("Email");
                    System.out.println(tempData.get("email").getAsString());
                    System.out.println("Senha");
                    System.out.println(tempData.get("password").getAsString());
                    System.out.println("----------------------");
                    return;
                case "INVALID_FIELD":
                case "USER_NOT_FOUND":
                    System.out.println("Erro ao procurar usuario.");
                    return;
                default:
                    break;
            }

        } else {
            LOGGER.severe("This piece of code should NEVER be executed @lerClienteCandidato");
        }
    }

    private void atualizarClienteEmpresa() {
        Gson gson = new Gson();
        String nome, email, senha, ramo, descricao;
        System.out.println("[UPDATE]");
        System.out.println("Confirme com enter");
        System.out.println("Novo nome ");

        try {
            nome = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Novo email");
        try {
            email = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Nova senha");
        try {
            senha = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Novo ramo");
        try {
            ramo = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Nova descricao");
        try {
            descricao = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (senha.isBlank() || email.isBlank() || nome.isBlank() || ramo.isBlank() || descricao.isBlank()) {
            System.out.println("Os dados foram informados incorretamente, aperte enter para voltar para o menu principal");
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        Map<String, String> data = Map.of("name", nome, "email", email, "password", senha, "industry", ramo, "description", descricao);
        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("UPDATE_ACCOUNT_RECRUITER", this.token, data);

        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);

        jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String result = jsonObject.get("status").getAsString();
        if (!result.isBlank()) {
            switch (result) {
                case "SUCCESS":
                    // Tudo certo
                    System.out.println("Candidato atualizado com sucesso!");
                    return;
                case "INVALID_EMAIL":
                case "INVALID_FIELD":
                    System.out.println("Email inválido, isso pode ocorrer se o email estiver mal formatado ou se já estiver registrado.");
                    System.out.println("(pressione enter para continuar)");
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                case "REJECTED":
                    // NOTE isso nao esta na spec da turma, eh pra teste meu
                    LOGGER.severe("UEPA, ERROU AI FOI? NUM CONXEGUE");
                    return;
                default:
                    // Putz, dai o server errou
                    LOGGER.severe("Server sent malformed message, check logs to learn more.");
            }
        } else {
            // Just... how did you arrive here?
            LOGGER.severe("This piece of code should NEVER be executed @cadastrarClienteCandidato");
        }
    }

    private void deletarClienteEmpresa() {
        // Thats stupid but the docs say for you to pass the jwt token as a param, that means you have to delete yourself
        // NOTE sudoku
        Gson gson = new Gson();

        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("DELETE_ACCOUNT_RECRUITER", this.token, Collections.emptyMap());
        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);

        if (!response.isBlank()) {
            jsonObject = JsonParser.parseString(response).getAsJsonObject();
            switch (jsonObject.get("status").getAsString()) {
                case "SUCCESS":
                    // Tudo certo, dale
                    System.out.println("----------------------");
                    System.out.println("Candidato deletado!");
                    System.out.println("----------------------");
                    logout();
//                    this.token = "";
                    return;
                case "INVALID_FIELD":
                case "USER_NOT_FOUND":
                case "INVALID_TOKEN":
                    System.out.println("Erro ao deletar usuario.");
                    return;
                default:
                    break;
            }

        } else {
            LOGGER.severe("This piece of code should NEVER be executed @lerClienteCandidato");
        }
    }


    private void login() {
        Gson gson = new Gson();
        String email, senha;

        try {
            System.out.println("Informe o seu email:");
            email = reader.readLine();
            System.out.println("Informe a sua senha:");
            senha = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> tempData = Map.of("email", email, "password", senha);
        if(isCandidate){
            msg = new MESSAGE_TWO_PARAMETERS("LOGIN_CANDIDATE", tempData);
        }else{
            msg = new MESSAGE_TWO_PARAMETERS("LOGIN_RECRUITER", tempData);
        }
        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);
        if (!response.isBlank()) {
            jsonObject = JsonParser.parseString(response).getAsJsonObject();
            String result = jsonObject.get("status").getAsString();

            switch (result) {
                case "SUCCESS":
                    //Tudo certo, settar o token vindo do server
                    JsonObject dataObj = jsonObject.get("data").getAsJsonObject();
                    this.token = dataObj.get("token").getAsString();
                    return;
                // NOTE ate a turma decidir a maneira certa vou deixar os 2
                case "INVALID_LOGIN":
                case "INVALID_FIELD":
                    System.out.println("Email ou senha invalido(s)");
                    return;
                default:
                    break;
            }

        } else {
            LOGGER.severe("This piece of code should NEVER be executed @lerClienteCandidato");
        }
    }

    private void logout() {
        Gson gson = new Gson();
        msg = new MESSAGE_THREE_PARAMETERS_WITH_TOKEN("LOGOUT_CANDIDATE", this.token, Collections.emptyMap());

        String outMsg = gson.toJson(msg);

        String response = sendMsg(outMsg);

        jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String result = jsonObject.get("status").getAsString();
        if (!result.isBlank()) {
            switch (result) {
                case "SUCCESS":
                    // Tudo certo
                    System.out.println("Candidato fez logout com sucesso!");
                    this.token = "";
                    return;
                case "INVALID_FIELD":
                    System.out.println("Algum dos campos foi enviado em branco (ou o servidor interpretou errado).");
                    System.out.println("(pressione enter para continuar)");
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                case "INVALID_TOKEN":
                    System.out.println("O servidor acionou que o token veio errado");
                    System.out.println("(pressione enter para continuar)");
                    try {
                        reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "REJECTED":
                    // NOTE isso nao esta na spec da turma, eh pra teste meu
                    LOGGER.severe("UEPA, ERROU AI FOI? NUM CONXEGUE");
                    return;
                default:
                    // Putz, dai o server errou
                    LOGGER.severe("Server sent malformed message, check logs to learn more.");
            }
        } else {
            // Just... how did you arrive here?
            LOGGER.severe("This piece of code should NEVER be executed @cadastrarClienteCandidato");
        }
    }

    private String sendMsg(String outMsg) {

        if (sock.isConnected()) {
            try {
                PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);

                LOGGER.info("Sending message " + outMsg + " to the server");

                // Enviar o JSON usando o printWriter
                writer.println(outMsg.strip());

                BufferedReader buf = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String line;
                line = buf.readLine();
                if (line == null) {
                    // Deu merda
                    LOGGER.severe("Server message arrived NULL, closing socket and throwing IOException");
                    throw new IOException();
                }
                LOGGER.info("Client recieved " + line + " from server");
                return line;
            } catch (IOException e) {
                LOGGER.severe("Server message arrived IOException, closing socket and throwing IOException");
            }
        } else {
            LOGGER.severe("Couldnt connect to server");
        }

        return "";
    }

}