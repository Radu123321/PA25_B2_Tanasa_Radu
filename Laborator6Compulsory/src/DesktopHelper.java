// Clasă de utilitate pentru a deschide un fișier folosind aplicația implicită a sistemului
import java.awt.Desktop;
import java.io.File;

public class DesktopHelper {
    public static void openImage(String path) {
        try {
            Desktop.getDesktop().open(new File(path)); // Încearcă să deschidă fișierul
        } catch (Exception e) {
            System.out.println("Could not open file: " + e.getMessage());
        }
    }
}