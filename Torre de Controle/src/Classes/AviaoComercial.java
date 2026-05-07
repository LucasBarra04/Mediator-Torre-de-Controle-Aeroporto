package Classes;

public class AviaoComercial extends Aviao {

    private final String companhia;

    public AviaoComercial(String codigo, String companhia) {
        super(codigo);
        this.companhia = companhia;
    }

    public String getCompanhia() {
        return companhia;
    }

    @Override
    public void receberPermissaoPouso(String numeroPista) {
        status = StatusAviao.POUSADO;
        System.out.println("[" + codigo + " - " + companhia + "] Permissão recebida. Pousando na pista " + numeroPista + ".");
    }

    @Override
    public void receberPermissaoDecolagem(String numeroPista) {
        status = StatusAviao.EM_DECOLAGEM;
        System.out.println("[" + codigo + " - " + companhia + "] Permissão recebida. Decolando pela pista " + numeroPista + ".");
    }
}