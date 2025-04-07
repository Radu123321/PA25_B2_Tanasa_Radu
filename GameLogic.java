import java.awt.Point;
import java.util.*;

public class GameLogic {
    public static double calculateMST(List<Point> dots) {
        List<Edge> allEdges = getAllEdges(dots);
        return kruskalMST(dots, allEdges);
    }

    private static List<Edge> getAllEdges(List<Point> dots) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < dots.size(); i++) {
            for (int j = i + 1; j < dots.size(); j++) {
                edges.add(new Edge(dots.get(i), dots.get(j), false));
            }
        }
        return edges;
    }

    private static double kruskalMST(List<Point> dots, List<Edge> edges) {
        edges.sort(Comparator.comparingDouble(Edge::getLength));
        DisjointSet<Point> ds = new DisjointSet<>(dots);

        double total = 0;
        int added = 0;
        for (Edge e : edges) {
            if (ds.union(e.a, e.b)) {
                total += e.getLength();
                added++;
                if (added == dots.size() - 1) break;
            }
        }

        return total;
    }

    public static List<List<Edge>> generateSpanningTrees(List<Point> dots, int maxCount) {
        List<Edge> allEdges = getAllEdges(dots);
        allEdges.sort(Comparator.comparingDouble(Edge::getLength)); // prioritizăm cele mai scurte

        List<List<Edge>> spanningTrees = new ArrayList<>();
        generateTreesBacktrack(dots, allEdges, new ArrayList<>(), new DisjointSet<>(dots), 0, spanningTrees, maxCount);

        spanningTrees.sort(Comparator.comparingDouble(GameLogic::totalCost));
        return spanningTrees;
    }

    private static void generateTreesBacktrack(
            List<Point> dots,
            List<Edge> allEdges,
            List<Edge> current,
            DisjointSet<Point> ds,
            int index,
            List<List<Edge>> results,
            int maxCount
    ) {
        if (current.size() == dots.size() - 1) {
            results.add(new ArrayList<>(current));
            return;
        }

        if (index >= allEdges.size() || results.size() >= maxCount) return;

        for (int i = index; i < allEdges.size(); i++) {
            Edge edge = allEdges.get(i);

            if (ds.find(edge.a) != ds.find(edge.b)) {
                current.add(edge);
                DisjointSet<Point> copy = new DisjointSet<>(ds);
                copy.union(edge.a, edge.b);

                generateTreesBacktrack(dots, allEdges, current, copy, i + 1, results, maxCount);
                current.remove(current.size() - 1);
            }
        }
    }

    private static double totalCost(List<Edge> tree) {
        return tree.stream().mapToDouble(Edge::getLength).sum();
    }

    private static class DisjointSet<T> {
        private final Map<T, T> parent = new HashMap<>();

        public DisjointSet(Collection<T> elements) {
            for (T e : elements) parent.put(e, e);
        }

        public DisjointSet(DisjointSet<T> other) {
            for (Map.Entry<T, T> entry : other.parent.entrySet()) {
                this.parent.put(entry.getKey(), entry.getValue());
            }
        }

        public T find(T x) {
            T root = x;
            while (!parent.get(root).equals(root)) {
                root = parent.get(root);
            }
            T curr = x;
            while (!curr.equals(root)) {
                T next = parent.get(curr);
                parent.put(curr, root);
                curr = next;
            }
            return root;
        }

        public boolean union(T a, T b) {
            T rootA = find(a);
            T rootB = find(b);
            if (rootA.equals(rootB)) return false;
            parent.put(rootA, rootB);
            return true;
        }
    }
}
