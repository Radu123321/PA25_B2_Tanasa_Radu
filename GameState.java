import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    public final List<Point> dots = new ArrayList<>();
    public final List<Edge> edges = new ArrayList<>();
    public Point selectedDot = null;
    public boolean playerOneTurn = true;

    public void resetEdges() {
        edges.clear();
        selectedDot = null;
        playerOneTurn = true;
    }

    public double getPlayerScore(boolean playerOne) {
        return edges.stream()
                .filter(edge -> edge.playerOne == playerOne)
                .mapToDouble(Edge::getLength)
                .sum();
    }
}
