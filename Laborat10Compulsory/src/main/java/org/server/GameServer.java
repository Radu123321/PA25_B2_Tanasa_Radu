package org.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {
    /*
    serverSocket: obiectul care asculta conexiunile pe un port.
    running: indica daca serverul este activ.
    games: dictionar thread-safe care mapeaza gameId → GameSession.
     */
    private ServerSocket serverSocket;
    private boolean running = true;
    private final ConcurrentHashMap<String, GameSession> games = new ConcurrentHashMap<>();
    /*
      Creeaza un ServerSocket pe portul dat si afiseaza mesaj de pornire.
      NU se foloseste try-with-resources aici deoarece serverSocket trebuie inchis manual in metoda stop().
     */
    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port); // NU în try-with-resources
            System.out.println("Server started on port " + port);
            /*Intra intr-o bucla in care:
            Asteapta conexiuni noi (accept() blocheaza pana vine un client);
            Creeaza si porneste un nou ClientThread pentru fiecare client.
            */
            while (running) {
                Socket clientSocket = serverSocket.accept();
                new ClientThread(this, clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    // inchide socketul serverului
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }
    //Returneaza starea serverului (activ/inactiv).
    public boolean isRunning() {
        return running;
    }
    ///Permite accesul la lista jocurilor active.
    public ConcurrentHashMap<String, GameSession> getGames() {
        return games;
    }
    ///  porneste serverul
    public static void main(String[] args) {
        new GameServer(12345);
    }
}
