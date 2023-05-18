package subway.domain.path;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.transfer.TransferSection;
import subway.domain.station.Station;

public class ShortestPathFinder {

    private final WeightedMultigraph<Station, Section> pathFinder = new WeightedMultigraph<>(Section.class);

    public ShortestPathFinder(final List<Station> stations, final List<GeneralSection> generalSections,
                              final List<TransferSection> transferSections) {
        initVertex(stations);
        initEdge(generalSections, transferSections);
    }

    private void initVertex(List<Station> stations) {
        for (Station station : stations) {
            pathFinder.addVertex(station);
        }
    }

    private void initEdge(List<GeneralSection> generalSections, List<TransferSection> transferSections) {
        initGeneralSectionsEdge(generalSections);
        initTransferSectionsEdge(transferSections);
    }

    private void initGeneralSectionsEdge(List<GeneralSection> generalSections) {
        for (GeneralSection generalSection : generalSections) {
            Station upStation = generalSection.getUpStation();
            Station downStation = generalSection.getDownStation();
            int distance = generalSection.getDistance();

            pathFinder.addEdge(upStation, downStation, generalSection);
            pathFinder.setEdgeWeight(generalSection, distance);
        }
    }

    private void initTransferSectionsEdge(List<TransferSection> transferSections) {
        for (TransferSection transferSection : transferSections) {
            Station upStation = transferSection.getUpStation();
            Station downStation = transferSection.getDownStation();
            int distance = transferSection.getDistance();

            pathFinder.addEdge(upStation, downStation, transferSection);
            pathFinder.setEdgeWeight(transferSection, distance);
        }
    }

    public PathSections findShortestPathSections(Station startStation, Station endStation) {
        DijkstraShortestPath<Station, Section> shortestPathGenerator = new DijkstraShortestPath<>(pathFinder);
        GraphPath<Station, Section> shortestPath = shortestPathGenerator.getPath(startStation, endStation);
        List<Section> orderedSections = shortestPath.getEdgeList();
        return new PathSections(orderedSections);
    }
}
