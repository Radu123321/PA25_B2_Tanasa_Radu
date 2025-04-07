import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel {
    private final GameState state;
    private Point draggingPoint = null;
    private Point currentMouse = null;
    private List<Edge> highlightedTree = null;

    public GamePanel(int numDots) {
        this.state = new GameState();
        if (numDots > 0) generateDots(numDots);
        setupListeners();
    }

    public GamePanel(GameState loadedState) {
        this.state = loadedState;
        setupListeners();
    }

    private void setupListeners() {
        MouseInputAdapter mouseHandler = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clicked = findClosestDot(e.getPoint());

                if (state.selectedDot == null) {
                    state.selectedDot = clicked;
                } else {
                    if (!state.selectedDot.equals(clicked)) {
                        boolean moved = handleHumanMove(state.selectedDot, clicked);
                        if (moved && !state.playerOneTurn) {
                            aiMove(2); // dificultate maximă
                        }
                    }
                    state.selectedDot = null;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                draggingPoint = findClosestDot(e.getPoint());
                currentMouse = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                currentMouse = e.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingPoint != null) {
                    Point releasedPoint = findClosestDot(e.getPoint());
                    if (releasedPoint != null && !releasedPoint.equals(draggingPoint)) {
                        boolean moved = handleHumanMove(draggingPoint, releasedPoint);
                        if (moved && !state.playerOneTurn) {
                            aiMove(2); // dificultate maximă
                        }
                    }
                }
                draggingPoint = null;
                currentMouse = null;
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private void generateDots(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(700) + 50;
            int y = rand.nextInt(500) + 50;
            state.dots.add(new Point(x, y));
        }
    }

    private Point findClosestDot(Point click) {
        return state.dots.stream()
                .min(Comparator.comparingDouble(dot -> click.distance(dot)))
                .orElse(null);
    }

    public void resetGame() {
        state.resetEdges();
        highlightedTree = null;
        repaint();
    }

    public GameState getState() {
        return state;
    }

    public void highlightTree(List<Edge> tree) {
        this.highlightedTree = tree;
        repaint();
    }

    // ✅ Mutare jucător uman – dacă e validă, se aplică și se returnează true
    public boolean handleHumanMove(Point a, Point b) {
        boolean exists = state.edges.stream().anyMatch(edge ->
                (edge.a.equals(a) && edge.b.equals(b)) ||
                        (edge.a.equals(b) && edge.b.equals(a))
        );

        if (!exists && !a.equals(b)) {
            state.edges.add(new Edge(a, b, state.playerOneTurn));
            state.playerOneTurn = !state.playerOneTurn;
            repaint();
            return true;
        }
        return false;
    }

    // ✅ Mutare AI în funcție de dificultate
    public void aiMove(int difficulty) {
        if (state.playerOneTurn) return;

        List<List<Edge>> trees = GameLogic.generateSpanningTrees(state.dots, 10);
        if (trees.isEmpty()) return;

        int index = switch (difficulty) {
            case 0 -> trees.size() - 1;
            case 1 -> trees.size() / 2;
            default -> 0;
        };

        List<Edge> selectedTree = trees.get(Math.min(index, trees.size() - 1));

        for (Edge e : selectedTree) {
            boolean exists = state.edges.stream().anyMatch(existing ->
                    (existing.a.equals(e.a) && existing.b.equals(e.b)) ||
                            (existing.a.equals(e.b) && existing.b.equals(e.a))
            );
            if (!exists) {
                state.edges.add(new Edge(e.a, e.b, false)); // AI = jucător 2
                state.playerOneTurn = true;
                repaint();
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Point dot : state.dots) {
            g.setColor(Color.BLACK);
            g.fillOval(dot.x - 5, dot.y - 5, 10, 10);
        }

        for (Edge edge : state.edges) {
            g.setColor(edge.playerOne ? Color.BLUE : Color.RED);
            g.drawLine(edge.a.x, edge.a.y, edge.b.x, edge.b.y);
        }

        if (draggingPoint != null && currentMouse != null) {
            g.setColor(Color.GRAY);
            g.drawLine(draggingPoint.x, draggingPoint.y, currentMouse.x, currentMouse.y);
        }

        if (highlightedTree != null) {
            for (Edge edge : highlightedTree) {
                g.setColor(Color.GREEN);
                g.drawLine(edge.a.x, edge.a.y, edge.b.x, edge.b.y);
            }
        }
    }
}
