package subway.dto;

import java.util.Objects;

public class LineRequest {

    private final String lineName;
    private final int surcharge;

    public LineRequest(String lineName, int surcharge) {
        this.lineName = lineName;
        this.surcharge = surcharge;
    }

    public String getLineName() {
        return lineName;
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
        LineRequest request = (LineRequest) o;
        return surcharge == request.surcharge && Objects.equals(lineName, request.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName, surcharge);
    }

    @Override
    public String toString() {
        return "LineRequest{" +
                "lineName='" + lineName + '\'' +
                ", surcharge=" + surcharge +
                '}';
    }
}
