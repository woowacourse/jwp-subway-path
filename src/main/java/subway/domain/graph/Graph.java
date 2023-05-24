package subway.domain.graph;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Direction;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
import java.util.Set;

public interface Graph {

    void addStation(Station station);

    DefaultWeightedEdge addSection(Station upStation, Station downStation, int distance);

    boolean containsStation(Station station);

    Set<DefaultWeightedEdge> downStationsOf(Station station);

    Set<DefaultWeightedEdge> upStationsOf(Station station);

    Set<Station> stationSet();

    Station getUpStation(DefaultWeightedEdge edge);

    Station getDownStation(DefaultWeightedEdge edge);

    double getSectionDistance(DefaultWeightedEdge edge);

    DefaultWeightedEdge getSection(Station upStation, Station downStation);

    void removeSection(Station upStation, Station downStation);

    void removeSection(DefaultWeightedEdge edge);

    Set<DefaultWeightedEdge> sectionsOf(Station station);

    void removeStation(Station station);

    void removeAllSections(Set<DefaultWeightedEdge> edges);

    boolean isTerminal(Direction direction, Station station);

    List<Section> getSections();

    DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> getGraph();

    List<Station> getStations();
}
