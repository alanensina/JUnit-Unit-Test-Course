package aensina.servicos;

import static aensina.utils.DataUtils.adicionarDias;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import aensina.utils.DataUtils;
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

        if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = adicionarDias(dataEntrega, 1);
        }

        locacao.setDataRetorno(dataEntrega);

        // Salvando a locacao...
        // TODO adicionar método para salvar

        return locacao;
    }

    public double calculaValorLocacao(List<Filme> filmes) throws FilmesSemEstoqueException {
        double valorDaLocacao = 0;

        for (int i = 0; i < filmes.size(); i++) {
            Filme filme = filmes.get(i);
            Double valorDoFilme = filme.getPrecoLocacao();

            if (filme.getEstoque() == 0) {
                throw new FilmesSemEstoqueException();
            }

            switch (i) {
                case 0:
                case 1:
                    valorDoFilme = valorDoFilme * 1;
                    break;
                case 2:
                    valorDoFilme = valorDoFilme * 0.75;
                    break;
                case 3:
                    valorDoFilme = valorDoFilme * 0.5;
                    break;
                case 4:
                    valorDoFilme = valorDoFilme * 0.25;
                    break;
                case 5:
                    valorDoFilme = valorDoFilme * 0;
                    break;

                default:
                    valorDoFilme = valorDoFilme * 1;
                    break;
            }

            valorDaLocacao = valorDaLocacao + valorDoFilme;
        }

        return toFixed(valorDaLocacao, 2);
    }

    // Arredondar casas decimais
    private double toFixed(double valor, int casasDecimais) {
        BigDecimal bd = new BigDecimal(valor).setScale(casasDecimais, RoundingMode.HALF_EVEN);

        return Double.parseDouble(bd.toString());
    }
}