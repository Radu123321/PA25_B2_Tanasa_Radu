// Comandă pentru încărcarea imaginilor dintr-un fișier JSON folosind Jackson
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

public class LoadCommand implements Command {
    @Override
    public void execute(String[] args, ImageRepository repository) throws Exception {
        if (args.length < 2) { // Verificăm dacă avem numele fișierului
            System.out.println("Usage: load <filename.json>");
            return;
        }
        String filename = args[1];
        ObjectMapper mapper = new ObjectMapper(); // Obiect pentru citire JSON
        List<ImageItem> loaded = mapper.readValue(new File(filename), new TypeReference<>() {}); // Citim lista
        repository.clear(); // Golim repo-ul existent
        loaded.forEach(repository::addImage); // Adăugăm imaginile încărcate
        System.out.println("Repository loaded from " + filename);
    }
}
