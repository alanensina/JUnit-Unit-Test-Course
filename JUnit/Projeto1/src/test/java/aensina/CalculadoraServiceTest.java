package aensina;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import aensina.servicos.CalculadoraService;
import aensina.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraServiceTest {

    private CalculadoraService calc;

    @Before
    public void setup() {
        calc = new CalculadoraService();
    }

    @Test
    public void deveSomarDoisValores() {

        // cenário
        int a = 5;
        int b = 3;

        // ação
        int resultado = calc.somar(a, b);

        // verificação
        assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {

        // cenário
        int a = 5;
        int b = 3;

        // ação
        int resultado = calc.subtrair(a, b);

        // verificação
        assertEquals(2, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {

        // cenário
        int a = 10;
        int b = 5;

        // ação
        int resultado = calc.dividir(a, b);

        // verificação
        assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {

        // cenário
        int a = 10;
        int b = 0;

        // ação
        calc.dividir(a, b);
    }

}
