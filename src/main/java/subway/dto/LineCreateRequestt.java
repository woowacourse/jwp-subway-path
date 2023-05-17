package subway.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class LineCreateRequestt {
    @NotBlank
    private String lineName;

    public LineCreateRequestt(String lineName) {
        this.lineName = lineName;
    }

    private LineCreateRequestt() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineCreateRequestt that = (LineCreateRequestt) o;
        return Objects.equals(lineName, that.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName);
    }

    public String getLineName() {
        return lineName;
    }
}
