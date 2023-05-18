package subway.domain.section;

import subway.domain.station.Station;

import java.util.List;

public interface DirectionStrategy {

    Sections calculate(final List<Section> sections, final Station existStation, final Station newStation, final Distance distance);
}
