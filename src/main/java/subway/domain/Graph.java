package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Set;

public interface Graph {

    void createInitialSection(Section section);

    boolean isDownLastStation(Station station);

    boolean isUpFirstStation(Station station);

    void addStation(Station station);

    DefaultWeightedEdge addSection(Station upStation, Station downStation);

    void setSectionDistance(DefaultWeightedEdge section, int distance);

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
}
