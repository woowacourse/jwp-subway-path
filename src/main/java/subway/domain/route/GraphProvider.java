package subway.domain.route;

import java.util.List;
import subway.domain.section.SubwayLine;
import subway.domain.station.Station;

public interface GraphProvider {

    List<Station> getShortestPath(final List<SubwayLine> sections, final Station sourceStation,
                                  final Station targetStation);
}
