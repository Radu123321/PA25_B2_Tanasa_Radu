// Comandă pentru adăugarea unei imagini în repository
public class AddCommand implements Command {
    @Override
    public void execute(String[] args, ImageRepository repository) {
        if (args.length < 3) { // Verificăm dacă avem numele și calea imaginii
            System.out.println("Usage: add <name> <path>");
            return;
        }
        String name = args[1];
        String path = args[2];
        ImageItem image = new ImageItem(name, path); // Creăm obiectul imagine
        repository.addImage(image); // Adăugăm în repository
    }
}