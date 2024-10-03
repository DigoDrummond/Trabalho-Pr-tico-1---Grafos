import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
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

        //Selection Sort
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

// class Busca {
//     private int t;
//     private int td[];
//     private int tt[];
//     private int pai[];

//     private List<Aresta> arvore;
//     private List<Aresta> retorno;

//     private boolean hasCycle = false;

//     public Busca(Adjacentes[] adjacentes) {
//         t = 0;
//         td = new int[adjacentes.length];
//         tt = new int[adjacentes.length];
//         pai = new int[adjacentes.length];
//         for (int i = 0; i < adjacentes.length; i++) {
//             td[i] = 0;
//             tt[i] = 0;
//             pai[i] = -1;
//         }
//         arvore = new ArrayList<Aresta>();
//         retorno = new ArrayList<Aresta>();

//         buscaEmProfundidadeIterativa(adjacentes);
//     }

//     public int getT(){
//         return t;
//     }

//     public int getTdAt(int v){
//         return td[v];
//     } 

//     public int getTtAt(int v){
//         return tt[v];
//     } 
//     public boolean visto(int v){
//         return td[v] != 0;
//     }

//     public int getPai(int v){
//         return pai[v];
//     }

//     public List<Aresta> getArvore(){
//         return arvore;
//     }

//     public List<Aresta> getRetorno(){
//         return retorno;
//     }

//     public boolean hasCycle() {
//         return hasCycle;
//     }

//     public void buscaEmProfundidadeIterativa(Adjacentes[] adjacentes) {
//         for (int vInicial = 1; vInicial <= adjacentes.length; vInicial++) {
//             if (!visto(vInicial - 1)) {
//                 visitaIterativa(adjacentes, vInicial);  // Chama a função de busca para cada nova raiz não visitada
//             }
//         }
//     }

//     public void visitaIterativa(Adjacentes[] adjacentes, int vInicial) {
//         Stack<Integer> stack = new Stack<>();
//         stack.push(vInicial);
        
//         td[vInicial - 1] = ++t;
        
//         while (!stack.isEmpty()) {
//             int v = stack.peek();
//             boolean allVisited = true;

//             for (Adjacentes g = adjacentes[v - 1].getProximo(); g != null; g = g.getProximo()) {
//                 int verticeAdj = g.getVertice();
//                 if (!visto(verticeAdj - 1)) {
//                     arvore.add(new Aresta(v, verticeAdj));
//                     pai[verticeAdj - 1] = v;
                    
//                     td[verticeAdj - 1] = ++t;
//                     stack.push(verticeAdj);
//                     allVisited = false;
//                     break;
//                 } else if (tt[verticeAdj - 1] == 0 && pai[v - 1] != verticeAdj) {
//                     retorno.add(new Aresta(v, verticeAdj));
//                     hasCycle = true;
//                 }
//             }
            
//             if (allVisited) {
//                 tt[v - 1] = ++t;
//                 stack.pop();
//             }
//         }
//     }
// }

class CycleFinder {
    private Adjacentes[] adjacentes;
    private Set<String> ciclosEncontrados;

    public CycleFinder(Adjacentes[] adjacentes) {
        this.adjacentes = adjacentes;
        this.ciclosEncontrados = new HashSet<>();
    }

    public void encontrarCiclos() {
        int n = adjacentes.length;
        for (int i = 0; i < n; i++) {
            boolean[] visitados = new boolean[n];
            List<Integer> caminhoAtual = new ArrayList<>();
            dfs(i, i, visitados, caminhoAtual);
        }
    }

