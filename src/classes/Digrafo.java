package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Digrafo {
    private HashMap<Integer, Vertice> lista_vertices;
    public Digrafo() { lista_vertices = new HashMap<Integer, Vertice>(); }
    protected Boolean aciclico = null;
    public Integer tempo;

    protected void add_vertice() {
        Vertice v = new Vertice( lista_vertices.size()+1 );
        lista_vertices.put( v.id, v );
    }

    protected void add_vertice(int id) {
        if (this.lista_vertices.get(id) == null){
            Vertice v = new Vertice( id );
            lista_vertices.put( v.id, v );
        } else System.out.println("Já existe um vértice com esse id");
    }

    public void remove_vertice(int id){
        this.lista_vertices.remove(id);
        for(Vertice v1: this.lista_vertices.values()) {
            ArrayList<Vertice> lista_vertices = new ArrayList<>(v1.get_adj().values());
            for (Vertice v2: lista_vertices)
                if (v2.id == id)
                    v1.remove_vizinho(v2);
        }
    }

    public void compacta(){
        int contador = 1;
        for(Vertice v1: this.lista_vertices.values()) {
            if (v1.id > contador)
                v1.setId(contador);
            contador++;
        }
    }

    public void add_arco(Integer id1, Integer id2) {
        Vertice v1 = lista_vertices.get(id1);
        Vertice v2 = lista_vertices.get(id2);
        v1.add_vizinho( v2 );
    }

    public void add_aresta(Integer id1, Integer id2) {
        Vertice v1 = lista_vertices.get(id1);
        Vertice v2 = lista_vertices.get(id2);
        v1.add_vizinho( v2 );
        v2.add_vizinho( v1 );

// ou
//        add_arc( id1, id2 );
//        add_arc( id2, id1 );
    }

    public int grau_max() {
        int max = 0;
        for(Vertice v1: this.lista_vertices.values())
            if(v1.grau > max) max = v1.grau;
        return max;
    }

    public boolean eh_direcionado() {
        for(Vertice v1: lista_vertices.values()){
            for(Vertice v2: v1.get_adj().values()){
                if(!v2.get_adj().containsKey(v1.id))
                    return false;
            }
        }
        return true;
    }

    public Digrafo grafo_subjacente() {
        Digrafo grafo_subjacente = this.clone();
        for(Vertice v1: grafo_subjacente.lista_vertices.values())
            for(Vertice v2: v1.get_adj().values())
                if(!v2.get_adj().containsKey(v1.id))
                    grafo_subjacente.add_arco(v2.id, v1.id);
        return grafo_subjacente;
    }

    public boolean eh_conexo() {
        int id_raiz = 1;
        while (this.lista_vertices.get(id_raiz) == null)
            id_raiz++;
        Digrafo abl = this.busca_largura(id_raiz);
        return this.lista_vertices.size() == abl.lista_vertices.size();
    }

    public boolean eh_aciclico() {
        if(this.aciclico == null)
            this.busca_profundidade();
        return this.aciclico;
    }

    //TODO CONTAR COMPONENTES CONEXOS

    public Digrafo busca_largura(Integer id_raiz ) {
        Digrafo abl = new Digrafo();
        Vertice raiz = this.lista_vertices.get( id_raiz );
        int index = 0;
        ArrayList<Vertice> fila_busca = new ArrayList<>();

        fila_busca.add(raiz);
        abl.add_vertice(id_raiz);
        abl.lista_vertices.get(id_raiz).distancia_raiz = 0;

        while (index< this.lista_vertices.size()){
            boolean fim_da_busca = true;
            for(Vertice v: fila_busca) {
                if (v.getCor() != 2) {
                    fim_da_busca = false;
                    break;
                }
            }
            if (fim_da_busca) {
                return abl;
            }


            Vertice v1 = fila_busca.get(index);
            for(Vertice v2: v1.get_adj().values()){
                if(v2.getCor() == 0){
                    v2.visitar();
                    v2.distancia_raiz = v1.distancia_raiz+1;
                    fila_busca.add(v2);
                    abl.add_vertice(v2.id);
                    abl.add_aresta(v1.id, v2.id);
                }
            }
            v1.explorar();
            index++;
        }
        return abl;
    }

    public void busca_profundidade(List<Vertice> ordenacao){
        if(ordenacao==null){
            ordenacao = new ArrayList<Vertice>();
            ordenacao.addAll(this.lista_vertices.values());
        }
        this.aciclico = true;
        for(Vertice v: this.lista_vertices.values())
            v.pai = null;
        this.tempo = 0;

        for(Vertice v1: ordenacao)
            if(v1.pai == null) {
                v1.pai = v1;
                visitar_busca_profundidade(v1);
            }
    }

    public void busca_profundidade(){
        this.busca_profundidade(null);
    }

    protected void visitar_busca_profundidade(Vertice v1){
        v1.tempo_insersao_bp = ++this.tempo;
        for(Vertice v2: v1.get_adj().values()){
            if(v2.pai == null){
                v2.pai = v1;
                visitar_busca_profundidade(v2);
            }
            /*
            No bloco abaixo, quer dizer que durante a exploração de V1, ele encontrou um vértice que já foi vizitado antes,
            sendo assim, existe um caminho de v2 até v1, E existe uma areste que liga v2 à v1, logo existe um ciclo
             */
            else if( (v2.tempo_insersao_bp < v1.tempo_insersao_bp) && (v2.tempo_exploracao_bp == null))
                this.aciclico = false;
        }
        v1.tempo_exploracao_bp = ++tempo;
    }

    public List<Vertice> ordenacao_topologica() {
        if(this.aciclico == null)
            this.busca_profundidade();

        if(!this.aciclico){
            System.out.println("O grafo contém ciclo");
            return null;
        }

        List<Vertice> ordem_topologica = new ArrayList<>(this.lista_vertices.values());
        Collections.sort(ordem_topologica);

        System.out.println("Ordenação Topológica:");
        for(Vertice v: ordem_topologica)
            System.out.println("id: " + v.id + " ordem de exploração: " + v.tempo_exploracao_bp);

        return ordem_topologica;
    }

    public Digrafo reverte(){
        Digrafo digrafo_reverso = new Digrafo();
        for (Vertice v: this.lista_vertices.values())
            digrafo_reverso.add_vertice(v.id);

        for (Vertice v1: this.lista_vertices.values())
            for (Vertice v2: v1.get_adj().values())
                digrafo_reverso.add_arco(v2.id, v1.id);

        return digrafo_reverso;
    }

    public void componentes_fortemente_conexas(){

    }

    public void print() {
        System.out.print("Grafo:\n");

        for(Integer id: this.lista_vertices.keySet())
            lista_vertices.get(id).print();
    }

    public Digrafo clone(){
        Digrafo copia = new Digrafo();
        for(int id: this.lista_vertices.keySet()){
            if(!copia.lista_vertices.containsKey(id))
                copia.add_vertice(id);
            for(int id_2: this.lista_vertices.get(id).get_adj().keySet()) {
                if (!copia.lista_vertices.containsKey(id_2))
                    copia.add_vertice(id_2);
                copia.add_arco(id, id_2);
            }
        }
        return copia;
    }
}