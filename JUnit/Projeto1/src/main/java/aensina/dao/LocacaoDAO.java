package aensina.dao;

import java.util.List;

import aensina.entidades.Locacao;

public interface LocacaoDAO {

    public void salvar(Locacao loc);

    public List<Locacao> obterLocacoesPendentes();
}
