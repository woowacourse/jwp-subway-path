package subway.domain;

import java.util.Objects;
import subway.exception.station.DuplicateStationNameException;

public class Section {
    private final Station startStation;
    private final Station endStation;
    private final Distance distance;

    public Section(final Builder builder) {
        this(builder.startStation, builder.endStation, builder.distance);
    }

    public Section(final Station startStation, final Station endStation, final Distance distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Section section) {
        return new Builder(section);
    }

    public static class Builder {
        private Station startStation;
        private Station endStation;
        private Distance distance;

        private Builder() {
        }

        private Builder(Section section) {
            this.startStation = section.startStation;
            this.endStation = section.endStation;
            this.distance = section.distance;
        }

        public Builder startStation(Station startStation) {
            this.startStation = startStation;
            return this;
        }

        public Builder endStation(Station endStation) {
            this.endStation = endStation;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            validate();

            return new Section(this);
        }

        private void validate() {
            if (startStation == null || endStation == null || distance == null) {
                throw new IllegalStateException("필수 필드가 설정되지 않았습니다.");
            }

            if (startStation.equals(endStation)) {
                throw new DuplicateStationNameException("시작 역과 도착 역은 같을 수 없습니다.");
            }
        }
    }

    public boolean isSameStartStation(Section otherSection) {
        return this.isSameStartStation(otherSection.startStation);
    }

    public boolean isSameStartStation(Station otherStation) {
        return this.startStation.equals(otherStation);
    }

    public boolean isSameEndStation(Section otherSection) {
        return this.isSameEndStation(otherSection.endStation);
    }

    public boolean isSameEndStation(Station otherStation) {
        return this.endStation.equals(otherStation);
    }

    public Distance subtractDistance(Section otherSection) {
        return this.distance.subtract(otherSection.distance);
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean isGreaterThanOtherDistance(Section otherSection) {
        return this.distance.isBiggerThanOtherDistance(otherSection.distance);
    }

    public boolean hasStation(Station otherStation) {
        return isSameStartStation(otherStation) || isSameEndStation(otherStation);
    }

    @Override
    public boolean equals(Object o) {
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
