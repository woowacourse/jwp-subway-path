package subway.domain;

import java.util.List;
import java.util.Objects;

public class SectionDomain {

    private final Long id;
    private final Distance distance;
    private final boolean isStart;
    private final StationDomain upStation;
    private final StationDomain downStation;

    public SectionDomain(
            final Distance distance,
            final boolean isStart,
            final StationDomain upStation,
            final StationDomain downStation
    ) {
        this(null, distance, isStart, upStation, downStation);
    }

    public SectionDomain(
            final Long id,
            final Distance distance,
            final boolean isStart,
            final StationDomain upStation,
            final StationDomain downStation
    ) {
        validate(upStation, downStation, distance);
        this.id = id;
        this.distance = distance;
        this.isStart = isStart;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    private void validate(final StationDomain upStation, final StationDomain downStation, final Distance distance) {
        if (Objects.isNull(upStation)) {
            throw new IllegalArgumentException("상행역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(downStation)) {
            throw new IllegalArgumentException("하행역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("거리는 null 일 수 없습니다.");
        }
    }

    public List<SectionDomain> separateBy(final SectionDomain newSection) {
        if (isSameUpStationBy(newSection.upStation)) {
            return createInnerSectionBaseIsUpStation(newSection);
        }
        if (isSameDownStationBy(newSection.downStation)) {
            return createInnerSectionBaseIsDownStation(newSection);
        }
        if (isSameUpStationBy(newSection.downStation)) {
            return createOuterSectionBaseIsUpStation(newSection);
        }
        return createOuterSectionBaseIsDownStation(newSection);
    }

    private List<SectionDomain> createInnerSectionBaseIsUpStation(final SectionDomain newSection) {
        final Distance oldUpToNewDownDistance = newSection.distance;
        final Distance oldDownToNewUpDistance = this.distance.minus(oldUpToNewDownDistance);

        final SectionDomain oldUpToNewDownSection
                = new SectionDomain(oldUpToNewDownDistance, this.isStart, this.upStation, newSection.downStation);
        final SectionDomain oldDownToNewUpSection
                = new SectionDomain(oldDownToNewUpDistance, false, newSection.downStation, this.downStation);

        return List.of(oldUpToNewDownSection, oldDownToNewUpSection);
    }

    private List<SectionDomain> createInnerSectionBaseIsDownStation(final SectionDomain newSection) {
        final Distance oldDownToNewUpDistance = newSection.distance;
        final Distance oldUpToNewDownDistance = this.distance.minus(oldDownToNewUpDistance);

        final SectionDomain oldUpToNewDownSection
                = new SectionDomain(oldUpToNewDownDistance, this.isStart, this.upStation, newSection.upStation);
        final SectionDomain oldDownToNewUpSection
                = new SectionDomain(oldDownToNewUpDistance, false, newSection.upStation, this.downStation);

        return List.of(oldUpToNewDownSection, oldDownToNewUpSection);
    }

    private List<SectionDomain> createOuterSectionBaseIsUpStation(final SectionDomain newSection) {
        if (this.isStart) {
            return List.of(newSection.toStart(), this.toNotStart());
        }
        return List.of(newSection.toNotStart(), this.toNotStart());
    }

    private SectionDomain toNotStart() {
        return new SectionDomain(this.id, this.distance, false, this.upStation, this.downStation);
    }

    private SectionDomain toStart() {
        return new SectionDomain(this.id, this.distance, true, this.upStation, this.downStation);
    }

    private List<SectionDomain> createOuterSectionBaseIsDownStation(final SectionDomain newSection) {
        return List.of(this, newSection.toNotStart());
    }

    public boolean isBaseStationExist(final SectionDomain otherSection) {
        return upStation.equals(otherSection.upStation)
                || upStation.equals(otherSection.downStation)
                || downStation.equals(otherSection.upStation)
                || downStation.equals(otherSection.downStation);
    }

    public boolean isSameUpStationBy(final StationDomain otherStation) {
        return Objects.equals(upStation, otherStation);
    }

    public boolean isSameDownStationBy(final StationDomain otherStation) {
        return Objects.equals(downStation, otherStation);
    }

    public Long getId() {
        return id;
    }

    public StationDomain getUpStation() {
        return upStation;
    }

    public StationDomain getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean getStart() {
        return isStart;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionDomain that = (SectionDomain) o;
        return Objects.equals(distance, that.distance)
                && Objects.equals(upStation, that.upStation)
                && Objects.equals(downStation, that.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, upStation, downStation);
    }
}
