package models;

public class Carta {
    protected String nome;
    protected String tipo;
    protected int custo;

    public Carta(String nome, String tipo, int custo) {
        this.nome = nome;
        this.tipo = tipo;
        this.custo = custo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome + " - " + tipo + " - Custo: " + custo;
    }
}
