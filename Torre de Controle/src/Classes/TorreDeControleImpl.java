package Classes;

import java.util.*;

public class TorreDeControleImpl implements TorreDeControle {

    private final String nome;
    private final Map<String, Aviao> avioes = new LinkedHashMap<>();
    private final List<Pista> pistas = new ArrayList<>();

    private final Queue<Aviao> filaPouso = new LinkedList<>();
    private final Queue<Aviao> filaDecolagem = new LinkedList<>();

    public TorreDeControleImpl(String nome) {
        this.nome = nome;
    }

    public void adicionarPista(Pista pista) {
        pistas.add(pista);
    }

    @Override
    public void registrarAviao(Aviao aviao) {
        aviao.setTorre(this);
        avioes.put(aviao.getCodigo(), aviao);
        System.out.println("[TORRE " + nome + "] Avião " + aviao.getCodigo() + " registrado.");
    }

    @Override
    public void solicitarPouso(Aviao aviao) {
        Optional<Pista> pistaLivre = encontrarPistaLivre();

        if (pistaLivre.isPresent()) {
            Pista pista = pistaLivre.get();
            pista.ocupar();
            System.out.println("[TORRE " + nome + "] Autorizado pouso de " + aviao.getCodigo() + " na " + pista);
            aviao.receberPermissaoPouso(pista.getNumero());
        } else {
            System.out.println("[TORRE " + nome + "] Sem pista livre. Avião " + aviao.getCodigo() + " adicionado à fila de pouso.");
            filaPouso.offer(aviao);
        }
    }

    @Override
    public void solicitarDecolagem(Aviao aviao) {
        Optional<Pista> pistaLivre = encontrarPistaLivre();

        if (pistaLivre.isPresent()) {
            Pista pista = pistaLivre.get();
            pista.ocupar();
            System.out.println("[TORRE " + nome + "] Autorizada decolagem de " + aviao.getCodigo() + " pela " + pista);
            aviao.receberPermissaoDecolagem(pista.getNumero());
        } else {
            System.out.println("[TORRE " + nome + "] Sem pista livre. Avião " + aviao.getCodigo() + " adicionado à fila de decolagem.");
            filaDecolagem.offer(aviao);
        }
    }

    @Override
    public void notificarPistaLivre(String numeroPista) {
        Pista pista = pistas.stream()
                .filter(p -> p.getNumero().equals(numeroPista))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pista não encontrada: " + numeroPista));

        pista.liberar();
        System.out.println("[TORRE " + nome + "] Pista " + numeroPista + " liberada.");

        if (!filaPouso.isEmpty()) {
            Aviao proximo = filaPouso.poll();
            pista.ocupar();
            System.out.println("[TORRE " + nome + "] Processando fila — pouso de " + proximo.getCodigo());
            proximo.receberPermissaoPouso(pista.getNumero());

        } else if (!filaDecolagem.isEmpty()) {
            Aviao proximo = filaDecolagem.poll();
            pista.ocupar();
            System.out.println("[TORRE " + nome + "] Processando fila — decolagem de " + proximo.getCodigo());
            proximo.receberPermissaoDecolagem(pista.getNumero());
        }
    }

    private Optional<Pista> encontrarPistaLivre() {
        return pistas.stream().filter(p -> !p.isOcupada()).findFirst();
    }

    public int getTamanhFilaPouso() {
        return filaPouso.size();
    }

    public int getTamanhoFilaDecolagem() {
        return filaDecolagem.size();
    }

    public List<Pista> getPistas() {
        return Collections.unmodifiableList(pistas);
    }

    public Map<String, Aviao> getAvioes() {
        return Collections.unmodifiableMap(avioes);
    }

    public String getNome() {
        return nome;
    }
}