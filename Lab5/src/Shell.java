
import java.util.*;

public class Shell {
    // Harta care asociază numele comenzilor cu obiectele Command corespunzătoare
    private final Map<String, Command> commands = new HashMap<>();

    // Repozitoriul care stochează toate imaginile
    private final ImageRepository repository = new ImageRepository();

    // Constructorul initializează comenzile disponibile
    public Shell() {
        commands.put("add", new AddCommand());      // Comandă pentru adăugare imagine
        commands.put("remove", new RemoveCommand()); // Comandă pentru ștergere imagine
        commands.put("update", new UpdateCommand()); // Comandă pentru actualizare imagine
        commands.put("load", new LoadCommand());     // Comandă pentru încărcare din fișier
        commands.put("save", new SaveCommand());     // Comandă pentru salvare în fișier
        commands.put("report", new ReportCommand()); // Comandă pentru generare raport HTML
    }

    // Metoda principală care rulează shell-ul (buclă de ascultare comenzi)
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) { // Buclă infinită până când se tastează 'exit'
            System.out.print("> "); // Afișează prompt-ul
            String line = scanner.nextLine();

            if (line.equalsIgnoreCase("exit")) break; // Ieșire dacă utilizatorul tastează "exit"

            String[] parts = line.trim().split(" "); // Împarte linia în cuvinte (comandă + argumente)

            Command cmd = commands.get(parts[0]);
            if (cmd == null) { // Dacă nu există comandă cu acel nume
                System.out.println("Unknown command: " + parts[0]);
                continue; // Treci la următoarea linie de comandă
            }

            try {
                cmd.execute(parts, repository); // Execută comanda, trimițând argumentele și repo-ul
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage()); // Prinde și afișează eventualele erori
            }
        }
    }
}
