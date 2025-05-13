package org.server;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    /// – Creeaza o clasă care extinde Thread.
    /// Adica rulează într-un fir separat pentru fiecare client.
    private final Socket socket;
    private final GameServer server;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }
    /// Metoda de apel cand incepe thread ul
    @Override
    public void run() {
        try (
                /// Pregătește fluxuri pentru a citi comenzi de la client și a trimite răspunsuri.
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            /// Citim comenzile intr o bucla pana la finlizare(exit)
            String request;
            while ((request = in.readLine()) != null) {
                if ("stop".equalsIgnoreCase(request)) {
                    out.println("Server stopped");
                    server.stop();
                    break;
                } else {
                    out.println("Server received the request: " + request);
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        }
    }
}
