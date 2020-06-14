package builders;

import java.util.Arrays;
import java.util.Date;

import aensina.entidades.Filme;
import aensina.entidades.Locacao;
import aensina.entidades.Usuario;
import aensina.utils.DataUtils;

public class LocacaoBuilder {
    private Locacao locacao;

    private LocacaoBuilder() {
    }

    public static LocacaoBuilder umLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(LocacaoBuilder builder) {
        builder.locacao = new Locacao();
        Locacao elemento = builder.locacao;

        elemento.setUsuario(UsuarioBuilder.umUsuario().agora());
        elemento.setFilme(Arrays.asList(FilmeBuilder.umFilme().agora()));
        elemento.setDataLocacao(new Date());
        elemento.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));
        elemento.setValor(4.0);
    }

    public LocacaoBuilder comUsuario(Usuario param) {
        locacao.setUsuario(param);
        return this;
    }

    public LocacaoBuilder comListaFilme(Filme... params) {
        locacao.setFilme(Arrays.asList(params));
        return this;
    }

    public LocacaoBuilder comDataLocacao(Date param) {
        locacao.setDataLocacao(param);
        return this;
    }

    public LocacaoBuilder comDataRetorno(Date param) {
        locacao.setDataRetorno(param);
        return this;
    }

    public LocacaoBuilder comValor(Double param) {
        locacao.setValor(param);
        return this;
    }

    public Locacao agora() {
        return locacao;
    }
}
