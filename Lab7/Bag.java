import java.util.*;

public class Bag {
    private final List<Tile> tiles = new ArrayList<>();
    private final Random random = new Random();

    public Bag() {
        for (char c = 'A'; c <= 'Z'; c++) {
            int points = 1 + random.nextInt(10);
            for (int i = 0; i < 10; i++) {
                tiles.add(new Tile(c, points));
            }
        }
        Collections.shuffle(tiles);
    }

    public synchronized List<Tile> extractTiles(int count) {
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i < count && !tiles.isEmpty(); i++) {
            result.add(tiles.remove(0));
        }
        return result;
    }

    public synchronized boolean isEmpty() {
        return tiles.isEmpty();
    }

    public synchronized int remainingTiles() {
        return tiles.size();
    }
}
