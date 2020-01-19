package br.ce.wcaquino.servicos;

import java.util.Date;

import org.junit.Assert;
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
		Assert.assertTrue(locacao.getValor() == 6.75);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	
}
