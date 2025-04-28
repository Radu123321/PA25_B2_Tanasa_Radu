public class Timekeeper extends Thread {
    private final long timeLimit;
    private final Game game;

    public Timekeeper(long timeLimit, Game game) {
        this.timeLimit = timeLimit;
        this.game = game;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeLimit) {
            // Afișează timpul rămas sau orice altceva ai nevoie
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Game running for: " + elapsedTime / 1000 + " seconds.");

            try {
                Thread.sleep(1000); // Așteaptă o secundă înainte de a verifica din nou
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.gameOver = true;
        }

        // Oprește jocul după ce a expirat timpul
        game.stopGame();
    }
}
