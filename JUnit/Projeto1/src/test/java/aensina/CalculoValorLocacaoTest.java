package aensina;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import aensina.exceptions.FilmesSemEstoqueException;
import aensina.exceptions.LocadoraException;
import aensina.servicos.LocacaoService;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	private LocacaoService service;

	private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 2, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 2, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 2, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 2, 4.0);
	private static Filme filme7 = new Filme("Filme 7", 2, 4.0);
	
	@Parameter //value = 0 por default, é a primeira posição do array
	public List<Filme> filmes;
	
	@Parameter(value=1) // value 1 pois é a segunda posição do array
	public double valorLocacao;
	
	@Parameter(value=2) // value 2 pois é a terceira posição do array
	public String cenario;
	
	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { 
				{ Arrays.asList(filme1, filme2), 8.0, "2 filmes: Sem desconto"},
				{ Arrays.asList(filme1, filme2, filme3), 11.0, "3 filmes: 25%"},
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 filmes: 50%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 filmes: 75%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 filmes: 100%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 filmes: Sem desconto" }				
		});
	}

	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmesSemEstoqueException, LocadoraException {
		// cenário
		Usuario usuario = new Usuario("Alan");

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		assertThat(locacao.getValor(), CoreMatchers.is(valorLocacao));
	}

}
