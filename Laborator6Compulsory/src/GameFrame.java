import javax.swing.*;
public class GameFrame extends JFrame { // Clasa ferestrei principale extinde JFrame
    public GameFrame() {
        setTitle("Dot Connect Game"); // Titlul ferestrei
        setSize(800, 600); // Dimensiunea ferestrei (lățime, înălțime)
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Când închizi fereastra, aplicația se oprește
        add(new GamePanel()); // Adaugă panoul de joc în interiorul ferestrei
        setVisible(true); // Fă fereastra vizibilă pe ecran
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
        // Creează și pornește fereastra pe firul grafic (safe)
    }
}