import java.util.*;
import java.util.stream.Collectors;

public class Player extends Thread {
    private final Bag bag;
    private final Board board;
    private final Dictionary dictionary;
    private final Object lock;
    protected boolean isMyTurn;
    private int score = 0;
    private final Random random = new Random();

    public Player(String name, Bag bag, Board board, Dictionary dictionary, Object lock, boolean isMyTurn) {
        super(name);
        this.bag = bag;
        this.board = board;
        this.dictionary = dictionary;
        this.lock = lock;
        this.isMyTurn = isMyTurn;
    }

    @Override
    public void run() {
        int iterationCount = 0;
        while (true) {
            synchronized (lock) {
                while (!isMyTurn) {
                    System.out.println(getName() + ": isMyTurn = " + isMyTurn); // Debugging
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                if (bag.isEmpty()) {
                    System.out.println(getName() + ": Bag is empty, exiting...");
                    lock.notify();
                    break;
                }

                List<Tile> tiles = bag.extractTiles(7);
                if (tiles.isEmpty()) {
                    System.out.println(getName() + ": No tiles to extract, exiting...");
                    lock.notify();
                    Game.gameOver = true;
                    break;
                }

                String letters = tiles.stream()
                        .map(tile -> String.valueOf(tile.getLetter()))
                        .collect(Collectors.joining());
                System.out.println(getName() + ": Extracted letters = " + letters);  // Debugging
                String word = findValidWord(letters);
                System.out.println(getName() + ": Found word = " + word);  // Debugging
                if (word != null) {
                    int wordPoints = calculatePoints(word, tiles);
                    board.submitWord(getName(), word, wordPoints);
                    score += wordPoints;
                }

                // Debugging after turn
                System.out.println(getName() + ": Finished turn, setting isMyTurn = false");

                // Schimbăm turnul
                Game.switchTurns(this, getOtherPlayer()); // Metodă care primește 'this' și cealaltă instanță de Player
                iterationCount++;
                System.out.println("Thread " + getName() + " is still running after " + iterationCount + " iterations.");
                lock.notify();

                try {
                    Thread.sleep(20); // Reducerea timpului de gândire
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }


            }
        }
    }

    // Getter pentru isMyTurn
    public boolean getIsMyTurn() {
        return this.isMyTurn;
    }

    // Setter pentru isMyTurn
    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    private String findValidWord(String letters) {
        List<String> possibleWords = dictionary.getWords()
                .stream()
                .filter(word -> canFormWord(word, letters))
                .collect(Collectors.toList());

        if (possibleWords.isEmpty()) return null;

        return possibleWords.get(random.nextInt(possibleWords.size()));
    }

    private boolean canFormWord(String word, String letters) {
        List<Character> available = new ArrayList<>();
        for (char c : letters.toCharArray()) {
            available.add(c);
        }
        for (char c : word.toCharArray()) {
            if (!available.remove((Character) c)) {
                return false;
            }
        }
        return true;
    }

    private int calculatePoints(String word, List<Tile> tiles) {
        int total = 0;
        for (char c : word.toCharArray()) {
            for (Tile tile : tiles) {
                if (tile.getLetter() == c) {
                    total += tile.getPoints();
                    break;
                }
            }
        }
        return total;
    }

    public int getScore() {
        return score;
    }


    // Metodă pentru a obține celălalt jucător
    private Player getOtherPlayer() {
        return this == Game.p1 ? Game.p2 : Game.p1;
    }
}
