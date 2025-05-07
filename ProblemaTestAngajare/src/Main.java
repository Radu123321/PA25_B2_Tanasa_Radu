import java.util.*;

public class Main
{
    /*
      Problema se poate rezolva cu un graf.
      Mentionez ca nu stiu dimensiunea exacta a cate noduri sunt(clase) si am pus un maxim de 100
      Pentru prima cerinta exista mostenire circulara daca exista nod de la A->B si nod de la B->A
      Pentru a doua cerinta exista mostenire diamand daca exista un ciclu de lungime 3 in graful ordonat
      Creez nod de la A -> B daca A extends B
      Prima cerinta se poate rezolva in complexitate de O(nr de noduri ^ 2) verificand fiecare nod i -> j
      daca exista muchie de la i la j si de la j la i
      A 2 a cerinta se rezolva facand un DFS  si tinand mereu "tatii" nodurilor.Daca ajung intr un nod vizitat
      si tatal nodului curent este diferit de tatal nodului vizitat, atunci avem un ciclu de lungime 3
      Complexitate O(nr de noduri ^ 2)
      Memorie:O(nr noduri ^ 2)
      Cred ca exista solutii mai optime din punct de vedere a memoriei, da cum m am gandit ca numarul de clase e mic
      putem sa folosim o matrice adiacenta.


      ..................................................................................


      Acuma mi am dat seama ca  exista inheritance diamond daca exista 2 drumuri diferite de la nodul x -> la nodul y
      Si circular inheritance daca exista un ciclu nu neaparat de lungime 3
     */
    private static final int Max = 100;
    private static String line;
    private static boolean[][] adj = new boolean[Max + 5][Max + 5];
    private static boolean[] viz = new boolean[105];
    private static int[] father = new int[105];
    private static boolean diamondInheritance = false;

    private static boolean CircularInheritance() {
        for (int i = 1; i <= Max; i++) {
            for (int j = 1; j <= Max; j++) {
                if (i == j) continue;
                if (adj[i][j] && adj[j][i]) return true;
            }
        }
        return false;
    }

    private static void Dfs(int node, int parent) {
        viz[node] = true;
        father[node] = parent;
        for (int i = 1; i <= Max && !diamondInheritance; i++) {
            if (i == node) continue;
            if (adj[node][i] && !viz[i]) {
                Dfs(i, node);
            } else if (viz[i] && father[node] != i) {
                diamondInheritance = true;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            line = scanner.nextLine();
            if (line.isEmpty()) break;
            int node = -1;
            int node1 = -1;
            for (int i = 0; i < line.length(); ) {
                if (line.startsWith("class", i)) {
                    i += 6;
                    node = line.charAt(i) - 'A' + 1;
                } else if (line.startsWith("extends", i)) {
                    i += 8;
                    node1 = line.charAt(i) - 'A' + 1;
                    adj[node][node1] = true;
                    i++;
                    while (line.charAt(i) != ';') {
                        if (line.charAt(i - 1) == ' ') {
                            node1 = line.charAt(i) - 'A' + 1;
                            adj[node][node1] = true;
                        }
                        i++;
                    }
                } else {
                    i++;
                }
            }
        }
        if (!CircularInheritance()) {
            System.out.println("There are no circular inheritance.");
        } else {
            System.out.println("There is at least one circular inheritance");
        }
        for (int i = 1; i <= Max; i++) {
            Dfs(i, i);
        }
        if (diamondInheritance) {
            System.out.println("There is at least one diamond inheritance");
        } else {
            System.out.println("There are no diamond inheritance");
        }
    }
}

