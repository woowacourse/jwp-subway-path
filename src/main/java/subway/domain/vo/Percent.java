package subway.domain.vo;

import java.math.BigDecimal;

public class Percent {

    public static final Percent PERCENT_100 = new Percent(BigDecimal.valueOf(100));
    public static final Percent PERCENT_50 = new Percent(BigDecimal.valueOf(50));
    public static final Percent PERCENT_20 = new Percent(BigDecimal.valueOf(20));
    public static final Percent PERCENT_0 = new Percent(BigDecimal.valueOf(0));

    private final BigDecimal value;

    private Percent(final BigDecimal value) {
        this.value = value;
    }

    public static Percent from(final String value) {
        return new Percent(new BigDecimal(value));
    }

    public BigDecimal getPercent() {
        return value.divide(BigDecimal.valueOf(100));
    }
}
