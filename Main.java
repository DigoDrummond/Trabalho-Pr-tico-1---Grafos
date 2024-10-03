import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

class Adjacentes {
    private Adjacentes proximo;
    private int vertice;

    public Adjacentes(int vertice) {
        this.proximo = null;
        this.vertice = vertice;
    }

    public int getVertice() {
        return this.vertice;
    }

    public void setVertice(int vertice) {
        this.vertice = vertice;
    }

    public Adjacentes getProximo() {
        return this.proximo;
    }

    public void setProximo(Adjacentes proximo) {
        this.proximo = proximo;
    }

    public static Adjacentes ordenarAdjacentes(Adjacentes cabeca) {
        if (cabeca == null || cabeca.proximo == null) {
            return cabeca;
        }

        // Selection Sort
        Adjacentes atual = cabeca;
        while (atual != null) {
            Adjacentes menor = atual;
            Adjacentes proximo = atual.proximo;

            while (proximo != null) {
                if (proximo.vertice < menor.vertice) {
                    menor = proximo;
                }
                proximo = proximo.proximo;
            }

            int temp = atual.vertice;
            atual.vertice = menor.vertice;
            menor.vertice = temp;

            atual = atual.proximo;
        }

        return cabeca;
    }
}

class Aresta {
    private int origem;
    private int destino;
    public Aresta(int a, int b) {
        this.origem = a;
        this.destino = b;
    }
    
    public int getOrigem() {
        return origem;
    }

    public int getDestino() {
        return destino;
    }

    @Override
    public String toString() {
        return "[" + origem + ", " + destino + "]";
    }
}

class Busca {
    private int t;
    private int td[];
    private int tt[];
    private int pai[];
    
    private List<Aresta> arvore;
    private List<Aresta> retorno;
    private List<List<Integer>> ciclos;

    private Set<String> ciclosSet; // Usado para evitar duplicatas

    private boolean hasCycle = false;

    private Adjacentes[] adjacentes;

    public Busca(Adjacentes[] adjacentes) {
        this.adjacentes = adjacentes;
        t = 0;
        td = new int[adjacentes.length];
        tt = new int[adjacentes.length];
        pai = new int[adjacentes.length];
        for (int i = 0; i < adjacentes.length; i++) {
            td[i] = 0;
            tt[i] = 0;
            pai[i] = -1;
        }
        arvore = new ArrayList<Aresta>();
        retorno = new ArrayList<Aresta>();
        ciclos = new ArrayList<>();
        ciclosSet = new HashSet<>();

        buscaEmProfundidadeIterativa();
    }

    public int getT(){
        return t;
    }

    public int getTdAt(int v){
        return td[v];
    } 

    public int getTtAt(int v){
        return tt[v];
    } 
    public boolean visto(int v){
        return td[v] != 0;
    }

    public int getPai(int v){
        return pai[v];
    }

    public List<Aresta> getArvore(){
        return arvore;
    }

    public List<Aresta> getRetorno(){
        return retorno;
    }

