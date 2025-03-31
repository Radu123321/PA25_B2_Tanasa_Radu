import java.awt.*;

class Edge {
    Point a, b; // Capetele liniei
    boolean playerOne; // Cine a tras linia (true = jucătorul 1)

    public Edge(Point a, Point b, boolean playerOne) {
        this.a = a;
        this.b = b;
        this.playerOne = playerOne;
    }

    public double getLength() {
        return a.distance(b); // Distanța între cele două puncte
    }
}