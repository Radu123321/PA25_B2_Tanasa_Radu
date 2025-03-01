import java.util.Random;

public class Homeworkk {
    /*
      Am ales sa fac solutia asta, pentru ca e mai buna in practica, n <= 25, maybe 30, fata de backtraking chiar
      si optimizat, asemnatoare cu dinamica pe biti cand vrei sa gasesti un ciclu hamiltonian de cost minim(aceeasi
      abordare)
      Desi cred ca exista un algoritm mult mai optim, dar se bazeaza pe unele proprietati a unor grafuri "speciale", in care
      solutia poate ajunge la timp Polinomial
     */
    private int[] adjacencyMatrix;
    private int n;
    private Random rand = new Random();

    public Homeworkk(int n, int k) {
        this.n = n;
        adjacencyMatrix = new int[n];
        generateRandomGraph();
        ensureClique(k);
        ensureStableSet(k);
    }

    private void generateRandomGraph() {
        for (int i = 0; i < n; i++) {
            adjacencyMatrix[i] = 0;
            for (int j = 0; j < n; j++) {
                /// bitul din masca de biti ai lui i este 1 inseamna ca exista nod i->j
                if (i != j && rand.nextBoolean()) {
                    adjacencyMatrix[i] |= (1 << j);
                }
            }
        }
    }

    private void ensureClique(int k) {
        int subset = 0;
        /*
         subset|=(1 << i), pune 1 la bitul i
         practic subsetul va fi = 2^k - 1
         */
        for (int i = 0; i < k; i++) {
            subset |= (1 << i);
        }
        /*
        face drumur de la i la toate celelalte noduri
        problema ca face si de la el insusi, deci il negam
         */
        for (int i = 0; i < k; i++) {
            adjacencyMatrix[i] |= subset;
            adjacencyMatrix[i] &= ~(1 << i);
        }
    }

    private void ensureStableSet(int k) {
        int subset = 0;
        for (int i = 0; i < k; i++) {
            subset |= (1 << i);
        }
        /*
        Sterge toate conexiunele dintre nodurile selectate.
        Dar si elimina orice conexiune pe care aceste noduri ar putea sa o aiba cu alte noduri
        din afara subsetului
         */
        for (int i = 0; i < k; i++) {
            adjacencyMatrix[i] &= ~subset;
        }
    }

    public boolean hasClique(int k)
    {
        /*
        Complexitate O(2 ^ n * n^2)
         */
        int maxMask = (1 << n);
        for (int subset = 0; subset < maxMask; subset++) {
            if (Integer.bitCount(subset) == k && isClique(subset)) {
                return true;
            }
        }
        return false;
    }

    private boolean isClique(int subset)
    {
        /*
           verifica daca bitul i din subset este 1
           apoi verifica daca bitul j face parte din subset
           dar daca nu exista muchie de la i la j adjacencyMatrix[i] & (1 << j)), nu este Clique
         */
        for (int i = 0; i < n; i++) {
            if ((subset & (1 << i)) != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((subset & (1 << j)) != 0 && (adjacencyMatrix[i] & (1 << j)) == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean hasStableSet(int k)
    {
        int maxMask = (1 << n);
        for (int subset = 0; subset < maxMask; subset++) {
            if (Integer.bitCount(subset) == k && isStableSet(subset)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStableSet(int subset)
    {
         /*
           verifica daca bitul i din subset este 1
           apoi verifica daca bitul j face parte din subset
           dar daca exista muchie de la i la j adjacencyMatrix[i] & (1 << j)), nu este StableSet
         */
        for (int i = 0; i < n; i++) {
            if ((subset & (1 << i)) != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((subset & (1 << j)) != 0 && (adjacencyMatrix[i] & (1 << j)) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int NumberEdges()
    {
        /*
         nr de biti de 1 din masca i reprezinta chiar numarul de muchii de la i
         */
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += Integer.bitCount(adjacencyMatrix[i]);
        }
        return count / 2;
    }

    public int MaximumDegree() {
        /*
          nr de biti de 1 din masca i , reprezinta gradul nodurilor
         */
        int maxDegree = 0;
        for (int i = 0; i < n; i++) {
            maxDegree = Math.max(maxDegree, Integer.bitCount(adjacencyMatrix[i]));
        }
        return maxDegree;
    }

    public int MinimumDegree()
    {
        int minDegree = n;
        for (int i = 0; i < n; i++) {
            minDegree = Math.min(minDegree, Integer.bitCount(adjacencyMatrix[i]));
        }
        return minDegree;
    }

    public String buildMatrixString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix Representation:\n");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((adjacencyMatrix[i] & (1 << j)) != 0) {
                    sb.append("■ ");
                } else {
                    sb.append("□ ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Homeworkk <n> <k>");
            return;
        }
        try {
            int n = Integer.parseInt(args[0]);
            int k = Integer.parseInt(args[1]);
            if (n < k) {
                throw new IllegalArgumentException("Error: n must be greater than or equal to k.");
            }
            Homeworkk graph = new Homeworkk(n, k);
            System.out.println(graph.buildMatrixString());
            System.out.println("Has clique of size " + k + ": " + graph.hasClique(k));
            System.out.println("Has stable set of size " + k + ": " + graph.hasStableSet(k));
            System.out.println("Number of edges: " + graph.NumberEdges());
            System.out.println("Maximum degree: " + graph.MaximumDegree());
            System.out.println("Minimum degree: " + graph.MinimumDegree());
        } catch (NumberFormatException e) {
            System.out.println("Both n and k must be integers.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
