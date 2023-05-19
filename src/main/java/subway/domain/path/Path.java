package subway.domain.path;

import subway.domain.Station;

import java.util.List;

public interface Path {

    void addStation(Station station);

    void addSectionEdge(Station sourceStation, Station targetStation, SectionEdge sectionEdge);

    List<SectionEdge> getShortestSectionPath(Station sourceStation, Station targetStation);

    List<Station> getShortestStationPath(Station sourceStation, Station targetStation);
}
