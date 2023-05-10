package subway.dto;

import java.util.Objects;
import subway.entity.Station;

public class StationSaveRequest {

    private String name;

    public StationSaveRequest() {
    }

    public StationSaveRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toEntity() {
        return Station.of(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationSaveRequest request = (StationSaveRequest) o;
        return Objects.equals(name, request.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
