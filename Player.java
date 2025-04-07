import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player extends Thread {
    private final String name;
    private final Bag bag;
    private final Board board;
    private final List<String> dictionary;
    private final List<Tile> rack = new ArrayList<>();
    private int score = 0;

    public Player(String name, Bag bag, Board board, List<String> dictionary) {
        this.name = name;
        this.bag = bag;
        this.board = board;
        this.dictionary = dictionary;
    }
    private int calculateScore(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            for (Tile tile : rack) {
                if (tile.getLetter() == c) {
                    score += tile.getPoints();
                    break; // folosim o singură dată fiecare literă
                }
            }
        }
        return score;
    }


    @Override
    public void run() {
        rack.addAll(bag.extractTiles(7));
        while (!bag.isEmpty()) {
            Optional<String> maybeWord = findWordFromRack();

            if (maybeWord.isPresent()) {
                String word = maybeWord.get();
                int wordScore = calculateScore(word);
                score += wordScore;
                board.submitWord(name, word, wordScore);
                removeTilesUsed(word);
                rack.addAll(bag.extractTiles(word.length()));
            } else {
                rack.clear();
                rack.addAll(bag.extractTiles(7));
            }

            try {
                Thread.sleep(100); // simulate thinking time
            } catch (InterruptedException ignored) {}
        }

        System.out.println(name + " final score: " + score);
    }

    private Optional<String> findWordFromRack() {
        String rackLetters = rack.stream().map(t -> String.valueOf(t.getLetter())).reduce("", String::concat);
        for (String word : dictionary) {
            if (canBuildWord(word, rackLetters)) {
                return Optional.of(word);
            }
        }
        return Optional.empty();
    }

    private boolean canBuildWord(String word, String rackLetters) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : rackLetters.toCharArray()) freq.merge(c, 1, Integer::sum);
        for (char c : word.toCharArray()) {
            if (!freq.containsKey(c) || freq.get(c) == 0) return false;
            freq.put(c, freq.get(c) - 1);
        }
        return true;
    }

    private void removeTilesUsed(String word) {
        for (char c : word.toCharArray()) {
            for (int i = 0; i < rack.size(); i++) {
                if (rack.get(i).getLetter() == c) {
                    rack.remove(i);
                    break;
                }
            }
        }
    }
}
