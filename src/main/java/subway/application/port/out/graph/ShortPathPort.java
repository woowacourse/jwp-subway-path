package subway.application.port.out.graph;

import subway.domain.Route;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public interface ShortPathPort {
    Route findSortPath(final Station fromStation, final Station toStation, final List<Sections> allSections);
}
