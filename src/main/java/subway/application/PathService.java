package subway.application;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;

import java.util.List;

public interface PathService {

    List<Section> getSectionsByShortestPath(final Station sourceStation,
                                            final Station targetStation,
                                            final List<Line> lines);

    List<Station> getStationsByShortestPath(final Station sourceStation,
                                            final Station targetStation,
                                            final List<Line> lines);

    Distance getDistanceByShortestPath(final Station sourceStation,
                                       final Station targetStation,
                                       final List<Line> lines);
}
