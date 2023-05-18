package subway.domain;

import subway.exception.InvalidSectionConnectException;
import subway.exception.InvalidSectionLengthException;

import java.util.UUID;

public class Section {
    private final UUID id;
    private Station upStation;
    private Station downStation;
    private final int distance;
    private UUID nextSectionId;

    public Section(final UUID id, final Station upStation, final Station downStation, final int distance,
                   final UUID nextSectionId) {
        validateDistance(distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.nextSectionId = nextSectionId;
    }

    private void validateDistance(int distance) {
        if (distance < 1) {
            throw new InvalidSectionLengthException();
        }
    }

    public Section(final UUID id, final Station upStation, final Station downStation, final int distance) {
        this(id, upStation, downStation, distance, null);
    }

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(UUID.randomUUID(), upStation, downStation, distance, null);
    }

    public Section(final Station upStation, final Station downStation, final int distance, final UUID nextSectionId) {
        this(UUID.randomUUID(), upStation, downStation, distance, nextSectionId);
    }

    public Section updateDuplicateStation(Section requestedSection) {
        if (this.isNextContinuable(requestedSection)) {
            this.downStation = requestedSection.upStation;
            return new Section(this.upStation, this.downStation, this.distance);
        }

        if (this.isPreviousContinuable(requestedSection)) {
            this.upStation = requestedSection.downStation;
            return new Section(this.upStation, this.downStation, this.distance);
        }

        if (this.isSameDownStationId(requestedSection)) {
            this.downStation = requestedSection.upStation;
            return new Section(this.upStation, this.downStation, this.distance);
        }

        if (this.isSameUpStationId(requestedSection)) {
            this.upStation = requestedSection.downStation;
            return new Section(this.upStation, this.downStation, this.distance);
        }
        throw new InvalidSectionConnectException();
    }

    public boolean isNextContinuable(Section newSection) {
        return this.downStation.equals(newSection.upStation);
    }

    public boolean isPreviousContinuable(Section newSection) {
        return this.upStation.equals(newSection.downStation);
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isSameDownStationId(Section other) {
        return this.downStation.equals(other.downStation);
    }

    public boolean isSameUpStationId(Section other) {
        return this.upStation.equals(other.upStation);
    }

    public boolean hasIntersection(Section section) {
        boolean isUpSame = this.upStation.equals(section.upStation);
        boolean isDownSame = this.downStation.equals(section.downStation);
        return isUpSame != isDownSame;
    }

    public boolean isDistanceSmallOrSame(final Section section) {
        return this.distance <= section.distance;
    }

    public boolean containsStation(Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public void setNextSectionId(UUID id) {
        this.nextSectionId = id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public UUID getNextSectionId() {
        return nextSectionId;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                ", nextSectionId=" + nextSectionId +
                '}';
    }

}
