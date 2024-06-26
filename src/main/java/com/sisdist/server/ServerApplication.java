package com.sisdist.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static com.sisdist.server.Server.PORT;

public class ServerApplication extends Thread {
    private volatile boolean shutdownRequest = false;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private static final Logger LOGGER = Logger.getLogger(ServerApplication.class.toString());

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!this.shutdownRequest){
                Socket socket = serverSocket.accept();
                LOGGER.info("Accepted connection from " + socket.getRemoteSocketAddress());
                //socket.setSoTimeout(1000);
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.err.println("Couldnt create socket at ServerApplication run()" + e);
        }
        System.out.println("Ouvindo por conexoes na porta " + PORT);
    }

    public void terminate() {
        this.shutdownRequest = true;
    }
}
