package org.server;

import org.server.ClientThread;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class GameServer {
    /// Variabilă care indică dacă serverul trebuie să continue să ruleze.
    private boolean running = true;
    /// Creezi un ServerSocket care ascultă pe portul primit.
    ///Acceptă conexiuni de la clienți.
    /// Creează câte un ClientThread pentru fiecare
    public GameServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                new ClientThread(clientSocket, this).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    ///  Metodă care oprește serverul (se va ieși din bucla while de mai sus).
    public void stop() {
        running = false;
    }
    /// Creează și pornește serverul pe portul 12345.
    public static void main(String[] args) {
        new GameServer(12345);
    }
}
