// === ImageRepository.java ===
// Clasa responsabilă cu gestionarea tuturor imaginilor
import java.util.*;

public class ImageRepository {
    private final List<ImageItem> images = new ArrayList<>(); // Lista internă de imagini

    public void addImage(ImageItem image) {
        images.add(image); // Adaugă imaginea în listă
        System.out.println("Image added: " + image.name());
    }

    public void removeImage(String name) throws ImageNotFoundException {
        boolean removed = images.removeIf(image -> image.name().equals(name)); // Șterge imaginea după nume
        if (!removed) throw new ImageNotFoundException("Image not found: " + name);
        System.out.println("Image removed: " + name);
    }

    public void updateImage(String oldName, String newName, String newPath) throws ImageNotFoundException {
        for (int i = 0; i < images.size(); i++) {
            ImageItem img = images.get(i);
            if (img.name().equals(oldName)) {
                images.set(i, new ImageItem(newName, newPath)); // Înlocuiește cu o imagine nouă
                System.out.println("Image updated: " + oldName);
                return;
            }
        }
        throw new ImageNotFoundException("Image not found: " + oldName);
    }

    public List<ImageItem> getAllImages() {
        return images; // Returnează toate imaginile
    }

    public void clear() {
        images.clear(); // Șterge toate imaginile
    }
}