
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lansăm interfața grafică pe firul special al Swing-ului
        SwingUtilities.invokeLater(() -> {
            new GameFrame(); // Creează și afișează fereastra principală
        });
    }
}

