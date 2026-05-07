package Classes;

public abstract class Aviao {

    protected final String codigo;
    protected StatusAviao status;
    protected TorreDeControle torre;

    public Aviao(String codigo) {
        this.codigo = codigo;
        this.status = StatusAviao.EM_VOO;
    }

    public void setTorre(TorreDeControle torre) {
        this.torre = torre;
    }

    public String getCodigo() {
        return codigo;
    }

    public StatusAviao getStatus() {
        return status;
    }

    public void setStatus(StatusAviao status) {
        this.status = status;
    }

    public void pedirPouso() {
        System.out.println("[" + codigo + "] Solicitando pouso à torre.");
        status = StatusAviao.AGUARDANDO_POUSO;
        torre.solicitarPouso(this);
    }

    public void pedirDecolagem() {
        System.out.println("[" + codigo + "] Solicitando decolagem à torre.");
        status = StatusAviao.AGUARDANDO_DECOLAGEM;
        torre.solicitarDecolagem(this);
    }

    public abstract void receberPermissaoPouso(String numeroPista);

    public abstract void receberPermissaoDecolagem(String numeroPista);

    @Override
    public String toString() {
        return "Aviao{codigo='" + codigo + "', status=" + status + "}";
    }
}
