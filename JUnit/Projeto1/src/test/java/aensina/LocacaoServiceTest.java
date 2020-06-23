package aensina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import aensina.dao.LocacaoDAO;
import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import aensina.exceptions.FilmesSemEstoqueException;
import aensina.exceptions.LocadoraException;
import aensina.interfaces.EmailService;
import aensina.interfaces.SPCInterface;
import aensina.servicos.LocacaoService;
import aensina.utils.DataUtils;
import builders.FilmeBuilder;
import builders.LocacaoBuilder;
import builders.UsuarioBuilder;
import matchers.MatchersProperties;

public class LocacaoServiceTest {

	@InjectMocks
    private LocacaoService service;
	
    private List<Filme> filmes = new ArrayList<Filme>(5);
    
    @Mock
    private SPCInterface spc;
    @Mock
    private LocacaoDAO dao;
    @Mock
    private EmailService es;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @SuppressWarnings("deprecation")
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // System.out.println("After");
    }

    @BeforeClass // Roda antes da classe LocacaoServiceTest ser instanciada
    public static void setupClass() {
        // System.out.println("Before class");
    }

    @AfterClass // Roda depois que todos os testes forem executados
    public static void tearDownClass() {
        // System.out.println("After class");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void deveAlugarFilme() throws Exception {

        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        // Iniciando as variáveis
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme1 = new Filme("Vanilla Sky", 1, 6.75);
        Filme filme2 = new Filme("Senhor dos Anéis - A Sociedade do Anel", 1, 6.75);
        Filme filme3 = new Filme("Star Wars - Episódio IV", 1, 6.75);
        Filme filme4 = new Filme("Harry Potter - A Pedra Filosofal", 1, 6.75);
        filmes.add(filme1);
        filmes.add(filme2);
        filmes.add(filme3);
        filmes.add(filme4);

        // Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // Verificação com assertEquals e assertThat(deprecated)
        assertEquals(21.94, locacao.getValor(), 0.01);
        assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(21.94)));
        assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(25.0)));
        assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
        assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        assertThat(locacao.getDataLocacao(), MatchersProperties.ehHoje());
        assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
        assertThat(locacao.getDataRetorno(), MatchersProperties.ehHojeComDiferencaDeDias(1));

        // Error collector, verificará casos de erros nas assertivas:
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(21.94)));
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(25.0)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
    }

    @Test(expected = FilmesSemEstoqueException.class) // Recomendado para exceções criadas
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        // Iniciando as variáveis
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme = FilmeBuilder.umFilme().semEstoque().agora();
        filmes.add(filme);

        // Ação
        service.alugarFilme(usuario, filmes);
    }

    @Ignore // Teste ignorado pois agora está lançando uma exceção específica e não valida mais por mensagem retornada
    @Test
    public void deveLancarFailAoAlugarFilmeSemEstoque() {
        // Iniciando as variáveis
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme = FilmeBuilder.umFilmeSemEstoque().agora();
        filmes.add(filme);

        // Ação
        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail("Deveria ter lançado uma exceção!");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Filme indisponível no estoque.");
        }
    }

    @Ignore // Teste ignorado pois agora está lançando uma exceção específica e não valida mais por mensagem retornada
    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque_v2() throws Exception {
        // Iniciando as variáveis
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme = FilmeBuilder.umFilme().semEstoque().agora();
        filmes.add(filme);
        exception.expect(Exception.class);
        exception.expectMessage("Filme indisponível no estoque.");

        // Ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarSemUsuario() throws FilmesSemEstoqueException {
        Filme filme = FilmeBuilder.umFilme().agora();
        filmes.add(filme);

        // Ação
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertEquals(e.getMessage(), "Usuário vazio");
        }
    }

    @Test
    public void naoDeveAlugarSeAListaDeFilmesEstiverVazia() throws FilmesSemEstoqueException, LocadoraException {
        // Iniciando as variáveis
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Ação
        service.alugarFilme(usuario, null);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueException, LocadoraException {

        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        // ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificação
        boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        assertTrue(ehSegunda);

        assertThat(locacao.getDataRetorno(), MatchersProperties.caiEm(Calendar.MONDAY));
        assertThat(locacao.getDataRetorno(), MatchersProperties.caiEmUmaSegunda());

    }

    // Foi importado um jar do BuilderMaster
    // Rodar como Java aplication e pegar no console o builder do Locacao
    // public static void main(String[] args) {
    // new BuilderMaster().gerarCodigoClasse(Locacao.class);
    // }

    @Test
    public void naoDeveAlugarFilmeParaUsuarioDevedor() throws FilmesSemEstoqueException {

        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
        Mockito.when(spc.possuiSaldoNegativo(Mockito.any(Usuario.class))).thenReturn(true);

        // ação
        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário devedor"));
        }

        Mockito.verify(spc).possuiSaldoNegativo(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacaoAtrasada() {
        // cenário
        Usuario usuario1 = UsuarioBuilder.umUsuario().comNome("Usuario atrasado 1").agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario sem atraso").agora();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Usuario atrasado 2").agora();
        List<Locacao> locacoes = Arrays.asList(
                LocacaoBuilder.umaLocacao().atrasada().comUsuario(usuario1).agora(),
                LocacaoBuilder.umaLocacao().atrasada().comUsuario(usuario3).agora(),
                LocacaoBuilder.umaLocacao().comUsuario(usuario2).agora());

        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        // ação
        service.notificarAtrasos();

        // verificação
        Mockito.verify(es).notificarAtraso(usuario1);
        Mockito.verify(es, Mockito.never()).notificarAtraso(usuario2);
        Mockito.verify(es).notificarAtraso(usuario3);
    }

}
