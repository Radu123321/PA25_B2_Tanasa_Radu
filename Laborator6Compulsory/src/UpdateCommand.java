// Comandă pentru actualizarea numelui și/sau căii unei imagini
public class UpdateCommand implements Command {
    @Override
    public void execute(String[] args, ImageRepository repository) throws Exception {
        if (args.length < 4) { // Verificăm dacă avem numele vechi, nou și noua cale
            System.out.println("Usage: update <oldName> <newName> <newPath>");
            return;
        }
        String oldName = args[1];
        String newName = args[2];
        String newPath = args[3];
        repository.updateImage(oldName, newName, newPath); // Facem update în repo
    }
}