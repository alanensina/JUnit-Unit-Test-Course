package suites;

import org.junit.runners.Suite.SuiteClasses;

import aensina.CalculadoraServiceTest;
import aensina.CalculoValorLocacaoTest;
import aensina.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
        CalculadoraServiceTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {

}
