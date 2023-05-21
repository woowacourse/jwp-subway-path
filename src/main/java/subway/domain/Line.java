package subway.domain;

import java.util.Objects;

public class Line {

    private Long id;
    private String name;
    private String color;
    private Integer additionalCharge;

    public Line() {
    }

    public Line(String name, String color, Integer additionalCharge) {
        this.name = name;
        this.color = color;
        this.additionalCharge = additionalCharge;
    }

    public Line(Long id, String name, String color, Integer additionalCharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalCharge = additionalCharge;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getAdditionalCharge() {
        return additionalCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Line line = (Line)o;

        if (!Objects.equals(id, line.id))
            return false;
        if (!Objects.equals(name, line.name))
            return false;
        if (!Objects.equals(color, line.color))
            return false;
        return Objects.equals(additionalCharge, line.additionalCharge);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (additionalCharge != null ? additionalCharge.hashCode() : 0);
        return result;
    }
}
