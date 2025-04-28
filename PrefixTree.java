import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.HashMap;
import java.util.Map;

public class PrefixTree {
    private final Graph<String, DefaultEdge> graph;
    private final String root;
    private final Map<String, String> nodes;

    public PrefixTree() {
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        root = "";
        nodes = new HashMap<>();
        graph.addVertex(root);
    }

    public void insert(String word) {
        String current = root;
        for (char c : word.toCharArray()) {
            String next = current + c;
            if (!graph.containsVertex(next)) {
                graph.addVertex(next);
                graph.addEdge(current, next);
            }
            current = next;
        }
        nodes.put(word, current);
    }

    public boolean searchPrefix(String prefix) {
        String current = root;
        for (char c : prefix.toCharArray()) {
            String next = current + c;
            if (!graph.containsVertex(next)) {
                return false;
            }
            current = next;
        }
        return true;
    }

    public void dfsTraversal() {
        DepthFirstIterator<String, DefaultEdge> iterator = new DepthFirstIterator<>(graph, root);
        while (iterator.hasNext()) {
            String node = iterator.next();
            System.out.println(node);
        }
    }
}