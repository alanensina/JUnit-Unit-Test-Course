package aensina.servicos;

import static aensina.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;

public class LocacaoService {

    /*
     * public String vPublica; // Variável é vista na classe de pois está no mesmo pacote protected String vProtegida; // Variável é vista na classe de teste pois está no mesmo pacote private String vPrivada; // Não é possível visualizar
     * fora desta classe String vDefault; // Variável é vista na classe de teste pois está no mesmo pacote
     */

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmesSemEstoqueException, LocadoraException {

        if (usuario == null) {
            throw new LocadoraException("Usuário vazio");
        }

        else if (filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        Locacao locacao = new Locacao();
        locacao.setFilme(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValor(calculaValorLocacao(filmes));

        // Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        // Salvando a locacao...
        // TODO adicionar método para salvar

        return locacao;
    }

    public double calculaValorLocacao(List<Filme> filmes) throws FilmesSemEstoqueException {
        double valorDaLocacao = 0;

        for (Filme filme : filmes) {
            if (filme.getEstoque() == 0) {
                throw new FilmesSemEstoqueException();
            }

            valorDaLocacao = valorDaLocacao + filme.getPrecoLocacao();
        }

        return valorDaLocacao;
    }
}