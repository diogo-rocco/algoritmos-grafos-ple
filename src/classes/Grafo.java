package classes;

public class Grafo extends Digrafo{

    @Override
    public void add_arco(Integer id1, Integer id2) {
        System.out.println("Não é possivel add arco em grafos não direcionados");
        super.add_aresta(id1, id2);
    }

    @Override
    protected void visitar_busca_profundidade(Vertice v1) {
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
            else if( (v2.tempo_insersao_bp < v1.tempo_insersao_bp) && (v1.pai != v2) && (v2.tempo_exploracao_bp == null))
                this.aciclico = false;
        }
        v1.tempo_exploracao_bp = ++tempo;
    }
}
