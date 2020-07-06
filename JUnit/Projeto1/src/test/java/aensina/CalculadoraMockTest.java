package aensina;

import org.junit.Test;
import org.mockito.Mockito;

import aensina.servicos.CalculadoraService;

public class CalculadoraMockTest {

    @Test
    public void testeDeMockitoMatchers() {
        CalculadoraService calcService = Mockito.mock(CalculadoraService.class);

        Mockito.when(calcService.somar(1, 2)).thenReturn(5);

        // Sempre que o Mockito receber parametros que ele não espera, ele retornará o valor default do tipo primitivo, no caso abaixo: zero.
        System.out.println(calcService.somar(1, 8));

        // No caso abaixo, você pode restringir os parâmetros para que sempre que o primeiro elemento for 5 e subtraido de qualquer inteiro, retorne 2
        Mockito.when(calcService.subtrair(Mockito.eq(5), Mockito.anyInt())).thenReturn(2);
        System.out.println(calcService.subtrair(5, 8));

    }

}
