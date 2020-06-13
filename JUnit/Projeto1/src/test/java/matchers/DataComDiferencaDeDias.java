package matchers;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import aensina.utils.DataUtils;

public class DataComDiferencaDeDias extends TypeSafeMatcher<Date> {
    private Integer qtdDias;

    public DataComDiferencaDeDias(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }

    public void describeTo(Description description) {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdDias));
    }

    public Integer getQtdDias() {
        ;
        return qtdDias;
    }

    public void setQtdDias(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }
}
