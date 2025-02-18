package models;

public class CartaCriatura extends Carta {
    private int poder;
    private int resistencia;

    public CartaCriatura(String nome, int custo, int poder, int resistencia) {
        super(nome, "Criatura", custo);
        this.poder = poder;
        this.resistencia = resistencia;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + poder + "/" + resistencia;
    }
}
