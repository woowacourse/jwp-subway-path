package subway.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class LineCreateRequest {
    @NotBlank
    private String lineName;

    public LineCreateRequest(String lineName) {
        this.lineName = lineName;
    }

    private LineCreateRequest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineCreateRequest that = (LineCreateRequest) o;
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
