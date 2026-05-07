package Classes;

public class AviaoCarga extends Aviao {

    private final double pesoToneladas;

    public AviaoCarga(String codigo, double pesoToneladas) {
        super(codigo);
        this.pesoToneladas = pesoToneladas;
    }

    public double getPesoToneladas() {
        return pesoToneladas;
    }

    @Override
    public void receberPermissaoPouso(String numeroPista) {
        status = StatusAviao.POUSADO;
        System.out.println("[" + codigo + " CARGA " + pesoToneladas + "t] Pousando na pista " + numeroPista + " com cautela.");
    }

    @Override
    public void receberPermissaoDecolagem(String numeroPista) {
        status = StatusAviao.EM_DECOLAGEM;
        System.out.println("[" + codigo + " CARGA " + pesoToneladas + "t] Decolando pela pista " + numeroPista + ".");
    }
}