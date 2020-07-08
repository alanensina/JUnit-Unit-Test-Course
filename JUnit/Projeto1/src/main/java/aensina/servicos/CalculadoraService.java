package aensina.servicos;

import aensina.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraService {

    public int somar(int a, int b) {
        System.out.println("Estou executando o método somar...");
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) throws NaoPodeDividirPorZeroException {

        if (b == 0) {
            throw new NaoPodeDividirPorZeroException();
        }

        return a / b;
    }

    public void imprime() {
        System.out.println("Imprimindo...");
    }

}
