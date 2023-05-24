package subway.domain;

import static subway.domain.vo.Direction.DOWN;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.EmptySectionOperationException;
import subway.domain.exception.IllegalDistanceArgumentException;
import subway.domain.exception.IllegalLineMapArgumentException;
import subway.domain.vo.Direction;
import subway.domain.vo.Distance;

public class LineMap {

    private final RoutedStations routedStations;

    private LineMap(final RoutedStations routedStations) {
        this.routedStations = routedStations;
    }

    public static LineMap of(final List<Section> sections) {
        return new LineMap(RoutedStations.from(sections));
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
        updateOverlappingEdgesForStations(existingSection.get(), addingSection);
    }

    private void updateVertexForStations(final Station base, final Station adding) {
        if (Objects.equals(base, adding)) {
            throw new IllegalLineMapArgumentException("기준 역과 등록할 역은 동일할 수 없습니다.");
        }
        if (routedStations.isEmpty()) {
            routedStations.addVertex(base);
        }
        validateExisting(base);
        validateNonExisting(adding);
        routedStations.addVertex(adding);
    }

    private void validateNonExisting(final Station station) {
        if (hasStation(station)) {
            throw new IllegalLineMapArgumentException(station.getName() + ": 해당 역이 노선에 이미 존재합니다.");
        }
    }

    private void validateExisting(final Station station) {
        if (!hasStation(station)) {
            throw new IllegalLineMapArgumentException(station.getName() + ": 해당 역이 노선에 존재하지 않습니다.");
        }
    }

    private boolean hasStation(final Station station) {
        return routedStations.containsVertex(station);
    }

    private Optional<Section> findExistingSectionByDirection(final Station base, final Direction direction) {
        if (direction == DOWN) {
            return routedStations.findRightSection(base);
        }
        return routedStations.findLeftSection(base);
    }

    private void updateSimpleAddingEdgeForStations(final Section addingSection) {
        routedStations.setEdgeWeight(routedStations.addEdge(addingSection.getLeft(), addingSection.getRight()),
                addingSection.getDistance().getValue());
    }

    private void updateOverlappingEdgesForStations(final Section currentSection, final Section addingSection) {
        Section subtractedFromCurrent = subtractAddingFromCurrent(currentSection, addingSection);

        routedStations.removeAllEdges(currentSection.getLeft(), currentSection.getRight());
        routedStations.setEdgeWeight(routedStations.addEdge(addingSection.getLeft(), addingSection.getRight()),
                addingSection.getDistance().getValue());
        routedStations.setEdgeWeight(
                routedStations.addEdge(subtractedFromCurrent.getLeft(), subtractedFromCurrent.getRight()),
                subtractedFromCurrent.getDistance().getValue());
    }

    private Section subtractAddingFromCurrent(final Section currentSection, final Section addingSection) {
        try {
            return currentSection.subtract(addingSection)
                    .orElseThrow(() -> new EmptySectionOperationException("겹치는 영역이 없는 구간의 뺀 값을 구할 수 없습니다."));
        } catch (IllegalDistanceArgumentException exception) {
            throw new IllegalDistanceArgumentException("기존 역 간 거리보다 크거나 같은 거리에 위치하는 새 역을 등록할 수 없습니다.");
        }
    }

    public void delete(final Station station) {
        validateExisting(station);

        Set<Station> allStations = new HashSet<>(routedStations.vertexSet());
        if (allStations.size() == 2) {
            routedStations.removeAllVertices(allStations);
            return;
        }

        Optional<Section> leftFound = routedStations.findLeftSection(station);
        Optional<Section> rightFound = routedStations.findRightSection(station);

        if (leftFound.isPresent() && rightFound.isPresent()) {
            Section leftSection = leftFound.get();
            Section rightSection = rightFound.get();
            Section merged = leftSection.merge(rightSection)
                    .orElseThrow(() -> new EmptySectionOperationException("교차점이 없는 두 구간의 합친 값을 구할 수 없습니다."));

            routedStations.setEdgeWeight(routedStations.addEdge(merged.getLeft(), merged.getRight()),
                    merged.getDistance().getValue());
        }
        routedStations.removeVertex(station);
    }

    public List<Section> extractSections() {
        return routedStations.extractSections();
    }

    public List<Station> getOrderedStations() {
        return routedStations.extractOrderedStations();
    }
}
