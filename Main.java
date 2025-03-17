import java.time.LocalTime;
import java.util.*;

// Interfaces
interface PassengerCapable {
    int getPassengerCapacity();
}

interface CargoCapable {
    double getMaxPayload();
}

// Abstract Aircraft class
abstract class Aircraft implements Comparable<Aircraft> {
    protected String name;
    protected String model;
    protected String tailNumber;

    public Aircraft(String name, String model, String tailNumber) {
        this.name = name;
        this.model = model;
        this.tailNumber = tailNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Aircraft other) {
        return this.name.compareTo(other.name);
    }
}

// Specific aircraft types
class Airliner extends Aircraft implements PassengerCapable {
    private int passengerCapacity;

    public Airliner(String name, String model, String tailNumber, int passengerCapacity) {
        super(name, model, tailNumber);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
}

class Freighter extends Aircraft implements CargoCapable {
    private double maxPayload;

    public Freighter(String name, String model, String tailNumber, double maxPayload) {
        super(name, model, tailNumber);
        this.maxPayload = maxPayload;
    }

    @Override
    public double getMaxPayload() {
        return maxPayload;
    }
}

class Drone extends Aircraft {
    private int batteryLife;

    public Drone(String name, String model, String tailNumber, int batteryLife) {
        super(name, model, tailNumber);
        this.batteryLife = batteryLife;
    }
}
class Flight {
    private String flightId;
    private Aircraft aircraft;
    private LocalTime landingStart;
    private LocalTime landingEnd;

    public Flight(String flightId, Aircraft aircraft, LocalTime landingStart, LocalTime landingEnd) {
        this.flightId = flightId;
        this.aircraft = aircraft;
        this.landingStart = landingStart;
        this.landingEnd = landingEnd;
    }

    public LocalTime getLandingStart() {
        return landingStart;
    }

    public LocalTime getLandingEnd() {
        return landingEnd;
    }

    /*Verific daca zborururile se interseacteaza*/

    public boolean conflictsWith(Flight other) {
        return !(this.landingEnd.isBefore(other.landingStart) || this.landingStart.isAfter(other.landingEnd));
    }

    /*
      Daca avem avionul x care se intersecteaza cu avionul returneaza ca delay, nr de minute
      de la aterizarea lui x pana la aterizarea lui y + 1(ca sa nu mai se intersecteze)
     */

    public void delayLanding(LocalTime newStart) {
        long delayMinutes = java.time.Duration.between(this.landingStart, newStart).toMinutes();
        this.landingStart = newStart;
        this.landingEnd = this.landingEnd.plusMinutes(delayMinutes);
    }

    @Override
    public String toString() {
        return flightId + " (" + landingStart + " - " + landingEnd + ")";
    }
}
/*



 */
class Airport {
    private int numberOfRunways;
    private final PriorityQueue<List<Flight>> runwayQueue;

    public Airport(int numberOfRunways) {
        this.numberOfRunways = numberOfRunways;
        this.runwayQueue = new PriorityQueue<>(Comparator.comparingInt(List::size));
        for (int i = 0; i < numberOfRunways; i++) {
            runwayQueue.add(new ArrayList<>());
        }
    }

    /*Pun avioanele pe pista daca avionul curent se intersecteaza cu unul din avioanele
    de la pista i, nu il pun pe pista i, altfel il pun.
     */

    public int assignRunway(Flight flight) {
        for (List<Flight> runway : runwayQueue) {
            boolean canAssign = true;
            for (Flight f : runway) {
                if (flight.conflictsWith(f)) {
                    canAssign = false;
                    break;
                }
            }
            if (canAssign) {
                runway.add(flight);
                return runwayQueue.size();
            }
        }
        return -1;
    }

    /*
    Sortam dupa plecare si in caz de egalitate dupa sorire
    Nr de piste:3
        Avem de ex: (1 , 2) , (1 , 7), (2 , 3), (2 , 4), (3 , 5), (3, 7) , (4 , 9)
        O sa folosim o coada de prioritati, care va fi sortata dupa numarul de avioane de pe piste
        Prima data punem primele x avioane, cate piste sunt
        P1:(1 , 2)
        P2:(1 , 7)
        P3:(2 , 3)
        (2 , 4) -> se integreaza in P1(nu exista conflict)
        P1:(2 , 4)
        P2:(1 , 7)
        P3:(2 , 3)
        (3 , 5)- este conflict cu (2 . 4), intarziem avionul, (3 , 5) -> (4 , 6)
         P2:(1 , 7)
        P3:(2 , 3)
        P1:(3 , 5) , (4 , 6)
        (3 , 7) este in conflict cu (1 , 7), il intarziem cu (8 , 12)
        P3:(2 , 3)
        P1:(3 , 5) , (4 , 6)
        P2:(1 , 7), (8 , 12)
        Analog, (4 , 9) il intarziem
        Fiecare pista va avea 2 zboruri.
     */

    public void scheduleFlights(List<Flight> flights) {
        flights.sort(Comparator.comparing(Flight::getLandingStart).thenComparing(Flight::getLandingEnd));

        for (Flight flight : flights) {
            List<Flight> bestRunway = runwayQueue.poll();

            if (!bestRunway.isEmpty() && bestRunway.get(bestRunway.size() - 1).conflictsWith(flight)) {
                LocalTime newStart = bestRunway.get(bestRunway.size() - 1).getLandingEnd().plusMinutes(1);
                flight.delayLanding(newStart);
            }

            bestRunway.add(flight);
            runwayQueue.add(bestRunway);
        }
    }

    public void printSchedule() {
        int runwayNumber = 1;
        for (List<Flight> runway : runwayQueue) {
            System.out.println("Runway " + runwayNumber++ + ": " + runway);
        }
    }
}

// Main class
public class Main
{
    public static void main(String[] args)
    {
        Aircraft airliner = new Airliner("Boeing 747", "747-400", "N12345", 416);
        Aircraft freighter = new Freighter("Cargo King", "C-130", "C5678", 20000);
        Aircraft drone = new Drone("DroneX", "DX-200", "D999", 120);

        List<Flight> flights = Arrays.asList(
                new Flight("F101", airliner, LocalTime.of(10, 0), LocalTime.of(10, 30)),
                new Flight("F102", freighter, LocalTime.of(10, 15), LocalTime.of(10, 45)),
                new Flight("F103", drone, LocalTime.of(11, 0), LocalTime.of(11, 30))
        );

        Airport airport = new Airport(1);
        airport.scheduleFlights(flights);

        airport.printSchedule();
    }
}