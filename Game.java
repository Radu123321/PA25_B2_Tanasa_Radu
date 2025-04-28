public class Game {
    public static final long TIME_LIMIT = 60000; // 60 de secunde
    private static final Object lock = new Object();
    public static Player p1, p2; // Adaugă variabilele statice pentru p1 și p2
    public static boolean gameOver = false;
    public static void main(String[] args) {
        Bag bag = new Bag();
        Board board = new Board();
        Dictionary dictionary = new Dictionary("words.txt");

        // Creăm p1 și p2
        p1 = new Player("Alice", bag, board, dictionary, lock, true);
        p2 = new Player("Bob", bag, board, dictionary, lock, false);

        // Creăm și pornim thread-ul de cronometrare
        Timekeeper timekeeper = new Timekeeper(TIME_LIMIT, new Game());
        timekeeper.setDaemon(true); // Setăm thread-ul ca daemon pentru a nu bloca închiderea programului
        timekeeper.start();

        // Pornim jucătorii
        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);

        t1.start();
        t2.start();

        try {
            // Așteptăm ca ambele thread-uri să termine
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll words submitted:");
        board.printWords();

        if (p1.getScore() > p2.getScore()) {
            System.out.println("\nWinner: " + p1.getName() + " with " + p1.getScore() + " points!");
        } else if (p2.getScore() > p1.getScore()) {
            System.out.println("\nWinner: " + p2.getName() + " with " + p2.getScore() + " points!");
        } else {
            System.out.println("\nIt's a tie! Both players have " + p1.getScore() + " points!");
        }
    }

    // Metodă pentru a opri jocul
    public void stopGame() {
        System.out.println("Stopping the game...");
        gameOver = true;
    }

    // Metoda de schimbare a turnului
    public static void switchTurns(Player p1, Player p2) {
        synchronized (lock) {
            System.out.println("Switching turns between players...");
            p1.setMyTurn(false);
            p2.setMyTurn(true);
            lock.notifyAll();
            System.out.println("Turn switched: " + p1.getName() + " isMyTurn=" + p1.isMyTurn + ", " + p2.getName() + " isMyTurn=" + p2.isMyTurn);
        }
    }
    public static boolean isGameOver() {
        return gameOver;
    }
}
