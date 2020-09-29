package classes;

import java.util.*;

public class AlgGrafos {
    public static void main(String args[]){

        testar_ordenacao_topologica();
    }
    public Integer inteito = 1;
    public static void printar_exemplo(){
        Grafo g1 = new Grafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_aresta(1,2);
        g1.add_aresta(1,3);
        g1.add_aresta(1,4);
        g1.print();
    }

    public static void testar_grau_max(){
        Grafo g1 = new Grafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_aresta(1,2);
        g1.add_aresta(1,3);
        g1.add_aresta(1,4);
        g1.print();
        System.out.print("O Grau Máximo é " + g1.grau_max());
    }

    public static void testar_nao_direcionado(){
        Digrafo g1 = new Digrafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_arco(1,2);
        g1.add_arco(2,3);
        g1.add_arco(2,4);
        g1.print();
        System.out.print("Grafo não direcionado: " + g1.eh_direcionado());
    }

    public static void testar_subjacente(){
        Digrafo g1 = new Digrafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_arco(1,2);
        g1.add_arco(2,3);
        g1.add_arco(2,4);
        g1.print();
        System.out.println("----------------");
        g1.grafo_subjacente().print();
        System.out.println("----------------");
        g1.print();
    }

    public static Grafo testar_remove_vertice(){
        Grafo g1 = new Grafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_aresta(1,2);
        g1.add_aresta(1,4);
        g1.add_aresta(2,3);
        g1.add_aresta(2,4);
        g1.add_aresta(3,4);
        g1.add_aresta(4,5);
        System.out.println("Grafo antes:");
        g1.print();
        g1.remove_vertice(2);
        System.out.println("Grafo depois:");
        g1.print();
        return g1;
    }

    public static void testar_compactar(){
        Grafo g1 = testar_remove_vertice();
        System.out.println("Grafo compactado");
        g1.compacta();
        g1.print();
    }

    public static void testar_bl() {
        Grafo g1 = new Grafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_aresta(1,2);
        g1.add_aresta(2,3);
        g1.add_aresta(2,4);
        g1.add_aresta(4,3);
        g1.add_aresta(4,5);
        System.out.println("Grafo G:");
        g1.print();
        System.out.println("Arvore de Busca em Largura");
        g1.busca_largura(2).print();
    }

    public static void testar_conexo() {
        Grafo g1 = new Grafo();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_vertice();
        g1.add_aresta(1,2);
        g1.add_aresta(2,3);
        g1.add_aresta(2,4);
        if(g1.eh_conexo())
            System.out.println("Conexo");
        else
            System.out.println("Não é conexo");
    }

    public static void testar_busca_profundidade() {
        Grafo ciclico = new Grafo();
        Grafo aciclico = new Grafo();

        ciclico.add_vertice();
        aciclico.add_vertice();

        ciclico.add_vertice();
        aciclico.add_vertice();

        ciclico.add_vertice();
        aciclico.add_vertice();

        ciclico.add_vertice();
        aciclico.add_vertice();

        ciclico.add_aresta(1,2);
        aciclico.add_aresta(1,2);

        ciclico.add_aresta(2,3);
        aciclico.add_aresta(2,3);

        ciclico.add_aresta(2,4);
        aciclico.add_aresta(2,4);

        ciclico.add_aresta(3,4);

        ciclico.busca_profundidade();
        aciclico.busca_profundidade();

        System.out.println("o Grafo acicilico é aciclico: " + aciclico.eh_aciclico());
        aciclico.print();
        System.out.println("o Grafo cicilico é aciclico: " + ciclico.eh_aciclico());
        ciclico.print();
    }

    public static void testar_ordenacao_vertices() {
        Vertice v1 = new Vertice(1);
        Vertice v2 = new Vertice(2);
        Vertice v3 = new Vertice(3);
        v1.tempo_exploracao_bp = 1;
        v2.tempo_exploracao_bp = 2;
        v3.tempo_exploracao_bp = 3;

        List<Vertice> lista = new ArrayList<>();
        lista.add(v3);
        lista.add(v1);
        lista.add(v2);

        System.out.println("Lista desordenada:");
        for(Vertice v: lista)
            v.print();

        Collections.sort(lista);

        System.out.println("Lista ordenada:");
        for(Vertice v: lista)
            v.print();
    }

    public static void testar_ordenacao_topologica(){
        Digrafo grafo = new Digrafo();
        grafo.add_vertice();
        grafo.add_vertice();
        grafo.add_vertice();
        grafo.add_vertice();

        grafo.add_arco(1,2);
        grafo.add_arco(1,3);
        //grafo.add_arco(2,3);
        grafo.add_arco(3,2);
        grafo.add_arco(2,4);
        grafo.add_arco(3,4);

        grafo.print();

        grafo.ordenacao_topologica();
    }
}