    private void dfs(int verticeAtual, int verticeInicial, boolean[] visitados, List<Integer> caminhoAtual) {
        visitados[verticeAtual] = true;
        caminhoAtual.add(verticeAtual + 1);

        for (Adjacentes adj = adjacentes[verticeAtual].getProximo(); adj != null; adj = adj.getProximo()) {
            int vizinho = adj.getVertice() - 1;
            if (vizinho == verticeInicial && caminhoAtual.size() > 2) {
                // Encontramos um ciclo
                List<Integer> ciclo = new ArrayList<>(caminhoAtual);
                normalizarEAdicionarCiclo(ciclo);
            } else if (!visitados[vizinho]) {
                dfs(vizinho, verticeInicial, visitados, caminhoAtual);
            }
        }

        // Backtracking
        visitados[verticeAtual] = false;
        caminhoAtual.remove(caminhoAtual.size() - 1);
    }

    private void normalizarEAdicionarCiclo(List<Integer> cicloOriginal) {
        String representacaoCanonica = obterRepresentacaoCanonica(cicloOriginal);
        if (!ciclosEncontrados.contains(representacaoCanonica)) {
            ciclosEncontrados.add(representacaoCanonica);
            // Imprime o ciclo na ordem original de adjacência
            System.out.println("Ciclo: " + cicloOriginal);
        }
    }

    private String obterRepresentacaoCanonica(List<Integer> ciclo) {
        int tamanho = ciclo.size();
        List<String> rotacoes = new ArrayList<>();

        // Gera todas as rotações possíveis do ciclo original e seu reverso
        List<Integer> cicloOriginal = new ArrayList<>(ciclo);
        List<Integer> cicloReverso = new ArrayList<>(ciclo);
        Collections.reverse(cicloReverso);

        List<List<Integer>> todasRotacoes = gerarRotacoes(cicloOriginal);
        todasRotacoes.addAll(gerarRotacoes(cicloReverso));

        // Converter cada rotação em uma string e adicionar à lista
        for (List<Integer> rotacao : todasRotacoes) {
            rotacoes.add(rotacao.toString());
        }

        // Retorna a menor representação lexicográfica
        return Collections.min(rotacoes);
    }

    private List<List<Integer>> gerarRotacoes(List<Integer> ciclo) {
        int tamanho = ciclo.size();
        List<List<Integer>> rotacoes = new ArrayList<>();

        for (int i = 0; i < tamanho; i++) {
            List<Integer> rotacao = new ArrayList<>();
            for (int j = 0; j < tamanho; j++) {
                rotacao.add(ciclo.get((i + j) % tamanho));
            }
            rotacoes.add(rotacao);
        }
        return rotacoes;
    }
}


public class Main {
    public static void main(String[] args) {
        String nomeArq = null;
        System.out.print("\nDigite o nome do arquivo com sua extensão (.txt): ");
        nomeArq = System.console().readLine();
        Adjacentes[] adjacentes = null;
        adjacentes = constroiAdjacentes(adjacentes, nomeArq);

        CycleFinder cycleFinder = new CycleFinder(adjacentes);
        cycleFinder.encontrarCiclos();

        // // imprime a lista de adjacência
        // for (int i = 0; i < adjacentes.length; i++) {
        //     for (Adjacentes adj = adjacentes[i]; adj != null; adj = adj.getProximo()) {
        //         System.out.print(adj.getVertice() + " ");
        //     }
        //     System.out.println();
        // }
    }

    public static Adjacentes[] constroiAdjacentes(Adjacentes[] adjacentes, String nomeArq) {
        // Leitura do arquivo
        try {
            RandomAccessFile rf = new RandomAccessFile(nomeArq, "r");
            int nVertices = Integer.parseInt(rf.readLine());

            adjacentes = new Adjacentes[nVertices];
            for (int i = 0; i < nVertices; i++) {
                adjacentes[i] = new Adjacentes(i+1);
            }

            while(rf.getFilePointer() < rf.length()) {
                int[] valores = processLine(rf);

                Adjacentes aux = adjacentes[valores[0]-1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Adjacentes(valores[1]));
                aux = adjacentes[valores[1]-1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Adjacentes(valores[0]));
            }

            for (int i = 0; i < nVertices; i++) {
                adjacentes[i].setProximo(Adjacentes.ordenarAdjacentes(adjacentes[i].getProximo()));
            }            

            rf.close();

            return adjacentes;
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

