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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import aensina.servicos.LocacaoService;
import aensina.utils.DataUtils;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;

public class LocacaoServiceTest {

    private LocacaoService service;
    private List<Filme> filmes;
    // private static int contadorDeTestes = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @SuppressWarnings("deprecation")
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
        filmes = new ArrayList<Filme>();
        // System.out.println("Before");
        // contadorDeTestes++;
        // System.out.println("Contador: " + contadorDeTestes);

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
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
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
        assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));

        // Error collector, verificará casos de erros nas assertivas:
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(21.94)));
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(25.0)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
    }

    @Test(expected = FilmesSemEstoqueException.class) // Recomendado para exceções criadas
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 0, 6.75);
        filmes.add(filme);

        // Ação
        service.alugarFilme(usuario, filmes);
    }

    @Ignore // Teste ignorado pois agora está lançando uma exceção específica e não valida mais por mensagem retornada
    @Test
    public void deveLancarFailAoAlugarFilmeSemEstoque() {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 0, 6.75);
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
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 0, 6.75);
        filmes.add(filme);
        exception.expect(Exception.class);
        exception.expectMessage("Filme indisponível no estoque.");

        // Ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarSemUsuario() throws FilmesSemEstoqueException {
        Filme filme = new Filme("Vanilla Sky", 1, 6.75);
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
        Usuario usuario = new Usuario("Alan");
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Ação
        service.alugarFilme(usuario, null);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void devePagar75pctNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
        // cenário
        Usuario usuario = new Usuario("Alan");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0));

        // ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificação
        assertThat(locacao.getValor(), CoreMatchers.is(11.0));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void devePagar50pctNoFilme4() throws FilmesSemEstoqueException, LocadoraException {
        // cenário
        Usuario usuario = new Usuario("Alan");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0));

        // ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificação
        assertThat(locacao.getValor(), CoreMatchers.is(13.0));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void devePagar25pctNoFilme5() throws FilmesSemEstoqueException, LocadoraException {
        // cenário
        Usuario usuario = new Usuario("Alan");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0));

        // ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificação
        assertThat(locacao.getValor(), CoreMatchers.is(14.0));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void devePagar0pctNoFilme6() throws FilmesSemEstoqueException, LocadoraException {
        // cenário
        Usuario usuario = new Usuario("Alan");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 2, 4.0));

        // ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificação
        assertThat(locacao.getValor(), CoreMatchers.is(14.0));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueException, LocadoraException {

        // cenario
        Usuario usuario = new Usuario("Alan");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0));

        // ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificação
        boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        assertTrue(ehSegunda);
    }

}
