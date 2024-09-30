import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Grafo {
    private Grafo proximo;
    private int vertice;

    public Grafo(int vertice) {
        this.proximo = null;
        this.vertice = vertice;
    }

    public int getVertice() {
        return this.vertice;
    }

    public void setVertice(int vertice) {
        this.vertice = vertice;
    }

    public Grafo getProximo() {
        return this.proximo;
    }

    public void setProximo(Grafo proximo) {
        this.proximo = proximo;
    }

    public static Grafo ordenarGrafo(Grafo cabeca) {
        if (cabeca == null || cabeca.proximo == null) {
            return cabeca;
        }

        //Selection Sort
        Grafo atual = cabeca;
        while (atual != null) {
            Grafo menor = atual;
            Grafo proximo = atual.proximo;

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
    private List<Aresta> avanco;
    private List<Aresta> cruzamento;

    public Busca(Grafo[] grafo, int vertice) {
        t = 0;
        td = new int[grafo.length];
        tt = new int[grafo.length];
        pai = new int[grafo.length];
        for (int i = 0; i < grafo.length; i++) {
            td[i] = 0;
            tt[i] = 0;
            pai[i] = -1;
        }
        arvore = new ArrayList<Aresta>();
        retorno = new ArrayList<Aresta>();
        avanco = new ArrayList<Aresta>();
        cruzamento = new ArrayList<Aresta>();

        buscaEmProfundidadeIterativa(grafo);
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

    public List<Aresta> getAvanco(){
        return avanco;
    }

    public List<Aresta> getCruzamento(){
        return cruzamento;
    }

    public void buscaEmProfundidadeIterativa(Grafo[] grafo) {
        for (int vInicial = 1; vInicial <= grafo.length; vInicial++) {
            if (!visto(vInicial - 1)) {
                visitaIterativa(grafo, vInicial);  // Chama a função de busca para cada nova raiz não visitada
            }
        }
    }

    public void visitaIterativa(Grafo[] grafo, int vInicial) {
        Stack<Integer> stack = new Stack<>();
        stack.push(vInicial);
        
        td[vInicial - 1] = ++t;
        
        while (!stack.isEmpty()) {
            int v = stack.peek();
            boolean allVisited = true;

            for (Grafo g = grafo[v - 1]; g != null; g = g.getProximo()) {
                int verticeAdj = g.getVertice();
                if (!visto(verticeAdj - 1)) {
                    arvore.add(new Aresta(v, verticeAdj));
                    pai[verticeAdj - 1] = v;
                    
                    td[verticeAdj - 1] = ++t;
                    stack.push(verticeAdj);
                    allVisited = false;
                    break;
                } else if (tt[verticeAdj - 1] == 0) {
                    retorno.add(new Aresta(v, verticeAdj));
                } else if (td[v - 1] < td[verticeAdj - 1]) {
                    avanco.add(new Aresta(v, verticeAdj));
                } else {
                    cruzamento.add(new Aresta(v, verticeAdj));
                }
            }
            
            if (allVisited) {
                tt[v - 1] = ++t;
                stack.pop();
            }
        }
    }

    // public void visitaRecursiva(Grafo[] grafo, int v) {
    //     td[v-1] = ++t;
    //     for (Grafo g = grafo[v-1]; g != null; g = g.getProximo()) {
    //         if (!visto(g.getVertice()-1)) {
    //             arvore.add(new Aresta(v, g.getVertice()));
    //             pai[g.getVertice()-1] = v;
    //             visita(grafo, g.getVertice());
    //         } else if (td[g.getVertice()-1] > td[v-1]) {
    //             avanco.add(new Aresta(v, g.getVertice()));
    //         } else if (tt[g.getVertice()-1] == 0) {
    //             cruzamento.add(new Aresta(v, g.getVertice()));
    //         } else {
    //             retorno.add(new Aresta(v, g.getVertice()));
    //         }
    //     }
    //     tt[v-1] = ++t;
    // }

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
        System.out.print("Digite o número do vertice: ");
        int vertice = Integer.parseInt(System.console().readLine());
        Grafo[] grafo = null;
        grafo = constroiGrafo(grafo, nomeArq);
        Busca busca = new Busca(grafo, vertice);
        // System.out.println("Tempo de descoberta: " + busca.getTdAt(vertice-1));
        // System.out.println("Tempo de término: " + busca.getTtAt(vertice-1));
        // System.out.println("Tempo total: " + busca.getT());
        System.out.println("Arestas de árvore: " + busca.getArvore());
        // System.out.println("Arestas de retorno: " + busca.getRetorno());
        // System.out.println("Arestas de avanço: " + busca.getAvanco());
        // System.out.println("Arestas de cruzamento: " + busca.getCruzamento());
        System.out.println("");
        System.out.println("Arestas de árvore do vértice " + vertice + ": " + busca.getArestasDeArvoreDoVertice(vertice));

    }

    public static Grafo[] constroiGrafo(Grafo[] grafo, String nomeArq) {
        // Leitura do arquivo
        try {
            RandomAccessFile rf = new RandomAccessFile(nomeArq, "r");
            int[] primeiraLinha = processLine(rf);

            grafo = new Grafo[primeiraLinha[0]];
            for (int i = 0; i < primeiraLinha[0]; i++) {
                grafo[i] = new Grafo(i+1);
            }

            for (int i = 0; i < primeiraLinha[1]; i++) {
                int[] valores = processLine(rf);

                Grafo aux = grafo[valores[0]-1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Grafo(valores[1]));
            }

            for (int i = 0; i < primeiraLinha[0]; i++) {
                grafo[i] = Grafo.ordenarGrafo(grafo[i]);
            }            

            rf.close();

            return grafo;
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo");
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
            System.out.println("Erro ao ler a linha: " + e);
        }
        return values;
    }
}

