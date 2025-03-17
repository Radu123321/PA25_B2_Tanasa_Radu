import java.util.*;
import java.util.stream.Collectors;

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

    /*Facem Override la CompareTo ca sa ne ajute la sortarea de la TreeSet*/
    /*Controleaza cand le punem in Treeset*/

    @Override
    public int compareTo(Location other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}

class Robot {
    private Location currentLocation;

    public Robot(Location startLocation) {
        this.currentLocation = startLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void moveTo(Location newLocation) {
        this.currentLocation = newLocation;
    }
}

class MapGraph {
    private final Map<Location, Map<Location, Double>> adjacencyMap = new HashMap<>();

    public void addLocation(Location location) {
        adjacencyMap.putIfAbsent(location, new HashMap<>());
    }
    /*
        formam graful
     */
    public void addPath(Location from, Location to, double time, double probability) {
        adjacencyMap.get(from).put(to, time);
        adjacencyMap.get(to).put(from, time);
    }

    /* Vericam daca locatia exista*/
    /*Dacă locația există, returnează lista de vecini și timpul necesar pentru fiecare*/

    public Map<Location, Double> getNeighbors(Location location) {
        return adjacencyMap.getOrDefault(location, Collections.emptyMap());
    }
}

public class Laborator4Compulsory {
    public static void main(String[] args) {
        List<Location> locations = Arrays.asList(
                new Location("Base Alpha", LocationType.FRIENDLY),
                new Location("Checkpoint 1", LocationType.NEUTRAL),
                new Location("Checkpoint 2", LocationType.NEUTRAL),
                new Location("Enemy Camp", LocationType.ENEMY),
                new Location("Outpost", LocationType.FRIENDLY),
                new Location("Radar Station", LocationType.ENEMY)
        );

        /*
          Sortam locatiile prietenoase dupa nume
         */

        TreeSet<Location> friendlyLocations = locations.stream()
                .filter(l -> l.getType() == LocationType.FRIENDLY)
                .collect(Collectors.toCollection(TreeSet::new));

        System.out.println("Friendly locations:");
        friendlyLocations.forEach(System.out::println);

        // Sortam dupa tip si nume
        LinkedList<Location> enemyLocations = locations.stream()
                .filter(l -> l.getType() == LocationType.ENEMY)
                .sorted(Comparator.comparing(Location::getName))
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println("\nEnemy locations:");
        enemyLocations.forEach(System.out::println);

        // Create a map graph
        MapGraph map = new MapGraph();
        locations.forEach(map::addLocation);

        // Adding connections
        map.addPath(locations.get(0), locations.get(1), 10, 0.9);
        map.addPath(locations.get(1), locations.get(2), 15, 0.8);
        map.addPath(locations.get(2), locations.get(3), 20, 0.7);
        map.addPath(locations.get(3), locations.get(4), 25, 0.6);
        map.addPath(locations.get(4), locations.get(5), 30, 0.5);

        // Initialize robot and move it
        Robot robot = new Robot(locations.get(0));
        System.out.println("\nRobot starts at: " + robot.getCurrentLocation());
        robot.moveTo(locations.get(1));
        System.out.println("Robot moved to: " + robot.getCurrentLocation());
    }
}