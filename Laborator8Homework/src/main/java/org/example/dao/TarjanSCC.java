package org.example.dao;

import org.example.dao.City;
import org.example.dao.SisterhoodDAO;

import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TarjanSCC {
    private final Map<Integer, List<Integer>> graph = new HashMap<>();
    private final List<List<Integer>> components = new ArrayList<>();
    private final int[] low;
    private final int[] disc;
    private final boolean[] visited;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private int time = 0;
    private final List<City> cities;

    public TarjanSCC(List<City> cities, Set<SisterhoodDAO.Pair> connections) {
        this.cities = cities;
        int maxId = cities.stream().mapToInt(City::getId).max().orElse(0) + 1;
        low = new int[maxId + 1];
        disc = new int[maxId + 1];
        visited = new boolean[maxId + 1];

        for (City city : cities) {
            graph.put(city.getId(), new ArrayList<>());
        }

        for (SisterhoodDAO.Pair pair : connections) {
            graph.get(pair.getCity1Id()).add(pair.getCity2Id());
            graph.get(pair.getCity2Id()).add(pair.getCity1Id());
        }
    }

    public List<List<Integer>> run() {
        for (int id : graph.keySet()) {
            if (disc[id] == 0) {
                dfs(id, -1);
            }
        }
        return components;
    }

    private void dfs(int u, int parent) {
        disc[u] = low[u] = ++time;
        stack.push(u);
        int children = 0;

        for (int v : graph.get(u)) {
            if (v == parent) continue;
            if (disc[v] == 0) {
                children++;
                dfs(v, u);
                low[u] = Math.min(low[u], low[v]);

                if (low[v] >= disc[u]) {
                    List<Integer> component = new ArrayList<>();
                    int w;
                    do {
                        w = stack.pop();
                        component.add(w);
                    } while (w != v);
                    component.add(u);
                    components.add(component);
                }
            } else {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    public void drawMap(Set<SisterhoodDAO.Pair> connections) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("City Sisterhood Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.add(new MapPanel(cities, connections, components));
            frame.setVisible(true);
        });
    }

    private static class MapPanel extends JPanel {
        private final List<City> cities;
        private final Set<SisterhoodDAO.Pair> connections;
        private final List<List<Integer>> components;
        private final Map<Integer, Point> cityPoints = new HashMap<>();

        public MapPanel(List<City> cities, Set<SisterhoodDAO.Pair> connections, List<List<Integer>> components) {
            this.cities = cities;
            this.connections = connections;
            this.components = components;

            for (City city : cities) {
                int x = (int) ((city.getLongitude() + 180) * 2.5);
                int y = (int) ((90 - city.getLatitude()) * 2.5);
                cityPoints.put(city.getId(), new Point(x, y));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1.5f));

            Color[] palette = { Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK, Color.YELLOW, Color.LIGHT_GRAY, Color.DARK_GRAY };

            Map<Integer, Color> cityColorMap = new HashMap<>();
            for (int i = 0; i < components.size(); i++) {
                Color color = palette[i % palette.length];
                for (int id : components.get(i)) {
                    cityColorMap.put(id, color);
                }
            }

            for (SisterhoodDAO.Pair pair : connections) {
                Point p1 = cityPoints.get(pair.getCity1Id());
                Point p2 = cityPoints.get(pair.getCity2Id());
                if (p1 != null && p2 != null) {
                    g.setColor(Color.GRAY);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            for (Map.Entry<Integer, Point> entry : cityPoints.entrySet()) {
                int id = entry.getKey();
                Point point = entry.getValue();
                g.setColor(cityColorMap.getOrDefault(id, Color.BLACK));
                g.fillOval(point.x - 3, point.y - 3, 6, 6);
            }
        }
    }
}
