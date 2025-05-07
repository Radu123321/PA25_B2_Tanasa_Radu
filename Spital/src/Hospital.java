import java.util.List;

public class Hospital {

    private int id;
    private String name;
    private final int capacity;
    private List<Employee> employees;
    private List<Salon> salons;

    public Hospital(int id, int capacity, String name) {
        this.capacity = capacity;
        this.name = name;
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public List<Salon> getSalons() {
        return salons;
    }
    public void setSalons(List<Salon> salons) {
        this.salons = salons;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
