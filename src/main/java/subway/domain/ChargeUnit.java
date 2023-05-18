package subway.domain;

import subway.exception.IllegalChargeUnitException;

public class ChargeUnit {
    private final int value;

    public ChargeUnit(int value) {
        if(value <= 0) {
            throw new IllegalChargeUnitException();
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
