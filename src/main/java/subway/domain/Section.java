package subway.domain;

import static subway.domain.Direction.INNER_RIGHT;
import static subway.domain.Direction.NONE;
import static subway.domain.Direction.INNER_LEFT;

public class Section {
    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;
    private final Long nextSectionId;

    public Section(final Long id, final Station upStation, final Station downStation, final int distance,
                   final Long nextSectionId) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.nextSectionId = nextSectionId;
    }

    public Section(final long id, final Station upStation, final Station downStation, final int distance) {
        this(id, upStation, downStation, distance, null);
    }

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(null, upStation, downStation, distance, null);
    }

    public Section(final Station upStation, final Station downStation, final int distance, final long nextSectionId) {
        this(null, upStation, downStation, distance, nextSectionId);
    }

    public boolean isNextContinuable(Section newSection) {
        return this.downStation.equals(newSection.upStation);
    }

    public boolean isPreviousContinuable(Section newSection) {
        return this.upStation.equals(newSection.downStation);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public Direction checkDirection(Section section) {
        if (this.upStation.equals(section.upStation)) {
            return INNER_LEFT;
        }

        if (this.downStation.equals(section.downStation)) {
            return INNER_RIGHT;
        }

        return NONE;
    }

    public boolean hasIntersection(Section section) {
        boolean isUpSame = this.upStation.equals(section.upStation);
        boolean isDownSame = this.downStation.equals(section.downStation);
        return isUpSame != isDownSame;
    }

    public int getDistance() {
        return distance;
    }

    public Long getNextSectionId() {
        return nextSectionId;
    }

    public Long getId() {
        return id;
    }

    public long getUpStationId() {
        return upStation.getId();
    }

    public long getDownStationId() {
        return downStation.getId();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
