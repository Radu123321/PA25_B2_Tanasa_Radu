import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private JLabel scoreLabel1;
    private JLabel scoreLabel2;

    // ✅ Configurare AI per jucător
    private boolean player1IsAI = false;
    private boolean player2IsAI = true;
    private int player1Difficulty = 2;
    private int player2Difficulty = 2;

    public GameFrame() {
        setTitle("Dot Connect Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel sus
        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Număr de puncte:");
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        JButton startButton = new JButton("Start joc");

        // ✅ Selectori pentru jucători
        JComboBox<String> player1Type = new JComboBox<>(new String[]{"Human", "AI"});
        JComboBox<String> player2Type = new JComboBox<>(new String[]{"Human", "AI"});
        JComboBox<Integer> player1Level = new JComboBox<>(new Integer[]{0, 1, 2});
        JComboBox<Integer> player2Level = new JComboBox<>(new Integer[]{0, 1, 2});

        topPanel.add(label);
        topPanel.add(spinner);
        topPanel.add(startButton);
        topPanel.add(new JLabel("P1:"));
        topPanel.add(player1Type);
        topPanel.add(new JLabel("Dificultate:"));
        topPanel.add(player1Level);
        topPanel.add(new JLabel("P2:"));
        topPanel.add(player2Type);
        topPanel.add(new JLabel("Dificultate:"));
        topPanel.add(player2Level);

        scoreLabel1 = new JLabel("Player 1: 0");
        scoreLabel2 = new JLabel("Player 2: 0");
        topPanel.add(scoreLabel1);
        topPanel.add(scoreLabel2);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new GamePanel(0);
        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton loadButton = new JButton("Load");
        JButton saveButton = new JButton("Save");
        JButton resetButton = new JButton("Reset");
        JButton scoreButton = new JButton("Compare Score");
        JButton treeButton = new JButton("Vizualizează Arbore");
        JButton exportButton = new JButton("Export PNG");
        JButton exitButton = new JButton("Exit");

        bottomPanel.add(loadButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(scoreButton);
        bottomPanel.add(treeButton);
        bottomPanel.add(exportButton);
        bottomPanel.add(exitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 🎮 Start joc
        startButton.addActionListener(e -> {
            int numDots = (Integer) spinner.getValue();

            // 👉 citește configurările AI
            player1IsAI = player1Type.getSelectedItem().equals("AI");
            player2IsAI = player2Type.getSelectedItem().equals("AI");
            player1Difficulty = (Integer) player1Level.getSelectedItem();
            player2Difficulty = (Integer) player2Level.getSelectedItem();

            remove(gamePanel);
            gamePanel = new GamePanel(numDots);
            add(gamePanel, BorderLayout.CENTER);

            revalidate();
            repaint();
            updateScores();

            // dacă primul e AI, mută imediat
            if (player1IsAI) {
                SwingUtilities.invokeLater(this::aiTurnIfNeeded);
            }
        });

        resetButton.addActionListener(e -> {
            gamePanel.resetGame();
            updateScores();
        });

        scoreButton.addActionListener(e -> {
            double p1 = gamePanel.getState().getPlayerScore(true);
            double p2 = gamePanel.getState().getPlayerScore(false);
            double best = GameLogic.calculateMST(gamePanel.getState().dots);

            JOptionPane.showMessageDialog(this,
                    String.format("Player 1: %.2f\nPlayer 2: %.2f\nBest Possible (MST): %.2f", p1, p2, best),
                    "Scoruri", JOptionPane.INFORMATION_MESSAGE);
        });

        treeButton.addActionListener(e -> {
            List<List<Edge>> trees = GameLogic.generateSpanningTrees(gamePanel.getState().dots, 5);

            if (trees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nu s-au putut genera arbori.");
                return;
            }

            List<Edge> cheapest = trees.get(0);
            gamePanel.highlightTree(cheapest);

            double cost = cheapest.stream().mapToDouble(Edge::getLength).sum();
            JOptionPane.showMessageDialog(this,
                    String.format("Arbore evidențiat (cost minim): %.2f", cost));
        });

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvează tabla de joc ca PNG");

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String path = fileToSave.getAbsolutePath();

                if (!path.toLowerCase().endsWith(".png")) {
                    fileToSave = new File(path + ".png");
                }

                BufferedImage image = new BufferedImage(
                        gamePanel.getWidth(),
                        gamePanel.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D g2d = image.createGraphics();
                gamePanel.paint(g2d);
                g2d.dispose();

                try {
                    ImageIO.write(image, "png", fileToSave);
                    JOptionPane.showMessageDialog(this,
                            "Imagine salvată:\n" + fileToSave.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Eroare la salvare: " + ex.getMessage());
                }
            }
        });

        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvează jocul");
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                    out.writeObject(gamePanel.getState());
                    JOptionPane.showMessageDialog(this, "Joc salvat cu succes.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Eroare la salvare: " + ex.getMessage());
                }
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Încarcă jocul");
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    GameState loadedState = (GameState) in.readObject();
                    remove(gamePanel);
                    gamePanel = new GamePanel(loadedState);
                    add(gamePanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                    updateScores();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Eroare la încărcare: " + ex.getMessage());
                }
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        // 🧠 Timer pentru AI (executat după fiecare mutare)
        Timer timer = new Timer(300, e -> aiTurnIfNeeded());
        timer.setRepeats(false);
        gamePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                updateScores();
                timer.restart(); // așteaptă mutarea umană și apoi AI
            }
        });

        setVisible(true);
    }

    private void aiTurnIfNeeded() {
        boolean p1 = gamePanel.getState().playerOneTurn;

        if ((p1 && player1IsAI)) {
            gamePanel.aiMove(player1Difficulty);
            updateScores();
            SwingUtilities.invokeLater(this::aiTurnIfNeeded);
        } else if (!p1 && player2IsAI) {
            gamePanel.aiMove(player2Difficulty);
            updateScores();
            SwingUtilities.invokeLater(this::aiTurnIfNeeded);
        }
    }

    private void updateScores() {
        double p1 = gamePanel.getState().getPlayerScore(true);
        double p2 = gamePanel.getState().getPlayerScore(false);
        scoreLabel1.setText(String.format("Player 1: %.2f", p1));
        scoreLabel2.setText(String.format("Player 2: %.2f", p2));
    }
}
