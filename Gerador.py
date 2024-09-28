import networkx as nx
import random

def gerar_grafo_erdos_renyi_conexo(num_vertices, probabilidade):
    while True:
        grafo = nx.erdos_renyi_graph(num_vertices, probabilidade)
        if nx.is_connected(grafo):
            break
    return grafo

def salvar_grafo_txt(nome_arquivo, grafo):
    num_vertices = grafo.number_of_nodes()
    arestas = grafo.edges()
    
    with open(nome_arquivo, 'w') as file:
        file.write(f"{num_vertices}\n")
        for aresta in arestas:
            file.write(f"{aresta[0]+1} {aresta[1]+1}\n")  
num_vertices = 100  # Número de vértices
probabilidade = 0.20  # Probabilidade de existência de uma aresta entre dois vértices

grafo_conexo = gerar_grafo_erdos_renyi_conexo(num_vertices, probabilidade)
salvar_grafo_txt('grafo.txt', grafo_conexo)

print(f"Grafo conexo com {num_vertices} vértices gerado e salvo em grafo_erdos_renyi_conexo.txt.")
