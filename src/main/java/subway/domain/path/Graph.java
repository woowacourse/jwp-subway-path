package subway.domain.path;

import org.springframework.stereotype.Component;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

@Component
public interface Graph {
    Path findPath(List<Station> stations, List<Section> sections, String start, String end);
}
