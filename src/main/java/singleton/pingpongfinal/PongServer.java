package singleton.pingpongfinal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PongServer {
    private static final int PORT = 5555;
    private static final int MAX_CLIENTS = 2;
    private List<PongClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        new PongServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (clients.size() < MAX_CLIENTS) {
                Socket clientSocket = serverSocket.accept();
                PongClientHandler clientHandler = new PongClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("Client connected. Total clients: " + clients.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message) {
        for (PongClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}