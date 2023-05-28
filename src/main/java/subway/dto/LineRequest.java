package subway.dto;

import java.util.Objects;

public class LineRequest {

    private String lineName;

    public LineRequest() {
    }

    public LineRequest(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineRequest that = (LineRequest) o;
        return Objects.equals(lineName, that.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName);
    }
}
