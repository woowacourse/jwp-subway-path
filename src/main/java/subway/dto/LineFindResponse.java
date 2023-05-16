package subway.dto;

import java.util.List;
import java.util.Objects;

public class LineFindResponse {

    private final String lineName;
    private final List<String> stationNames;

    public LineFindResponse(String lineName, List<String> stationNames) {
        this.lineName = lineName;
        this.stationNames = stationNames;
    }

    public String getLineName() {
        return lineName;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineFindResponse that = (LineFindResponse) o;
        return Objects.equals(lineName, that.lineName) && Objects.equals(stationNames, that.stationNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName, stationNames);
    }

    @Override
    public String toString() {
        return "LineFindResponse{" +
                "lineName='" + lineName + '\'' +
                ", stationNames=" + stationNames +
                '}';
    }
}
