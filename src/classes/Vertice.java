package classes;

import java.io.Serializable;
import java.util.HashMap;

public class Vertice implements Serializable, Comparable<Vertice> {
    public Integer id;
    public Integer grau;
    private Integer cor=0;
    public Integer distancia_raiz, tempo_insersao_bp, tempo_exploracao_bp = null;
    public Vertice pai;
    private HashMap<Integer, Vertice> adj;

    public Vertice(int id ) {
        this.id = id;
        this.grau = 0;
        adj = new HashMap<Integer, Vertice>();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void add_vizinho(Vertice viz ) {
        adj.put(viz.id, viz);
        this.grau++;
    }

    public void remove_vizinho(Vertice viz){
        adj.remove(viz.id);
        this.grau--;
    }

    public HashMap<Integer, Vertice> get_adj(){
        return this.adj;
    }

    public void visitar(){
        this.cor = 1;
    }

    public void explorar(){
        this.cor = 2;
    }

    public Integer getCor(){return this.cor; }

    public void print() {
        System.out.print("Id do vertice = " + id + ", Vizinhança: " );
        for( Vertice v : adj.values())
            System.out.print(" " + v.id );
        System.out.print('\n');
    }

    //Usado na ordenação topológica, nela os vértices com maior grau de saída (os ultimos a saírem da pilha)
    //vem antes, pois são de onde as arestas estão saindo
    @Override
    public int compareTo(Vertice outro_vertice) {
        if (outro_vertice.tempo_exploracao_bp > this.tempo_exploracao_bp)
            return 1;

        else return -1;
    }
}