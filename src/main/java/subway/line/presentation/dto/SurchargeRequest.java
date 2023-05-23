package subway.line.presentation.dto;

import java.math.BigDecimal;

public class SurchargeRequest {
    private final BigDecimal surcharge;

    public SurchargeRequest(BigDecimal surcharge) {
        this.surcharge = surcharge;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }
}
