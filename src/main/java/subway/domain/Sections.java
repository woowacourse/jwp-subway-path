package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class Sections {
    private final Graph graph;
    private final Line line;

    public Sections(final Line line, final Graph graph) {
        this.graph = graph;
        this.line = line;
    }

    public void createInitialSection(Station upStation, Station downStation, int distance) {
        validateDistance(distance);
        final Section section = new Section(upStation, downStation, distance);
        graph.createInitialSection(section);
    }

    public Station addStation(Station upLineStation, Station downLineStation, int distance) {
        validateStations(upLineStation, downLineStation);
        validateDistance(distance);

        // 기존 역: upLineStation, 새로운 역: downLineStation
        if (isNewStation(upLineStation)) {

            // upLineStation[하행종점] -> downLineStation (새 역) -> nothing!
            final boolean isDownLastStation = graph.isDownLastStation(upLineStation);

            if (isDownLastStation) {
                addStationToDownLine(upLineStation, downLineStation, distance);
                return downLineStation;
            }

            // upLineStation -> downLineStation (새 역) -> 기존 다음 역
            final Set<DefaultWeightedEdge> outgoingEdges = graph.downStationsOf(upLineStation);
            addStationToDownLine(upLineStation, downLineStation, findNextStation(outgoingEdges), distance);
            return downLineStation;
        }

        // 기존 역: downLineStation, 새로운 역: upLineStation
        if (isNewStation(downLineStation)) {

            // nothing -> upLineStation (새 역) -> downLineStation
            final boolean isUpFirstStation = graph.isUpFirstStation(downLineStation);

            if (isUpFirstStation) {
                addStationToUpLine(upLineStation, downLineStation, distance);
                return upLineStation;
            }

            // 기존 상행 역 -> upLineStation (새 역) -> downLineStation
            final Set<DefaultWeightedEdge> defaultWeightedEdges = graph.upStationsOf(downLineStation);
            final Station previousStation = findPreviousStation(defaultWeightedEdges);
            addStationToUpLine(previousStation, upLineStation, downLineStation, distance);
            return upLineStation;
        }

        throw new IllegalArgumentException("부적절한 입력입니다.");
    }

    private boolean isNewStation(final Station station) {
        return graph.containsStation(station);
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
    }

    public Station findUpEndStation() {
        return graph.stationSet().stream()
                .filter(vertex -> this.graph.upStationsOf(vertex).isEmpty())
                .findFirst()
                .orElse(null);
    }

    public List<Station> findAllStationsInOrder() {
        List<Station> allStationsInOrder = new ArrayList<>();
        Station startStation = findUpEndStation();
        while (startStation != null) {
            allStationsInOrder.add(startStation);
            Set<DefaultWeightedEdge> outgoingEdges = graph.downStationsOf(startStation);
            if (outgoingEdges.isEmpty()) {
                startStation = null;
            } else {
                startStation = graph.getDownStation(outgoingEdges.iterator().next());
            }
        }
        return allStationsInOrder;
    }

    public Map<List<Station>, Integer> findAllSectionsInOrder() {
        final Map<List<Station>, Integer> sections = new LinkedHashMap<>();

        Station currentStation = findUpEndStation();

        while (currentStation != null) {
            Set<DefaultWeightedEdge> outgoingEdges = graph.downStationsOf(currentStation);
            if (outgoingEdges.isEmpty()) {
                currentStation = null;
            } else {
                final DefaultWeightedEdge edge = outgoingEdges.iterator().next();
                final Station nextStation = graph.getDownStation(edge);
                final int distance = (int) graph.getSectionDistance(edge);
                sections.put(List.of(currentStation, nextStation), distance);
                currentStation = graph.getDownStation(edge);
            }
        }

        return sections;
    }

    public int findDistanceBetween(Station upLineStation, Station downLineStation) {
        final DefaultWeightedEdge edge = graph.getSection(upLineStation, downLineStation);
        return (int) graph.getSectionDistance(edge);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역 사이 거리는 양의 정수로 입력해 주세요.");
        }
    }

    private void validateStations(final Station upLineStation, final Station downLineStation) {
        if (upLineStation.equals(downLineStation)) {
            throw new IllegalArgumentException("서로 다른 역을 입력해 주세요.");
        }
    }

    private Station findPreviousStation(final Set<DefaultWeightedEdge> defaultWeightedEdges) {
        if (!defaultWeightedEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = defaultWeightedEdges.iterator().next();
            return graph.getUpStation(closestEdge);
        }
        throw new IllegalStateException("이전 역이 존재하지 않습니다.");
    }

    private Station findNextStation(final Set<DefaultWeightedEdge> outgoingEdges) {
        if (!outgoingEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = outgoingEdges.iterator().next();
            return graph.getDownStation(closestEdge);
        }
        throw new IllegalStateException("다음 역이 존재하지 않습니다.");
    }

    private void addStationToUpLine(final Station newStation, final Station downLineStation, final int distance) {
        graph.addStation(newStation);
        graph.setSectionDistance(graph.addSection(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final int distance) {
        graph.addStation(newStation);
        graph.setSectionDistance(graph.addSection(upLineStation, newStation), distance);
    }

    private void addStationToUpLine(final Station downLinePreviousStation, final Station newStation, final Station downLineStation, final int distance) {
        graph.addStation(newStation); //
        // downLinePreviousStation ~ downLineStation distance <= distance => 예외 발생
        final DefaultWeightedEdge edge = graph.getSection(downLinePreviousStation, downLineStation);
        final int distanceBetweenDownLinePreviousStationAndDownLineStation = (int) graph.getSectionDistance(edge);
        if (distanceBetweenDownLinePreviousStationAndDownLineStation <= distance) {
            throw new IllegalArgumentException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenDownLinePreviousStationAndNewStation = distanceBetweenDownLinePreviousStationAndDownLineStation - distance;

        // downLinePreviousStation, downLineStation 끊어줌
        graph.removeSection(downLinePreviousStation, downLineStation);

        // downLinePreviousStation, newStation 연결 => distance: distanceBetweenDownLinePreviousStationAndNewStation
        graph.setSectionDistance(graph.addSection(downLinePreviousStation, newStation), distanceBetweenDownLinePreviousStationAndNewStation);

        // newStation, downLineStation 연결 => distance: 입력한 거리
        graph.setSectionDistance(graph.addSection(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final Station upLineNextStation, final int distance) {
        graph.addStation(newStation); //
        // upLineStation ~ upLineNextStation distance <= distance => 예외 발생
        DefaultWeightedEdge edge = graph.getSection(upLineStation, upLineNextStation);
        int distanceBetweenUpLineStationAndUpLineNextStation = (int) graph.getSectionDistance(edge);
        if (distanceBetweenUpLineStationAndUpLineNextStation <= distance) {
            throw new IllegalArgumentException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenNewStationAndUpLineNextStation = distanceBetweenUpLineStationAndUpLineNextStation - distance;

        // upLineStation, upLineNextStation 끊어줌
        graph.removeSection(upLineStation, upLineNextStation);
        // upLineStation, newStation 연결 -> distance: 입력한 거리
        graph.setSectionDistance(graph.addSection(upLineStation, newStation), distance);
        // newStation, upLineNextStation 연결 -> distance: 계산한 거리
        graph.setSectionDistance(graph.addSection(newStation, upLineNextStation), distanceBetweenNewStationAndUpLineNextStation);
    }

    public void deleteStation(Station station) {
        List<DefaultWeightedEdge> adjacentEdges = new ArrayList<>(graph.sectionsOf(station));

        // 양쪽 연결된 경우
        if (adjacentEdges.size() == 2) {
            deleteMiddleStation(station);
        }

        // 한 쪽만 연결된 경우
        if (adjacentEdges.size() == 1) {
            graph.removeSection(adjacentEdges.get(0));
        }

        // Station 지우기
        graph.removeStation(station);
    }

    private void deleteMiddleStation(Station station) {
        Set<DefaultWeightedEdge> edgesToRemove = new HashSet<>();
        Station upLineStation = null;
        Station downLineStation = null;
        int distanceBetweenUpLineStationAndStation = 0;
        int distanceBetweenStationAndDownLineStation = 0;

        for (DefaultWeightedEdge edge : graph.upStationsOf(station)) {
            edgesToRemove.add(edge);
            distanceBetweenUpLineStationAndStation = (int) graph.getSectionDistance(edge);
            upLineStation = graph.getUpStation(edge);
        }

        for (DefaultWeightedEdge edge : graph.downStationsOf(station)) {
            edgesToRemove.add(edge);
            distanceBetweenStationAndDownLineStation = (int) graph.getSectionDistance(edge);
            downLineStation = graph.getDownStation(edge);
        }

        // 거리 재배치
        int distance = distanceBetweenUpLineStationAndStation + distanceBetweenStationAndDownLineStation;

        // 연결 다시 이어주기
        graph.setSectionDistance(graph.addSection(upLineStation, downLineStation), distance);

        // 지우기
        graph.removeAllSections(edgesToRemove);
        graph.removeStation(station);
    }

    public boolean containsStation(final Station station) {
        return graph.stationSet().contains(station);
    }

    public Station findStationBefore(final Station station) {
        Station previousStation = null;
        DefaultWeightedEdge previousEdge = null;
        Set<DefaultWeightedEdge> incomingEdges = graph.upStationsOf(station);
        if (!incomingEdges.isEmpty()) {
            previousEdge = incomingEdges.iterator().next();
            previousStation = graph.getUpStation(previousEdge);
        }
        return previousStation;
    }

    public Station findStationAfter(final Station station) {
        Station nextStation = null;
        DefaultWeightedEdge nextEdge = null;
        Set<DefaultWeightedEdge> outgoingEdges = graph.downStationsOf(station);
        if (!outgoingEdges.isEmpty()) {
            nextEdge = outgoingEdges.iterator().next();
            nextStation = graph.getDownStation(nextEdge);
        }
        return nextStation;
    }

    public Line getLine() {
        return line;
    }
}
