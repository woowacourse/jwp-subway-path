package subway.domain.vo;

import java.util.Objects;
import subway.exception.vo.ChargeException;

public class Charge {
    private final double value;

    public Charge(double value) {
        validate(value);
        this.value = value;
    }

    public Charge add(Charge other) {
        return new Charge(this.value + other.value);
    }

    public Charge substract(Charge other) {
        return new Charge(this.value - other.value);
    }

    public Charge multiply(double rate) {
        return new Charge(this.value * rate);
    }

    private void validate(double charge) {
        if (charge < 0) {
            throw new ChargeException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Charge charge1 = (Charge) o;
        return Double.compare(charge1.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public double getValue() {
        return value;
    }
}
