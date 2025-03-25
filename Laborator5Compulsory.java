import java.io.*;
import java.time.LocalDate;
import java.util.*;

// Record pentru imagine
//forma speciala de clasa imutabila
record ImageItem(String name, LocalDate date, List<String> tags, String filePath) implements Serializable {}

// Clasă pentru gestionarea imaginilor
class ImageRepository implements Serializable {
    private List<ImageItem> images;

    public ImageRepository() {
        this.images = new ArrayList<>();
    }

    public void addImage(ImageItem image) {
        images.add(image);
    }

    public List<ImageItem> getImages() {
        return new ArrayList<>(images);
    }

    public void displayImages() {
        if (images.isEmpty()) {
            System.out.println("Nu există imagini în repository.");
        }
        for (ImageItem image : images) {
            System.out.println("Nume: " + image.name());
            System.out.println("Data: " + image.date());
            System.out.println("Taguri: " + image.tags());
            System.out.println("Locație: " + image.filePath());
            System.out.println("-----------");
        }
    }

    // Salvare în fișier
    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
            System.out.println("Repository salvat cu succes în " + filename);
        } catch (IOException e) {
            System.err.println("Eroare la salvare: " + e.getMessage());
        }
    }

    // Încărcare din fișier
    public static ImageRepository loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (ImageRepository) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Eroare la încărcare: " + e.getMessage());
            return new ImageRepository(); // fallback
        }
    }
}

// Clasa principală
public class Laborator5Compulsory {
    public static void main(String[] args) {
        String fileName = "imagini.ser";

        // 1. Creează un repository și adaugă imagini
        ImageRepository repository = new ImageRepository();

        repository.addImage(new ImageItem(
                "poza1.jpg",
                LocalDate.of(2023, 12, 25),
                Arrays.asList("Craciun", "familie"),
                "/home/user/poze/poza1.jpg"
        ));

        repository.addImage(new ImageItem(
                "vacanta2024.png",
                LocalDate.of(2024, 7, 10),
                Arrays.asList("vacanta", "mare", "relaxare"),
                "/home/user/poze/vacanta2024.png"
        ));

        // 2. Afișează imagini
        System.out.println("== Imagini în memorie ==");
        repository.displayImages();

        // 3. Salvează în fișier
        repository.saveToFile(fileName);

        // 4. Încarcă din fișier
        System.out.println("\n== Încărcare repository din fișier ==");
        ImageRepository loadedRepo = ImageRepository.loadFromFile(fileName);
        loadedRepo.displayImages();
    }
}
