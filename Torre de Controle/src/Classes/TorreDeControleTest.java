package Classes;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Torre de Controle — Padrão Mediator")
class TorreDeControleTest {

    private TorreDeControleImpl torre;
    private AviaoComercial la1234;
    private AviaoComercial g31001;
    private AviaoCarga     az9900;

    @BeforeEach
    void setUp() {
        torre = new TorreDeControleImpl("GRU");
        torre.adicionarPista(new Pista("09L"));
        torre.adicionarPista(new Pista("09R"));

        la1234 = new AviaoComercial("LA1234", "LATAM");
        g31001 = new AviaoComercial("G31001", "GOL");
        az9900 = new AviaoCarga("AZ9900", 80.0);

        torre.registrarAviao(la1234);
        torre.registrarAviao(g31001);
        torre.registrarAviao(az9900);
    }

    @Test
    @DisplayName("Deve registrar aviões e injetar referência da torre")
    void deveRegistrarAviaoComReferenciaDaTorre() {
        assertEquals(3, torre.getAvioes().size());
        assertTrue(torre.getAvioes().containsKey("LA1234"));
        assertTrue(torre.getAvioes().containsKey("G31001"));
        assertTrue(torre.getAvioes().containsKey("AZ9900"));
    }

    @Test
    @DisplayName("Deve autorizar pouso quando há pista livre")
    void deveAutorizarPousoComPistaLivre() {
        la1234.pedirPouso();

        assertEquals(StatusAviao.POUSADO, la1234.getStatus());
        assertEquals(0, torre.getTamanhFilaPouso());
    }

    @Test
    @DisplayName("Deve ocupar a pista após autorizar o pouso")
    void deveOcuparPistaAposPouso() {
        la1234.pedirPouso();

        long pistasOcupadas = torre.getPistas().stream().filter(Pista::isOcupada).count();
        assertEquals(1, pistasOcupadas);
    }

    @Test
    @DisplayName("Deve autorizar dois pousos simultâneos com duas pistas")
    void deveAutorizarDoisPousosSimultaneos() {
        la1234.pedirPouso();
        g31001.pedirPouso();

        assertEquals(StatusAviao.POUSADO, la1234.getStatus());
        assertEquals(StatusAviao.POUSADO, g31001.getStatus());
        assertEquals(0, torre.getTamanhFilaPouso());

        long pistasOcupadas = torre.getPistas().stream().filter(Pista::isOcupada).count();
        assertEquals(2, pistasOcupadas);
    }

    @Test
    @DisplayName("Deve enfileirar avião quando todas as pistas estiverem ocupadas (pouso)")
    void deveEnfileirarQuandoNaoHaPistaParaPouso() {
        la1234.pedirPouso();
        g31001.pedirPouso();
        az9900.pedirPouso();

        assertEquals(StatusAviao.AGUARDANDO_POUSO, az9900.getStatus());
        assertEquals(1, torre.getTamanhFilaPouso());
    }

    @Test
    @DisplayName("Deve processar fila de pouso ao liberar pista")
    void deveProcessarFilaDePouso() {
        la1234.pedirPouso();
        g31001.pedirPouso();
        az9900.pedirPouso();

        torre.notificarPistaLivre("09L");

        assertEquals(StatusAviao.POUSADO, az9900.getStatus());
        assertEquals(0, torre.getTamanhFilaPouso());
    }

    @Test
    @DisplayName("Deve autorizar decolagem quando há pista livre")
    void deveAutorizarDecolagem() {
        la1234.setStatus(StatusAviao.POUSADO);
        la1234.pedirDecolagem();

        assertEquals(StatusAviao.EM_DECOLAGEM, la1234.getStatus());
        assertEquals(0, torre.getTamanhoFilaDecolagem());
    }

    @Test
    @DisplayName("Deve enfileirar decolagem quando não há pista livre")
    void deveEnfileirarDecolagem() {
        la1234.pedirPouso();
        g31001.pedirPouso();

        az9900.pedirDecolagem();

        assertEquals(StatusAviao.AGUARDANDO_DECOLAGEM, az9900.getStatus());
        assertEquals(1, torre.getTamanhoFilaDecolagem());
    }


    @Test
    @DisplayName("Deve priorizar pouso sobre decolagem ao liberar pista")
    void devePriorizarPousoSobreDecolagem() {
        la1234.pedirPouso();
        g31001.pedirPouso();

        AviaoComercial ad4500 = new AviaoComercial("AD4500", "Azul");
        torre.registrarAviao(ad4500);

        ad4500.pedirDecolagem();
        az9900.pedirPouso();

        torre.notificarPistaLivre("09L");

        assertEquals(StatusAviao.POUSADO, az9900.getStatus());
        assertEquals(StatusAviao.AGUARDANDO_DECOLAGEM, ad4500.getStatus());
        assertEquals(1, torre.getTamanhoFilaDecolagem());
    }

    @Test
    @DisplayName("Deve lançar exceção ao notificar pista inexistente")
    void deveLancarExcecaoParaPistaInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> torre.notificarPistaLivre("INVALIDA"));
    }

    @Test
    @DisplayName("Avião deve iniciar com status EM_VOO")
    void aviaoDeveIniciarEmVoo() {
        AviaoComercial novo = new AviaoComercial("XX9999", "Test");
        assertEquals(StatusAviao.EM_VOO, novo.getStatus());
    }

    @Test
    @DisplayName("Avião de carga deve guardar o peso corretamente")
    void aviaoDeveGuardarPeso() {
        assertEquals(80.0, az9900.getPesoToneladas());
    }

    @Test
    @DisplayName("Pista deve iniciar livre")
    void pistaDeveIniciarLivre() {
        Pista pista = new Pista("27R");
        assertFalse(pista.isOcupada());
    }

    @Test
    @DisplayName("Pista deve ficar ocupada e ser liberada corretamente")
    void pistaDeveOcuparELiberar() {
        Pista pista = new Pista("27R");
        pista.ocupar();
        assertTrue(pista.isOcupada());

        pista.liberar();
        assertFalse(pista.isOcupada());
    }
}