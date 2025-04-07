import java.awt.*;
import java.io.Serializable;

///  convertit in flux de bytes

class Edge implements Serializable {
    Point a, b;
    boolean playerOne;

    public Edge(Point a, Point b, boolean playerOne) {
        this.a = a;
        this.b = b;
        this.playerOne = playerOne;
    }

    public double getLength() {
        return a.distance(b);
    }
}
