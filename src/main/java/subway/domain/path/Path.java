package subway.domain.path;

import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public interface Path {

    ShortestPath registerSections(List<Sections> sections);

    public List<Station> path(final Station source, final Station target);

    public double distance(final Station source, final Station target);
}
