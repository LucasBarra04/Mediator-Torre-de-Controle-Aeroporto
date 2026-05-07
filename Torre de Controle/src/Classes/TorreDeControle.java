package Classes;

public interface TorreDeControle {

    void registrarAviao(Aviao aviao);

    void solicitarPouso(Aviao aviao);

    void solicitarDecolagem(Aviao aviao);

    void notificarPistaLivre(String numeroPista);
}