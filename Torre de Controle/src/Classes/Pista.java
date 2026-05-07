package Classes;

public class Pista {

    private final String numero;
    private boolean ocupada;

    public Pista(String numero) {
        this.numero = numero;
        this.ocupada = false;
    }

    public String getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar() {
        this.ocupada = true;
    }

    public void liberar() {
        this.ocupada = false;
    }

    @Override
    public String toString() {
        return "Pista " + numero + " [" + (ocupada ? "OCUPADA" : "LIVRE") + "]";
    }
}