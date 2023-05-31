package subway.domain.line;

import subway.exception.IllegalSurchargeException;

import java.util.Objects;

public class Surcharge {

    private final int surcharge;

    public Surcharge(int surcharge) {
        validate(surcharge);
        this.surcharge = surcharge;
    }

    private void validate(int surcharge) {
        if (surcharge < 0) {
            throw new IllegalSurchargeException();
        }
    }

    public int getSurcharge() {
        return surcharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Surcharge surcharge1 = (Surcharge) o;
        return surcharge == surcharge1.surcharge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(surcharge);
    }

    @Override
    public String toString() {
        return "Surcharge{" +
                "surcharge=" + surcharge +
                '}';
    }
}