    public List<List<Integer>> getCiclos() {
        return ciclos;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public void buscaEmProfundidadeIterativa() {
        for (int vInicial = 1; vInicial <= adjacentes.length; vInicial++) {
            if (!visto(vInicial - 1)) {
                visitaIterativa(vInicial);  // Chama a função de busca para cada nova raiz não visitada
            }
        }
    }

    public void visitaIterativa(int vInicial) {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> caminhoAtual = new Stack<>(); // Pilha para o caminho atual
        stack.push(vInicial);
        caminhoAtual.push(vInicial);

        td[vInicial - 1] = ++t;

        while (!stack.isEmpty()) {
            int v = stack.peek();
            boolean allVisited = true;

            for (Adjacentes g = adjacentes[v - 1]; g != null; g = g.getProximo()) {
                int verticeAdj = g.getVertice();
                if (!visto(verticeAdj - 1)) {
                    arvore.add(new Aresta(v, verticeAdj));
                    pai[verticeAdj - 1] = v;

                    td[verticeAdj - 1] = ++t;
                    stack.push(verticeAdj);
                    caminhoAtual.push(verticeAdj);
                    allVisited = false;
                    break;
                } else if (verticeAdj != pai[v - 1]) {
                    // Encontrou um ciclo
                    hasCycle = true;

                    // Reconstrói o ciclo
                    List<Integer> ciclo = new ArrayList<>();

                    // Start from v, go back through caminhoAtual until we reach verticeAdj
                    for (int i = caminhoAtual.size() - 1; i >= 0; i--) {
                        int vertexInPath = caminhoAtual.get(i);
                        ciclo.add(vertexInPath);
                        if (vertexInPath == verticeAdj) {
                            break;
                        }
                    }

                    // Normaliza o ciclo
                    // Reverte para que o ciclo comece do verticeAdj
                    Collections.reverse(ciclo);

                    // Rotaciona o ciclo para começar com o menor vértice
                    int minVertex = Collections.min(ciclo);
                    int minIndex = ciclo.indexOf(minVertex);

                    List<Integer> normalizedCiclo = new ArrayList<>();
                    for (int i = 0; i < ciclo.size(); i++) {
                        normalizedCiclo.add(ciclo.get((minIndex + i) % ciclo.size()));
                    }

                    // Também rotaciona o ciclo invertido
                    List<Integer> reversedCiclo = new ArrayList<>(ciclo);
                    Collections.reverse(reversedCiclo);
                    int minIndexReversed = reversedCiclo.indexOf(minVertex);

                    List<Integer> normalizedReversedCiclo = new ArrayList<>();
                    for (int i = 0; i < reversedCiclo.size(); i++) {
                        normalizedReversedCiclo.add(reversedCiclo.get((minIndexReversed + i) % reversedCiclo.size()));
                    }

                    // Seleciona a versão lexicograficamente menor
                    List<Integer> finalCiclo = (normalizedCiclo.toString().compareTo(normalizedReversedCiclo.toString()) < 0)
                        ? normalizedCiclo : normalizedReversedCiclo;

                    // Adiciona o ciclo se ainda não foi registrado
                    String cicloStr = finalCiclo.toString();

                    if (!ciclosSet.contains(cicloStr)) {
                        ciclosSet.add(cicloStr);
                        ciclos.add(finalCiclo);
                    }
                }
            }

            if (allVisited) {
                tt[v - 1] = ++t;
                stack.pop();
                caminhoAtual.pop();
            }
        }
    }

    public List<Aresta> getArestasDeArvoreDoVertice(int vertice) {
        List<Aresta> arestasDeArvore = new ArrayList<>();
        for (Aresta aresta : arvore) {
            if (aresta.getOrigem() == vertice) {
                arestasDeArvore.add(aresta);
            }
        }
    
        return arestasDeArvore;
    }
}

public class Main {
    public static void main(String[] args) {
        String nomeArq = null;
        System.out.print("\nDigite o nome do arquivo com sua extensão (.txt): ");
        nomeArq = System.console().readLine();
        Adjacentes[] adjacentes = null;
        adjacentes = constroiAdjacentes(adjacentes, nomeArq);
        Busca busca = new Busca(adjacentes);

        if (busca.hasCycle()) {
            System.out.println("O grafo contém ciclos.");
            List<List<Integer>> ciclos = busca.getCiclos();
            System.out.println("Ciclos encontrados:");
            for (List<Integer> ciclo : ciclos) {
                System.out.println(ciclo);
            }
        } else {
            System.out.println("O grafo não contém ciclos.");
        }

    }

    public static Adjacentes[] constroiAdjacentes(Adjacentes[] adjacentes, String nomeArq) {
        // Leitura do arquivo
        try {
            RandomAccessFile rf = new RandomAccessFile(nomeArq, "r");
            int nVertices = Integer.parseInt(rf.readLine());

            adjacentes = new Adjacentes[nVertices];
            for (int i = 0; i < nVertices; i++) {
                adjacentes[i] = null;
            }

            while(rf.getFilePointer() < rf.length()) {
                int[] valores = processLine(rf);

                int u = valores[0];
                int v = valores[1];

                // Adiciona v à lista de adjacência de u
                if (adjacentes[u - 1] == null) {
                    adjacentes[u - 1] = new Adjacentes(v);
                } else {
                    Adjacentes aux = adjacentes[u - 1];
                    while (aux.getProximo() != null) {
                        aux = aux.getProximo();
                    }
                    aux.setProximo(new Adjacentes(v));
                }

                // Adiciona u à lista de adjacência de v (grafo não direcionado)
                if (adjacentes[v - 1] == null) {
                    adjacentes[v - 1] = new Adjacentes(u);
                } else {
                    Adjacentes aux = adjacentes[v - 1];
                    while (aux.getProximo() != null) {
                        aux = aux.getProximo();
                    }
                    aux.setProximo(new Adjacentes(u));
                }
            }

            // Ordena as listas de adjacência
            for (int i = 0; i < nVertices; i++) {
                adjacentes[i] = Adjacentes.ordenarAdjacentes(adjacentes[i]);
            }            

            rf.close();

            return adjacentes;
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return null;
        }
    }

    public static int[] processLine(RandomAccessFile rf) {
        int[] values = new int[2];
        try {
            String linha = rf.readLine();
            linha = linha.trim();
            String numeros[] = linha.split("\\s+");
            values[0] = Integer.parseInt(numeros[0]);
            values[1] = Integer.parseInt(numeros[1]);
        } catch (Exception e) {
            System.out.println("Erro ao ler a linha: " + e.getMessage());
        }
        return values;
    }
}