package subway.domain.graph;

import java.util.List;
import subway.domain.section.Section;
import subway.domain.station.Station;

public interface SubwayGraph {

    List<Station> getPath(final List<Section> sections, final Station source, final Station destination);

    int getWeight(final List<Section> sections, final Station source, final Station destination);
}
