package subway.domain.section;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Station;
import subway.exception.CanNotSplitSectionByNextStationException;
import subway.exception.CanNotSplitSectionByPrevStationException;
import subway.exception.DistanceValueValidateException;
import subway.exception.SectionHasSameStationsException;
import subway.exception.SplitSectionIsSmallerThanSplitterException;

public class Section {

    private final Long id;
    private final Station prevStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(final Long id, final Station prevStation, final Station nextStation, final Distance distance) {
        validateSameStation(prevStation, nextStation);
        this.id = id;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    private void validateSameStation(final Station prevStation, final Station nextStation) {
        if (prevStation.equals(nextStation)) {
            throw new SectionHasSameStationsException();
        }
    }

    public Section(final Station prevStation, final Station nextStation, final Distance distance) {
        this(null, prevStation, nextStation, distance);
    }

    public boolean isEqualPrevStation(final Station station) {
        return station.equals(prevStation);
    }

    public boolean isEqualNextStation(final Station station) {
        return station.equals(nextStation);
    }

    public Long getId() {
        return id;
    }

    public Station getPrevStation() {
        return prevStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean containStation(final Station station) {
        return isEqualPrevStation(station) || isEqualNextStation(station);
    }

    public Section concatSection(final Section nextSection) {
        return new Section(prevStation, nextSection.nextStation, distance.plusValue(nextSection.distance));
    }

    public List<Section> splitByPrev(final Section section) {
        if (!section.isEqualPrevStation(prevStation)) {
            throw new CanNotSplitSectionByPrevStationException();
        }
        try {
            return List.of(
                    section,
                    new Section(section.nextStation, nextStation, distance.minusValue(section.distance))
            );
        } catch (final DistanceValueValidateException exception) {
            throw new SplitSectionIsSmallerThanSplitterException();
        }
    }

    public List<Section> splitByNext(final Section section) {
        if (!section.isEqualNextStation(nextStation)) {
            throw new CanNotSplitSectionByNextStationException();
        }
        try {
            return List.of(
                    new Section(prevStation, section.prevStation, distance.minusValue(section.distance))
                    , section
            );
        } catch (final DistanceValueValidateException exception) {
            throw new SplitSectionIsSmallerThanSplitterException();
        }
    }
}
