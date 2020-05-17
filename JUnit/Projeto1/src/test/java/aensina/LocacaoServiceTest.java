package aensina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
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

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @SuppressWarnings("deprecation")
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @SuppressWarnings("deprecation")
    @Test
    public void alugarFilme() throws Exception {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 1, 6.75);
        LocacaoService service = new LocacaoService();

        // Ação
        Locacao locacao = service.alugarFilme(usuario, filme);

        // Verificação com assertEquals e assertThat(deprecated)
        assertEquals(6.75, locacao.getValor(), 0.01);
        assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(6.75)));
        assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.25)));
        assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
        assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));

        // Error collector, verificará casos de erros nas assertivas:
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(6.75)));
        error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.25)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
    }

    @Test(expected = FilmesSemEstoqueException.class) // Recomendado para exceções criadas
    public void alugarFilmeSemEstoque() throws Exception {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 0, 6.75);
        LocacaoService service = new LocacaoService();

        // Ação
        service.alugarFilme(usuario, filme);
    }

    @Ignore // Teste ignorado pois agora está lançando uma exceção específica e não valida mais por mensagem retornada
    @Test
    public void alugarFilmeSemEstoque_v2() {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 0, 6.75);
        LocacaoService service = new LocacaoService();

        // Ação
        try {
            service.alugarFilme(usuario, filme);
            Assert.fail("Deveria ter lançado uma exceção!");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Filme indisponível no estoque.");
        }
    }

    @Ignore // Teste ignorado pois agora está lançando uma exceção específica e não valida mais por mensagem retornada
    @Test
    public void alugarFilmeSemEstoque_v3() throws Exception {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        Filme filme = new Filme("Vanilla Sky", 0, 6.75);
        LocacaoService service = new LocacaoService();
        exception.expect(Exception.class);
        exception.expectMessage("Filme indisponível no estoque.");

        // Ação
        service.alugarFilme(usuario, filme);
    }

    @Test
    public void usuarioVazio() throws FilmesSemEstoqueException {
        Filme filme = new Filme("Vanilla Sky", 1, 6.75);
        LocacaoService service = new LocacaoService();

        // Ação
        try {
            service.alugarFilme(null, filme);
            Assert.fail();
        } catch (LocadoraException e) {
            assertEquals(e.getMessage(), "Usuário vazio");
        }
    }

    @Test
    public void filmeVazio() throws FilmesSemEstoqueException, LocadoraException {
        // Iniciando as variáveis
        Usuario usuario = new Usuario("Alan");
        LocacaoService service = new LocacaoService();
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Ação
        service.alugarFilme(usuario, null);
    }
}
