import java.util.LinkedList;
import java.util.Scanner;

class Graph {
    private int vertices;
    private LinkedList<Integer> graph[];
    private int time;

    Graph(int vertices) {
        this.vertices = vertices;
        graph = new LinkedList[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new LinkedList<>();
        }

        time = 0;
    }

    void addEdge(int v, int w) {
        graph[v].add(w);
    }

    // Função para imprimir os sucessores de um vértice
    void printVertexSuccessors(int v) {
        System.out.print("Successors of vertex " + v + " -> ");
        for (int successor : graph[v]) {
            System.out.print(successor + ", ");
        }
        System.out.println();
    }

    // Função para imprimir os predecessores de um vértice
    void printVertexPredecessors(int v) {
        System.out.print("Predecessors of vertex " + v + " -> ");
        for (int i = 0; i < vertices; i++) {
            for (int j : graph[i]) {
                if (j == v) {
                    System.out.print(i + ", ");
                }
            }
        }
        System.out.println();
    }

    // Função para imprimir o grau de entrada (in-degree) de um vértice
    void printVertexInDegree(int v) {
        int count = 0;
        for (int i = 0; i < vertices; i++) {
            for (int j : graph[i]) {
                if (j == v) {
                    count++;
                }
            }
        }
        System.out.println("In-degree of vertex " + v + " = " + count);
    }

    // Função para imprimir o grau de saída (out-degree) de um vértice
    void printVertexOutDegree(int v) {
        int count = graph[v].size();
        System.out.println("Out-degree of vertex " + v + " = " + count);
    }

    // Função para carregar um grafo a partir de um arquivo
    public static Graph loadFile(String filename) {
        try (Scanner scanner = new Scanner(new java.io.File(filename))) {
            int vertices = scanner.nextInt();
            Graph graph = new Graph(vertices);

            while (scanner.hasNextInt()) {
                int u = scanner.nextInt();
                int w = scanner.nextInt();
                graph.addEdge(u, w);
            }

            return graph;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the file name: ");
        String filename = scanner.nextLine();

        Graph graph = loadFile(filename);
        if (graph == null) return;

        System.out.print("Enter the vertex: ");
        int vertex = scanner.nextInt();

        graph.printVertexSuccessors(vertex);
        graph.printVertexPredecessors(vertex);
        graph.printVertexInDegree(vertex);
        graph.printVertexOutDegree(vertex);
    }
}
