import java.util.Arrays;
import java.util.List;

public class Game {
    public static void main(String[] args) {
        Bag bag = new Bag();
        Board board = new Board();

        List<String> dictionary = Arrays.asList(
                "CAT", "DOG", "FISH", "HAT", "BOOK", "COOL", "JAVA", "CODE", "LOVE", "GAME", "GAMER"
        );

        Player p1 = new Player("Alice", bag, board, dictionary);
        Player p2 = new Player("Bob", bag, board, dictionary);

        p1.start();
        p2.start();

        try {
            p1.join();
            p2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll words submitted:");
        board.printWords();
    }
}
