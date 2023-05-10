package subway.domain;

import java.util.Objects;
import subway.exception.GlobalException;

public class Section {
    private final Station startStation;
    private final Station endStation;
    private final Distance distance;

    public Section(final Station startStation, final Station endStation, final Distance distance) {
        validate(startStation, endStation);

        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private void validate(final Station startStation, final Station endStation) {
        if (startStation.equals(endStation)) {
            throw new GlobalException("시작 역과 도착 역은 같을 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(startStation, section.startStation) && Objects.equals(endStation,
                section.endStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startStation, endStation);
    }
}
