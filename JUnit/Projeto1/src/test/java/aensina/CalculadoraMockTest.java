package aensina;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import aensina.interfaces.EmailService;
import aensina.servicos.CalculadoraService;

public class CalculadoraMockTest {

    @Mock
    private CalculadoraService calcMock;

    @Spy
    private CalculadoraService calcSpy;

    // @Spy - Só funciona para classes concretas e não para interfaces
    @Mock
    private EmailService emailSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deveMostrarADiferencaEntreMockESpy() {
        System.out.println("Caso em que ele sabe o esperado:");
        Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        System.out.println("Mock: " + calcMock.somar(1, 2));
        // Mockito.when(calcSpy.somar(1, 2)).thenReturn(8); // O sysout dentro do método somar é executado no momento da gravação da expectativa
        Mockito.doReturn(8).when(calcSpy).somar(1, 2); // Nesse caso o sysout nao é executado
        System.out.println("Spy: " + calcSpy.somar(1, 2));

        System.out.println("Caso em que ele não sabe o esperado:");
        System.out.println("Mock: " + calcMock.somar(2, 2)); // retorna o valor padrão que é zero
        System.out.println("Spy: " + calcSpy.somar(2, 2)); // retorna o valor real da função

        System.out.println("Mock: ");
        calcMock.imprime();

        System.out.println("Spy: ");
        calcSpy.imprime();

        Mockito.doNothing().when(calcSpy).imprime();
        System.out.println("Spy: ");
        calcSpy.imprime();

    }

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
