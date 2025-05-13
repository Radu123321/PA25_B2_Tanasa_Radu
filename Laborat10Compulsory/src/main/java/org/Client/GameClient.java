package org.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) {
        /*
         Punctul de intrare al programului curent
         Defineste adresa ip si portul serverului "localhost" pe aceeadi masina
         */
        String serverAddress = "localhost";
        int port = 12345;
        /*
          Creeam:
        o conexiune la server (Socket)
        un stream de ieșire (out) pentru a trimite comenzi
        un stream de intrare (in) pentru a primi răspunsuri
        un scanner pentru a citi comenzi de la tastatură
        Acestea sunt gestionate automat de try-with-resources
        Se inchid automat la final
         */
        try (
                Socket socket = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server. Type a command:");
            /// Citim comenzile de la client
            while (true) {
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command)) {
                    break;
                }

                out.println(command);
                String response = in.readLine();
                System.out.println("Server response: " + response);
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
