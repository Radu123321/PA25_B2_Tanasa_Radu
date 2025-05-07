// Comandă pentru ștergerea unei imagini din repository
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args, ImageRepository repository) throws Exception {
        if (args.length < 2) { // Verificăm dacă avem numele imaginii
            System.out.println("Usage: remove <name>");
            return;
        }
        String name = args[1];
        repository.removeImage(name); // Încercăm să ștergem imaginea după nume
    }
}