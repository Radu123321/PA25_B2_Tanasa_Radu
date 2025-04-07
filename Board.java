import java.util.*;

public class Board {
    private final List<String> submittedWords = new ArrayList<>();

    public synchronized void submitWord(String player, String word, int points) {
        submittedWords.add(player + " submitted: " + word + " (" + points + " points)");
    }

    public void printWords() {
        submittedWords.forEach(System.out::println);
    }
}
