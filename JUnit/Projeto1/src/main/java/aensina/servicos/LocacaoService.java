package aensina.servicos;

import static aensina.utils.DataUtils.adicionarDias;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import aensina.dao.LocacaoDAO;
import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import aensina.exceptions.FilmesSemEstoqueException;
import aensina.exceptions.LocadoraException;
import aensina.interfaces.EmailService;
import aensina.interfaces.SPCInterface;
import aensina.utils.DataUtils;

public class LocacaoService {

    /*
     * public String vPublica; // Variável é vista na classe de pois está no mesmo pacote protected String vProtegida; // Variável é vista na classe de teste pois está no mesmo pacote private String
     * vPrivada; // Não é possível visualizar fora desta classe String vDefault; // Variável é vista na classe de teste pois está no mesmo pacote
     */

    private LocacaoDAO dao;
    private SPCInterface spc;
    private EmailService es;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmesSemEstoqueException, LocadoraException {
        boolean negativado;

        if (usuario == null) {
            throw new LocadoraException("Usuário vazio");
        }

        else if (filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        try {
            negativado = spc.possuiSaldoNegativo(usuario);
        } catch (Exception e) {
            throw new LocadoraException("Problemas com o SPC, tente novamente mais tarde!");
        }

        if (negativado) {
            throw new LocadoraException("Usuário devedor");
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
        dao.salvar(locacao);

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

    public void notificarAtrasos() {
        List<Locacao> locacoes = dao.obterLocacoesPendentes();

        for (Locacao loc : locacoes) {
            if (loc.getDataRetorno().before(new Date())) {
                es.notificarAtraso(loc.getUsuario());
            }
        }
    }

    public void prorrogarLocacao(Locacao loc, int dias) {
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(loc.getUsuario());
        novaLocacao.setFilme(loc.getFilme());
        novaLocacao.setDataLocacao(new Date());
        novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
        novaLocacao.setValor(loc.getValor() * dias);

        dao.salvar(novaLocacao);
    }
}