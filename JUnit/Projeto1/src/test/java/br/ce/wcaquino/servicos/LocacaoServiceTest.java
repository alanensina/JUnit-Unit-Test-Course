package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Test
	public void alugarFilme() {
		// Iniciando as variáveis
		Usuario usuario = new Usuario("Alan");
		Filme filme = new Filme("Vanilla Sky", 1, 6.75);
		LocacaoService service = new LocacaoService();
		
		// Ação
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		// Verificação
		assertEquals(6.75, locacao.getValor(), 0.01);
		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	
}
