package org.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) {
        /// Adresa si portul serverului. localhost inseamna ca serverul ruleaza pe acelasi calculator. Portul trebuie sa fie acelasi ca in GameServer.
        String serverAddress = "localhost";
        int port = 12345;

        //Deschide conexiunea cu serverul
        try (
                Socket socket = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server. Type a command:");
            // Citeste o comanda de la utilizator si o trimite catre server. Daca utilizatorul tasteaza exit, clientul se opreste.
            while (true) {
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command)) {
                    break;
                }

                out.println(command);

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("__END__")) break;
                    if (!line.trim().isEmpty()) {
                        System.out.println("Server response: " + line);
                    }
                }
            }
            //  In caz de eroare (ex: serverul nu e pornit), afiseaza mesajul de eroare.
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}