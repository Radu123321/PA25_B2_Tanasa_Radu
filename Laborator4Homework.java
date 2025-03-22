import com.github.javafaker.Faker;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum LocationType {
    FRIENDLY, NEUTRAL, ENEMY
}

class Location implements Comparable<Location> {
    private final String name;
    private final LocationType type;

    public Location(String name, LocationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public LocationType getType() {
        return type;
    }

    @Override
    public int compareTo(Location other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}

record RouteStats(double probability, Map<LocationType, Long> typeCounts) {}

public class Laborator4Homework {
    public static void main(String[] args) {
        Faker faker = new Faker();
        Random random = new Random();

        int numLocations = 50;
        List<Location> locations = generateRandomLocations(numLocations, faker);

        Graph<Location, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        locations.forEach(graph::addVertex);

        // Adăugăm muchii aleatorii cu probabilități
        Map<DefaultWeightedEdge, Double> edgeProbabilities = new HashMap<>();
        for (int i = 0; i < numLocations; i++) {
            for (int j = 0; j < 3; j++) {
                int to = random.nextInt(numLocations);
                if (i != to) {
                    DefaultWeightedEdge e = graph.addEdge(locations.get(i), locations.get(to));
                    if (e != null) {
                        double probability = 0.5 + (0.5 * random.nextDouble());
                        double weight = -Math.log(probability); // Pentru a maximiza produsul => minimizăm -log
                        graph.setEdgeWeight(e, weight);
                        edgeProbabilities.put(e, probability);
                    }
                }
            }
        }

        List<RouteStats> allStats = new ArrayList<>();
        DijkstraShortestPath<Location, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);

        for (Location source : locations) {
            for (Location target : locations) {
                if (!source.equals(target)) {
                    GraphPath<Location, DefaultWeightedEdge> path = dijkstra.getPath(source, target);
                    if (path != null) {
                        double totalProb = path.getEdgeList().stream()
                                .map(edgeProbabilities::get)
                                .reduce(1.0, (a, b) -> a * b);

                        Map<LocationType, Long> counts = path.getVertexList().stream()
                                .collect(Collectors.groupingBy(Location::getType, Collectors.counting()));

                        allStats.add(new RouteStats(totalProb, counts));
                    }
                }
            }
        }

        // Statistici generale
        double avgProb = allStats.stream()
                .mapToDouble(RouteStats::probability)
                .average().orElse(0);

        long totalRoutes = allStats.size();
        long totalFriendly = allStats.stream()
                .flatMap(r -> r.typeCounts().entrySet().stream())
                .filter(e -> e.getKey() == LocationType.FRIENDLY)
                .mapToLong(Map.Entry::getValue).sum();

        long totalNeutral = allStats.stream()
                .flatMap(r -> r.typeCounts().entrySet().stream())
                .filter(e -> e.getKey() == LocationType.NEUTRAL)
                .mapToLong(Map.Entry::getValue).sum();

        System.out.printf("\nTotal routes: %d\n", totalRoutes);
        System.out.printf("Average safety probability: %.4f\n", avgProb);
        System.out.printf("Total FRIENDLY locations in all routes: %d\n", totalFriendly);
        System.out.printf("Total NEUTRAL locations in all routes: %d\n", totalNeutral);
    }

    public static List<Location> generateRandomLocations(int count, Faker faker) {
        Random rand = new Random();
        return IntStream.range(0, count)
                .mapToObj(i -> new Location(
                        faker.address().cityName() + "_" + i,
                        LocationType.values()[rand.nextInt(LocationType.values().length)]
                )).collect(Collectors.toList());
    }
}
