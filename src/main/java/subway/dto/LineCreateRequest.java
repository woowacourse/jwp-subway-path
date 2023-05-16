package subway.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class LineCreateRequest {
    @NotBlank
    private final String lineName;

    @NotBlank
    private final String upLineStationName;

    @NotBlank
    private final String downLineStationName;

    @NotNull
    @Range(min = 1, max = 30)
    private final Integer distance;

    public LineCreateRequest(String lineName, String upLineStationName, String downLineStationName, Integer distance) {
        this.lineName = lineName;
        this.upLineStationName = upLineStationName;
        this.downLineStationName = downLineStationName;
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineCreateRequest that = (LineCreateRequest) o;
        return Objects.equals(lineName, that.lineName) && Objects.equals(upLineStationName, that.upLineStationName) && Objects.equals(downLineStationName, that.downLineStationName) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName, upLineStationName, downLineStationName, distance);
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpLineStationName() {
        return upLineStationName;
    }

    public String getDownLineStationName() {
        return downLineStationName;
    }

    public Integer getDistance() {
        return distance;
    }

}
