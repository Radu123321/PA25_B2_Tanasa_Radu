import java.util.List;
import java.util.stream.Collectors;

public class Salon extends Room{

    private final int capacity;
    private List<Pacient> pacients;
    Salon(int id, String name) {
        super(id, name);
        capacity = 10;
    }

    public List<Pacient> getPacients() {
        return pacients;
    }

    public void setPacients(List<Pacient> pacients) {
        this.pacients = pacients;
    }

    public void addPacient(Pacient pacient) {
        pacients.add(pacient);
    }

    public void deletePacient(int id) {
        pacients.stream().filter(t->t.getId()==id).collect(Collectors.toList()).removeFirst();
    }


}
