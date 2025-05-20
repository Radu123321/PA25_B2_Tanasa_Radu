package org.server;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    /*
      server: referinta catre obiectul GameServer (pentru acces la jocuri).
    socket: conexiunea socket cu clientul.
    playerName: numele jucatorului, cerut la conectare.
     */
    private final GameServer server;
    private final Socket socket;
    private String playerName;

    public ClientThread(GameServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                /*
             Deschide canale de intrare si iesire;
            in: pentru a citi comenzi de la client;
            out: pentru a trimite raspunsuri.

                 */
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Welcome to the Game Server. Please enter your name:");
            playerName = in.readLine();
            out.println("Hello, " + playerName + ". Type your commands:");
            out.println("__END__");

            String request;
            while ((request = in.readLine()) != null) {
                if (request.trim().isEmpty()) continue;
                String[] tokens = request.trim().split(" ");
                String command = tokens[0];
                String response;

                switch (command.toLowerCase()) {
                    case "stop":
                        server.stop();
                        response = "Server stopped";
                        break;
                    case "create":
                        if (tokens.length < 2) {
                            response = "Usage: create <gameId> [ai]";
                        } else {
                            String gameId = tokens[1];
                            boolean vsAI = tokens.length > 2 && tokens[2].equalsIgnoreCase("ai");
                            GameSession game = new GameSession(gameId, playerName, vsAI);
                            server.getGames().put(gameId, game);
                            response = "Game " + gameId + " created." + (vsAI ? " Playing against AI." : "");
                            if (vsAI && !game.getCurrentPlayer().equals(playerName)) {
                                response += "\n" + game.autoMove();
                            }
                        }
                        break;
                    case "join":
                        if (tokens.length < 2) {
                            response = "Usage: join <gameId>";
                        } else {
                            String gameId = tokens[1];
                            GameSession game = server.getGames().get(gameId);
                            if (game == null) {
                                response = "Game not found.";
                            } else if (game.addPlayer(playerName)) {
                                response = "Joined game " + gameId;
                            } else {
                                response = "Game is full or AI game.";
                            }
                        }
                        break;
                    case "move":
                        if (tokens.length < 3) {
                            response = "Usage: move <gameId> <move>";
                        } else {
                            String gameId = tokens[1];
                            String move = tokens[2];
                            GameSession game = server.getGames().get(gameId);
                            if (game == null) {
                                response = "Game not found.";
                            } else {
                                response = game.submitMove(playerName, move);
                                if (game.isVsAI() && game.getCurrentPlayer().equals("AI")) {
                                    response += "\n" + game.autoMove();
                                }
                            }
                        }
                        break;
                    case "status":
                        if (tokens.length < 2) {
                            response = "Usage: status <gameId>";
                        } else {
                            String gameId = tokens[1];
                            GameSession game = server.getGames().get(gameId);
                            if (game == null) {
                                response = "Game not found.";
                            } else {
                                response = "--- Status ---\n" + game.getStatus() + "\n--- Board ---\n" + game.getBoardAsString();
                            }
                        }
                        break;
                    case "list":
                        if (server.getGames().isEmpty()) {
                            response = "No games available.";
                        } else {
                            StringBuilder sb = new StringBuilder();
                            for (String id : server.getGames().keySet()) {
                                GameSession g = server.getGames().get(id);
                                sb.append(id)
                                        .append(" - ")
                                        .append(g.isWaiting() ? "waiting" : "in progress")
                                        .append(g.isVsAI() ? " (vs AI)" : "")
                                        .append("\n");
                            }
                            response = sb.toString();
                        }
                        break;
                    case "moves":
                        if (tokens.length < 2) {
                            response = "Usage: moves <gameId>";
                        } else {
                            String gameId = tokens[1];
                            GameSession game = server.getGames().get(gameId);
                            if (game == null) {
                                response = "Game not found.";
                            } else {
                                response = game.getMoveHistory();
                            }
                        }
                        break;
                    default:
                        response = "Unknown command.";
                }
                //Trimite raspunsul pentru comanda si un marker __END__ pentru finalul raspunsului.
                out.println(response);
                out.println("__END__");
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Client communication error: " + e.getMessage());
        }
    }
}
