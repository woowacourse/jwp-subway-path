package subway.domain;

import static subway.domain.Direction.DOWN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class LineRoute {

    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations;

    private LineRoute(final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        this.stations = stations;
    }

    public static LineRoute of(final List<Section> sections) {
        return new LineRoute(RoutedStationsFactory.create(sections));
    }

    public void add(final Station base,
                    final Station adding,
                    final Distance distance,
                    final Direction direction) {
        updateVertexForStations(base, adding);

        Section addingSection = Section.createByDirection(base, adding, distance, direction);
        Optional<Section> existingSection = findExistingSectionByDirection(base, direction);

        if (existingSection.isEmpty()) {
            updateSimpleAddingEdgeForStations(addingSection);
            return;
        }
        updateIntersectionEdgesForStations(existingSection.get(), addingSection);
    }

    private void updateVertexForStations(final Station base, final Station adding) {
        if (Objects.equals(base, adding)) {
            throw new IllegalArgumentException("기준 역과 등록할 역은 동일할 수 없습니다.");
        }
        if (isStationsEmpty()) {
            stations.addVertex(base);
        }
        validateExisting(base);
        validateNonExisting(adding);
        stations.addVertex(adding);
    }

    private void validateNonExisting(final Station station) {
        if (hasStation(station)) {
            throw new IllegalArgumentException(station.getName() + ": 해당 역이 노선에 이미 존재합니다.");
        }
    }

    private void validateExisting(final Station station) {
        if (!hasStation(station)) {
            throw new IllegalArgumentException(station.getName() + ": 해당 역이 노선에 존재하지 않습니다.");
        }
    }

    private boolean hasStation(final Station station) {
        return stations.containsVertex(station);
    }

    private Optional<Section> findExistingSectionByDirection(final Station base, final Direction direction) {
        if (direction == DOWN) {
            return findRightSection(base);
        }
        return findLeftSection(base);
    }

    private Optional<Section> findRightSection(Station station) {
        try {
            DefaultWeightedEdge edge = stations.outgoingEdgesOf(station).iterator().next();
            return Optional.of(
                    new Section(station, stations.getEdgeTarget(edge), new Distance((int) stations.getEdgeWeight(edge)))
            );
        } catch (NoSuchElementException exception) {
            return Optional.empty();
        }
    }

    private Optional<Section> findLeftSection(Station station) {
        try {
            DefaultWeightedEdge edge = stations.incomingEdgesOf(station).iterator().next();
            return Optional.of(
                    new Section(stations.getEdgeSource(edge), station, new Distance((int) stations.getEdgeWeight(edge)))
            );
        } catch (NoSuchElementException exception) {
            return Optional.empty();
        }
    }

    private void updateSimpleAddingEdgeForStations(final Section addingSection) {
        stations.setEdgeWeight(stations.addEdge(addingSection.getLeft(), addingSection.getRight()),
                addingSection.getDistance().getValue());
    }

    private void updateIntersectionEdgesForStations(final Section currentSection, final Section addingSection) {
        // TODO 예외 메시지 작성
        Section subtractedFromCurrent = currentSection.subtract(addingSection)
                .orElseThrow(() -> new IllegalArgumentException(""));
        // TODO Distance 범위 예외 커스텀으로 만든 뒤 여기서 예외 변환, 메시지 적절하게 작성 (기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.)

        stations.removeAllEdges(currentSection.getLeft(), currentSection.getRight());
        stations.setEdgeWeight(stations.addEdge(addingSection.getLeft(), addingSection.getRight()),
                addingSection.getDistance().getValue());
        stations.setEdgeWeight(stations.addEdge(subtractedFromCurrent.getLeft(), subtractedFromCurrent.getRight()),
                subtractedFromCurrent.getDistance().getValue());
    }

    public void delete(final Station station) {
        validateExisting(station);

        Set<Station> allStations = new HashSet<>(stations.vertexSet());
        if (allStations.size() == 2) {
            stations.removeAllVertices(allStations);
            return;
        }

        Optional<Section> leftFound = findLeftSection(station);
        Optional<Section> rightFound = findRightSection(station);

        if (leftFound.isPresent() && rightFound.isPresent()) {
            Section leftSection = leftFound.get();
            Section rightSection = rightFound.get();
            // TODO 예외 메시지 작성
            Section merged = leftSection.merge(rightSection)
                    .orElseThrow(() -> new IllegalArgumentException(""));

            stations.setEdgeWeight(stations.addEdge(merged.getLeft(), merged.getRight()),
                    merged.getDistance().getValue());
        }
        stations.removeVertex(station);
    }

    public List<Section> extractSections() {
        return stations.edgeSet()
                .stream()
                .map(edge -> new Section(stations.getEdgeSource(edge), stations.getEdgeTarget(edge),
                        new Distance((int) stations.getEdgeWeight(edge))))
                .collect(Collectors.toList());
    }

    public List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (isStationsEmpty()) {
            return orderedStations;
        }

        Station station = findStart()
                .orElseThrow(() -> new IllegalArgumentException("하행 종점을 찾을 수 없습니다."));
        while (!stations.outgoingEdgesOf(station).isEmpty()) {
            orderedStations.add(station);
            Section rightSection = findRightSection(station).get();
            station = rightSection.getRight();
        }
        orderedStations.add(station);

        return orderedStations;
    }

    private Optional<Station> findStart() {
        return stations.vertexSet()
                .stream()
                .filter(station -> stations.incomingEdgesOf(station).isEmpty())
                .findFirst();
    }

    private boolean isStationsEmpty() {
        return stations.edgeSet().isEmpty();
    }
}
