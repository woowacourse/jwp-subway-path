package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Long lineId;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(final Long id, final Section other) {
        this(id, other.lineId, other.upStation, other.downStation, other.distance);
    }

    private Section(final Long id, final Long lineId, final Station upStation, final Station downStation, final Distance distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }

    public static class Builder {

        private Long id;
        private Long lineId;
        private Station upStation;
        private Station downStation;
        private Distance distance;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder lineId(final Long lindId) {
            this.lineId = lindId;
            return this;
        }

        public Builder upStation(final Long upStationId) {
            upStation = new Station(upStationId);
            return this;
        }

        public Builder upStation(final Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(final Long downStationId) {
            downStation = new Station(downStationId);
            return this;
        }

        public Builder downStation(final Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(final int distance) {
            this.distance = new Distance(distance);
            return this;
        }

        public Builder distance(final Distance distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(id, lineId, upStation, downStation, distance);
        }
    }
}
