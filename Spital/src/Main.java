import java.util.List;

public class Main {
    public static void main(String[] args) {
        Hospital hospital=new Hospital(1,10, "Socola");

        Employee employee1 = new Employee(1,"Vasile Popescu", 54);
        Employee employee2 = new Employee(2,"Vasile Popescu", 54);
        Employee employee3 = new Employee(3,"Vasile Popescu", 54);
        Employee employee4 = new Employee(4,"Vasile Popescu", 54);
        Employee employee5 = new Employee(5,"Vasile Popescu", 54);
        Employee employee6 = new Employee(6,"Vasile Popescu", 54);
        Employee employee7 = new Employee(7,"Vasile Popescu", 54);
        hospital.setEmployees(List.of(employee1,employee2,employee3,employee4,employee5,employee6,employee7));


        Pacient pacient1= new Pacient(1,"Celalalt Vasile Popescu", 45);
        Pacient pacient2= new Pacient(2,"Celalalt Vasile Popescu", 45);
        Pacient pacient3= new Pacient(3,"Celalalt Vasile Popescu", 45);
        Pacient pacient4= new Pacient(4,"Celalalt Vasile Popescu", 45);
        Pacient pacient5= new Pacient(5,"Celalalt Vasile Popescu", 45);
        Pacient pacient6= new Pacient(6,"Celalalt Vasile Popescu", 45);
        Pacient pacient7= new Pacient(7,"Celalalt Vasile Popescu", 45);
        Pacient pacient8= new Pacient(8,"Celalalt Vasile Popescu", 45);
        Pacient pacient9= new Pacient(9,"Celalalt Vasile Popescu", 45);
        Pacient pacient10= new Pacient(10,"Celalalt Vasile Popescu", 45);
        Pacient pacient11= new Pacient(11,"Celalalt Vasile Popescu", 45);
        Pacient pacient12= new Pacient(12,"Celalalt Vasile Popescu", 45);
        Pacient pacient13= new Pacient(13,"Celalalt Vasile Popescu", 45);
        Pacient pacient14= new Pacient(14,"Celalalt Vasile Popescu", 45);
        Pacient pacient15= new Pacient(15,"Celalalt Vasile Popescu", 45);
        Pacient pacient16= new Pacient(16,"Celalalt Vasile Popescu", 45);
        Pacient pacient17= new Pacient(17,"Celalalt Vasile Popescu", 45);
        Pacient pacient18= new Pacient(18,"Celalalt Vasile Popescu", 45);


        Salon salon1= new Salon(1,"Camera 1");
        Salon salon2= new Salon(2,"Camera 2");
        Salon salon3= new Salon(3,"Camera 3");
        Salon salon4= new Salon(4,"Camera 4");

        salon1.setPacients(List.of(pacient1,pacient2, pacient3, pacient4, pacient5));
        salon2.setPacients(List.of(pacient6,pacient7));
        salon3.setPacients(List.of(pacient8,pacient9, pacient10, pacient11, pacient12, pacient13, pacient14, pacient15, pacient16));
        salon4.setPacients(List.of(pacient17,pacient18));

        hospital.setSalons(List.of(salon1,salon2,salon3, salon4));
        int x=1;
    }
}