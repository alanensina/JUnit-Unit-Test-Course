package matchers;

import java.util.Calendar;

public class MatchersProperties {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiEmUmaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataComDiferencaDeDias ehHojeComDiferencaDeDias(Integer qtdDias) {
        return new DataComDiferencaDeDias(qtdDias);
    }

    public static DataComDiferencaDeDias ehHoje() {
        return new DataComDiferencaDeDias(0);
    }
}
