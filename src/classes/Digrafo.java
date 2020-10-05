package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Digrafo {
    protected HashMap<Integer, Vertice> lista_vertices;
    public Digrafo() { lista_vertices = new HashMap<Integer, Vertice>(); }
    protected Boolean aciclico = null, clique = null, conjunto_independente = null, split = null;
    public Integer tempo;
    private Digrafo maior_clique = null;

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
        List<Vertice> ordem_vertices = new ArrayList<>(); //essa variavel vai armazenar os vértices referentes ao grafo, pois em ordenação, podem haver vértices de outro grafo
        if(ordenacao==null){
            ordem_vertices.addAll(this.lista_vertices.values());
        }
        else
            for(Vertice v: ordenacao)
                ordem_vertices.add(this.lista_vertices.get(v.id));

        this.aciclico = true;
        for(Vertice v: this.lista_vertices.values())
            v.pai = null;
        this.tempo = 0;

        for(Vertice v1: ordem_vertices)
            if(v1.pai == null) {
                v1.pai = v1;
                visitar_busca_profundidade(v1);
            }
    }

    public void busca_profundidade(){ this.busca_profundidade(null); }

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

    public List<Vertice> get_lista_raizes(){
        List<Vertice> lista_raizes = new ArrayList<>();
        for (Vertice v: this.lista_vertices.values()) {
            v.raiz = v.get_raiz();
            if (v.pai == v)
                lista_raizes.add(v);
        }
        return lista_raizes;
    }

    public void componentes_fortemente_conexas(){
        Digrafo grafo_reverso = this.reverte();
        grafo_reverso.busca_profundidade(this.ordenacao_topologica());

        List<Vertice> lista_raizes = grafo_reverso.get_lista_raizes();

        for(Vertice raiz: lista_raizes){
            System.out.println("CFC:");
            for(Vertice v: grafo_reverso.lista_vertices.values())
                if(v.raiz == raiz)
                    v.print();
        }
    }

    public Boolean eh_clique(){
        if (this.clique != null)
            return this.clique;

        this.clique = true;
        int grau_clique = this.lista_vertices.size()-1;
        for (Vertice v: this.lista_vertices.values())
            if (v.grau < grau_clique) {
                this.clique = false;
                break;
            }

        return this.clique;
    }

    public Boolean eh_conjunto_independente(){
        if (this.conjunto_independente != null)
            return this.conjunto_independente;

        this.conjunto_independente = true;
        for (Vertice v: this.lista_vertices.values())
            if (v.get_adj().size() != 0)
                this.conjunto_independente = false;

        return this.conjunto_independente;
    }

    public Boolean eh_split(){
        int tamanho_maximo_subgrafo = this.grau_max() + 1;
        int tamanho_subgrafo = 1;

        while (tamanho_subgrafo <= tamanho_maximo_subgrafo){
            pegar_clique_do_subgrafo(tamanho_subgrafo);
            tamanho_subgrafo++;
        }

        Digrafo candidato_conjunto_independente = this.clone();
        for (Vertice vertice_do_clique: this.maior_clique.lista_vertices.values())
            candidato_conjunto_independente.remove_vertice(vertice_do_clique.id);

        System.out.println("Clique:");
        this.maior_clique.print();
        if (!candidato_conjunto_independente.eh_conjunto_independente()) {
            System.out.println("Não há conjunto independente");
            return false;
        }
        System.out.println("\nConjunto Independete:");
        candidato_conjunto_independente.print();
        System.out.println("\nÉ Split");
        return true;
    }

    public void pegar_clique_do_subgrafo(int tamanho_combinacao){

        Vertice[] vertices_do_grafo = this.lista_vertices.values().toArray(new Vertice[0]);
        int posicao_inicial = 0;
        Vertice[] vertices_da_combinacao = new Vertice[tamanho_combinacao];

        for (int i = posicao_inicial; i <= vertices_do_grafo.length-tamanho_combinacao; i++){
            vertices_da_combinacao[vertices_da_combinacao.length - tamanho_combinacao] = vertices_do_grafo[i];
            pegar_clique_do_subgrafo(vertices_do_grafo, tamanho_combinacao-1, i+1, vertices_da_combinacao);
        }
    }

    public void pegar_clique_do_subgrafo(Vertice[] vertices_do_grafo, int tamanho_combinacao, int startPosition, Vertice[] vertices_da_combinacao){
        /*
        nesse looping é que vamos encontrar se o subgrafo é ou não um clique
        ->  para gerar o subgrafo, eu estou usando o candidato_de_clique, nela eu salvo uma cópi dos vértices contendo só as arestas que ligam à vertices
            que existam no candidato_de_clique
         */
        if (tamanho_combinacao == 0){
            Digrafo candidato_de_clique = new Digrafo();

            for(Vertice v: vertices_da_combinacao)
                candidato_de_clique.add_vertice(v.id);

            for(Vertice v1: candidato_de_clique.lista_vertices.values()){
                Vertice vertice_no_grafo = this.lista_vertices.get(v1.id);
                for(Vertice v2: vertice_no_grafo.get_adj().values())
                    if (candidato_de_clique.lista_vertices.containsKey(v2.id))
                        candidato_de_clique.add_arco(v1.id, v2.id);
            }

            if (candidato_de_clique.eh_clique()) this.maior_clique = candidato_de_clique;

            return;
        }

        for (int i = startPosition; i <= vertices_do_grafo.length-tamanho_combinacao; i++){
            vertices_da_combinacao[vertices_da_combinacao.length - tamanho_combinacao] = vertices_do_grafo[i];
            pegar_clique_do_subgrafo(vertices_do_grafo, tamanho_combinacao-1, i+1, vertices_da_combinacao);
        }
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

    public void ler_arquivo(String nome_arquivo) {
        HashMap<Vertice, String> support_map = new HashMap<>(); //vai armazenar a string de adj de cada vertice
        String path = System.getProperty("user.dir") + "\\entradas\\";
        try {
            File entrada = new File(path + nome_arquivo);
            Scanner leitor = new Scanner(entrada);

            while (leitor.hasNextLine()) {
                String dados = leitor.nextLine();
                String[] vertice_e_adjacencias;
                if (dados.contains(" = ")) vertice_e_adjacencias = dados.split(" = ");
                else vertice_e_adjacencias = dados.split(" =");

                String string_vertice_id = vertice_e_adjacencias[0];
                string_vertice_id = string_vertice_id.replace(" ","");
                System.out.println(string_vertice_id);
                int vertice_id = Integer.parseInt(string_vertice_id);
                String string_adjacencias = null;

                if (vertice_e_adjacencias.length>1)
                    string_adjacencias = vertice_e_adjacencias[1];

                System.out.print(vertice_e_adjacencias);

                this.add_vertice(vertice_id);
                support_map.put(this.lista_vertices.get(vertice_id), string_adjacencias);

            }
            leitor.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        adicionar_vertices_do_arquivo(support_map);
    }

    protected void adicionar_vertices_do_arquivo(HashMap<Vertice, String> support_map){
        for (Vertice v: support_map.keySet()) {
            String string_adjacencias = support_map.get(v);
            String[] adjacencias;

            if (string_adjacencias != null) {
                adjacencias = string_adjacencias.split(" ");
                for (String id_string : adjacencias) {
                    int id = Integer.parseInt(id_string);
                    if (this.lista_vertices.containsKey(id)) this.add_arco(v.id, id);
                    else System.out.println("O vertice " + id_string + " não está no grafo");
                }
            }
        }
    }
}