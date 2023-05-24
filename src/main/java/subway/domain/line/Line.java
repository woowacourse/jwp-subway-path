package subway.domain.line;

import java.util.Objects;
import subway.domain.price.Price;

public class Line {
    private final LineName name;
    private final LineColor color;
    private final Price extraFee;

    public Line(LineName name, LineColor color, Price extraFee) {
        this.name = name;
        this.color = color;
        this.extraFee = extraFee;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public long getExtraFee() {
        return extraFee.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }

        Line line = (Line) o;

        if (!Objects.equals(name, line.name)) {
            return false;
        }
        if (!Objects.equals(color, line.color)) {
            return false;
        }
        return Objects.equals(extraFee, line.extraFee);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (extraFee != null ? extraFee.hashCode() : 0);
        return result;
    }
}
