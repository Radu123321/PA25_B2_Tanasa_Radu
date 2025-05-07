// Comandă pentru salvarea imaginilor într-un fișier JSON folosind Jackson
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class SaveCommand implements Command {
    @Override
    public void execute(String[] args, ImageRepository repository) throws Exception {
        if (args.length < 2) { // Verificăm dacă avem numele fișierului
            System.out.println("Usage: save <filename.json>");
            return;
        }
        String filename = args[1];
        ObjectMapper mapper = new ObjectMapper(); // Obiect pentru conversia în JSON
        mapper.writeValue(new File(filename), repository.getAllImages()); // Scrie lista în fișier
        System.out.println("Repository saved to " + filename);
    }
}