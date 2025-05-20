package org.server;

import java.util.*;
// AtomicInteger permite manipularea thread-safe a valorilor de timp ramase pentru jucatori.
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession {
    private static final int BOARD_SIZE = 5;
    /*
    gameId: identificator unic al jocului.
    player1, player2: numele jucatorilor.
    vsAI: daca se joaca contra AI-ului.
    currentPlayer: cine are randul.
     */
    private final String gameId;
    private String player1;
    private String player2;
    private final boolean vsAI;
    private String currentPlayer;
    private final String[][] board;
    private final List<String> moveHistory = new ArrayList<>();
    /*
      Timp ramas pentru fiecare jucator, initial 60 de secunde (scade cu 5 secunde la fiecare mutare).
     */
    private final AtomicInteger time1 = new AtomicInteger(60);
    private final AtomicInteger time2 = new AtomicInteger(60);

    /*
      Mutarile fiecarui jucator (salvate ca stringuri x,y pentru usurinta)
     */
    private final Set<String> player1Moves = new HashSet<>();
    private final Set<String> player2Moves = new HashSet<>();

    // constructor
    public GameSession(String gameId, String playerName, boolean vsAI) {
        this.gameId = gameId;
        this.player1 = playerName;
        this.vsAI = vsAI;
        this.board = new String[BOARD_SIZE][BOARD_SIZE];
        this.currentPlayer = playerName;
        if (vsAI) {
            this.player2 = "AI";
        }
    }

    // Intr-un joc multiplayer, adauga al doilea jucator daca nu este deja setat. Este synchronized pentru a preveni acces concurent.
    public synchronized boolean addPlayer(String player) {
        if (!vsAI && player2 == null) {
            player2 = player;
            return true;
        }
        return false;
    }

    /*
    Jucatorul trimite o mutare (ex: "2,3"). Verifica:
    - Daca este randul jucatorului;
    - Daca mutarea este valida (in format corect, in interiorul tablei si celula este libera);
    - Actualizeaza tabla si timpul ramas;
    - Comuta jucatorul curent;
    - Salveaza mutarea in istoric;
    - Returneaza un mesaj descriptiv.
     */
    public synchronized String submitMove(String player, String move) {
        if (!player.equals(currentPlayer)) {
            return "It's not your turn.";
        }

        String[] coords = move.split(",");
        if (coords.length != 2) return "Invalid move format. Use x,y";

        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE || board[x][y] != null) {
            return "Invalid move: cell occupied or out of bounds.";
        }

        board[x][y] = player;

        if (player.equals(player1)) {
            time1.addAndGet(-5);
            if (time1.get() <= 0) return player1 + " time out. " + player2 + " wins!";
            player1Moves.add(move);
            currentPlayer = player2;
        } else {
            time2.addAndGet(-5);
            if (time2.get() <= 0) return player2 + " time out. " + player1 + " wins!";
            player2Moves.add(move);
            currentPlayer = player1;
        }

        moveHistory.add(player + ":" + move);
        return "Move registered: " + move + ". Next turn: " + currentPlayer;
    }

    // Pentru AI: cauta prima celula libera si joaca acolo. Foloseste submitMove("AI", move).
    public String autoMove() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                String move = x + "," + y;
                if (board[x][y] == null) {
                    return submitMove("AI", move);
                }
            }
        }
        return "No available moves for AI.";
    }

    // Verifica daca al doilea jucator nu s-a conectat inca (folosit pentru lobby).
    public boolean isWaiting() {
        return player2 == null;
    }

    // Returneaza daca jocul este impotriva AI.
    public boolean isVsAI() {
        return vsAI;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getStatus() {
        return String.format(
                "Game: %s\nPlayer1: %s\nPlayer2: %s\nCurrent turn: %s\nTime left - %s: %ds, %s: %ds\nPlayer1 path exists: %s\nPlayer2 path exists: %s",
                gameId,
                player1 != null ? player1 : "(waiting)",
                player2 != null ? player2 : "(waiting)",
                currentPlayer != null ? currentPlayer : "(none)",
                player1 != null ? player1 : "Player1", time1.get(),
                player2 != null ? player2 : "Player2", time2.get(),
                hasPath(player1Moves, true),
                hasPath(player2Moves, false)
        );
    }

    // Returneaza tabla sub forma de text: "." pentru liber, "X" pentru player1, "O" pentru player2.
    public String getBoardAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == null) {
                    sb.append(". ");
                } else if (board[i][j].equals(player1)) {
                    sb.append("X ");
                } else if (board[i][j].equals(player2)) {
                    sb.append("O ");
                } else {
                    sb.append("? ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Returneaza istoricul mutarilor, cate o mutare pe linie.
    public String getMoveHistory() {
        return String.join("\n", moveHistory);
    }

    /*
    Verifica daca jucatorul are un drum complet de la margine la margine:
    - player1: de la stanga la dreapta;
    - player2: de sus in jos;
    Se foloseste BFS pentru a cauta drumul prin mutarile sale.
     */
    private boolean hasPath(Set<String> moves, boolean isPlayer1) {
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        if (isPlayer1) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                String pos = i + ",0";
                if (moves.contains(pos)) {
                    queue.add(new int[]{i, 0});
                    visited.add(pos);
                }
            }
        } else {
            for (int j = 0; j < BOARD_SIZE; j++) {
                String pos = "0," + j;
                if (moves.contains(pos)) {
                    queue.add(new int[]{0, j});
                    visited.add(pos);
                }
            }
        }

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0], y = pos[1];

            if (isPlayer1 && y == BOARD_SIZE - 1) return true;
            if (!isPlayer1 && x == BOARD_SIZE - 1) return true;

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];
                String next = nx + "," + ny;
                if (nx >= 0 && nx < BOARD_SIZE && ny >= 0 && ny < BOARD_SIZE &&
                        moves.contains(next) && !visited.contains(next)) {
                    queue.add(new int[]{nx, ny});
                    visited.add(next);
                }
            }
        }

        return false;
    }
}
