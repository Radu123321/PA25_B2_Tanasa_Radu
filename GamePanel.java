import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel {
    private final List<Point> dots = new ArrayList<>(); // Lista cu puncte generate aleator
    private final List<Edge> edges = new ArrayList<>(); // Lista cu liniile trase între puncte
    private Point selectedDot = null; // Punctul selectat la primul click
    private boolean playerOneTurn = true; // Alternanță între jucători (true = jucător 1)

    public GamePanel() {
        generateDots(10); // Generează 10 puncte aleator la început

        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clicked = findClosestDot(e.getPoint()); // Găsește cel mai apropiat punct de click

                if (selectedDot == null) {
                    selectedDot = clicked; // Prima selecție
                } else {
                    if (!selectedDot.equals(clicked)) {
                        edges.add(new Edge(selectedDot, clicked, playerOneTurn)); // Adaugă o linie
                        playerOneTurn = !playerOneTurn; // Schimbă jucătorul
                    }
                    selectedDot = null; // Resetează selecția
                    repaint(); // Reafișează panoul pentru a desena modificările
                }
            }
        });
    }

    private void generateDots(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(700) + 50; // Coord x între 50 și 750 (evită marginile)
            int y = rand.nextInt(500) + 50; // Coord y între 50 și 550
            dots.add(new Point(x, y));
        }
    }

    private Point findClosestDot(Point click) {
        return dots.stream()
                .min(Comparator.comparingDouble(dot -> click.distance(dot)))
                .orElse(null); // Returnează cel mai apropiat punct de locul click-ului
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenează punctele
        for (Point dot : dots) {
            g.setColor(Color.BLACK);
            g.fillOval(dot.x - 5, dot.y - 5, 10, 10); // Mic cerc cu raza 5px
        }

        // Desenează liniile între puncte
        for (Edge edge : edges) {
            g.setColor(edge.playerOne ? Color.BLUE : Color.RED);
            g.drawLine(edge.a.x, edge.a.y, edge.b.x, edge.b.y);
        }
    }
}
