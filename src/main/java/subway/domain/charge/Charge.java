package subway.domain.charge;

import java.util.Objects;

public class Charge {
    private final double charge;

    public Charge(double charge) {
        validate(charge);
        this.charge = charge;
    }

    public Charge add(Charge other) {
        return new Charge(this.charge + other.charge);
    }

    public Charge substract(Charge other) {
        return new Charge(this.charge - other.charge);
    }

    public Charge multiply(double rate) {
        return new Charge(this.charge * rate);
    }

    private void validate(double charge) {
        if (charge < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
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
        return Double.compare(charge1.charge, charge) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(charge);
    }

    public double getValue() {
        return charge;
    }
}
