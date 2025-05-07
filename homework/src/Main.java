import java.util.*;

class GraphGenerator {
    private static int[][] adjacencyMatrix;
    private static int n, k;
    private static Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        k = scanner.nextInt();
        long startTime = System.currentTimeMillis();

        generateGraph();

        if (n <= 30000) {
            System.out.println(matrixToString());
        }

        int edges = countEdges();
        int maxDegree = getMaxDegree();
        int minDegree = getMinDegree();

        System.out.println("Number of edges (m): " + edges);
        System.out.println("Maximum degree (Δ(G)): " + maxDegree);
        System.out.println("Minimum degree (δ(G)): " + minDegree);
        System.out.println("Sum of degrees: " + sumDegrees());
        System.out.println("2 * m: " + (2 * edges));

        if (sumDegrees() == 2 * edges) {
            System.out.println("The sum of degrees equals 2 * m, as expected.");
        } else {
            System.out.println("Error: Sum of degrees does not match 2 * m.");
        }

        if (hasClique(k)) {
            System.out.println("The graph contains a clique of size at least " + k);
        } else {
            System.out.println("No clique of size " + k + " found.");
        }

        if (hasStableSet(k)) {
            System.out.println("The graph contains a stable set of size at least " + k);
        } else {
            System.out.println("No stable set of size " + k + " found.");
        }

        long endTime = System.currentTimeMillis();
        if (n > 30000) {
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        }
    }

    private static void generateGraph() {
        adjacencyMatrix = new int[n][n];

        // Ensure a clique of size k
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < k; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[j][i] = 1;
            }
        }

        List<Integer> stableSetNodes = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            stableSetNodes.add(k + i);
        }

        for (int i = 0; i < stableSetNodes.size(); i++) {
            for (int j = i + 1; j < stableSetNodes.size(); j++) {
                adjacencyMatrix[stableSetNodes.get(i)][stableSetNodes.get(j)] = 0;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjacencyMatrix[i][j] == 0 && random.nextBoolean()) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[j][i] = 1;
                }
            }
        }
    }

    private static String matrixToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(adjacencyMatrix[i][j] == 1 ? "■ " : "□ ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static int countEdges() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                count += adjacencyMatrix[i][j];
            }
        }
        return count;
    }

    private static int getMaxDegree() {
        int maxDegree = 0;
        for (int i = 0; i < n; i++) {
            int degree = Arrays.stream(adjacencyMatrix[i]).sum();
            maxDegree = Math.max(maxDegree, degree);
        }
        return maxDegree;
    }

    private static int getMinDegree() {
        int minDegree = n;
        for (int i = 0; i < n; i++) {
            int degree = Arrays.stream(adjacencyMatrix[i]).sum();
            minDegree = Math.min(minDegree, degree);
        }
        return minDegree;
    }

    private static int sumDegrees() {
        return Arrays.stream(adjacencyMatrix).mapToInt(row -> Arrays.stream(row).sum()).sum();
    }

    private static boolean hasClique(int size) {
        return hasCliqueHelper(new HashSet<>(), 0, size);
    }

    private static boolean hasCliqueHelper(Set<Integer> clique, int start, int size) {
        if (clique.size() == size) return true;
        for (int i = start; i < n; i++) {
            final int node = i;
            if (clique.stream().allMatch(v -> adjacencyMatrix[v][node] == 1)) {
                clique.add(i);
                if (hasCliqueHelper(clique, i + 1, size)) return true;
                clique.remove(i);
            }
        }
        return false;
    }

    private static boolean hasStableSet(int size) {
        return hasStableSetHelper(new HashSet<>(), 0, size);
    }

    private static boolean hasStableSetHelper(Set<Integer> stableSet, int start, int size) {
        if (stableSet.size() == size) return true;
        for (int i = start; i < n; i++) {
            final int node = i;
            if (stableSet.stream().noneMatch(v -> adjacencyMatrix[v][node] == 1)) {
                stableSet.add(i);
                if (hasStableSetHelper(stableSet, i + 1, size)) return true;
                stableSet.remove(i);
            }
        }
        return false;
    }
}