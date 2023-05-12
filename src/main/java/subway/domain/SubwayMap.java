package subway.domain;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SubwayMap {

    private final Map<Station, Sections> subwayMap;
    private final Map<Line, Station> endpointMap;

    public SubwayMap() {
        this.subwayMap = new HashMap<>();
        this.endpointMap = new HashMap<>();
    }

    public SubwayMap(final Map<Station, Sections> subwayMap, final Map<Line, Station> endpointMap) {
        this.subwayMap = new HashMap<>(subwayMap);
        this.endpointMap = new HashMap<>(endpointMap);
    }

    public Map<Line, List<Station>> getAllStationsGroupByLine() {
        return endpointMap.keySet().stream()
                .collect(Collectors.toMap(Function.identity(),
                        this::stationsInLine));
    }

    public List<Station> stationsInLine(final Line line) {
        final Station upEndpoint = endpointMap.get(line);
        final List<Station> stations = new ArrayList<>();

        Optional<Section> currentSection = getNextSection(line, upEndpoint, null);
        stations.add(currentSection.orElseThrow().getDeparture());
        while (currentSection.isPresent()) {
            final Station visited = currentSection.orElseThrow().getDeparture();
            final Station current = currentSection.orElseThrow().getArrival();
            stations.add(currentSection.orElseThrow().getArrival());
            currentSection = getNextSection(line, current, visited);
        }
        return stations;
    }

    private Optional<Section> getNextSection(final Line line, final Station current, final Station visited) {
        final List<Section> sameLineSections = subwayMap.get(current).getSameLineSections(line);
        final int length = sameLineSections.size();
        if (length == 1) {
            if (sameLineSections.get(0).getArrival().equals(visited)) {
                return Optional.empty();
            }
            return Optional.of(sameLineSections.get(0));
        }
        if (length == 2) {
            return sameLineSections.stream().
                    filter(s -> !s.isArrival(visited))
                    .findAny();
        }
        throw new IllegalArgumentException("한 라인에 갈림길이 존재할 수 없습니다.");
    }

    public void addIntermediateStation(final Section thisToPrev, final Section thisToNext) {
        final Station prevStation = thisToPrev.getArrival();
        final Station thisStation = thisToPrev.getDeparture();
        final Station nextStation = thisToNext.getArrival();

        final Sections prevSections = subwayMap.get(prevStation);
        prevSections.validateDistanceBetween(nextStation, thisToPrev, thisToNext);

        subwayMap.put(thisStation, new Sections(List.of(thisToPrev, thisToNext)));
        updateSections(prevStation, thisToPrev.getReverse(), nextStation, thisToNext.getReverse());
    }

    private void updateSections(final Station prevStation, final Section prevToThis, final Station nextStation, final Section nextToThis) {
        final Sections prevSections = subwayMap.get(prevStation);
        final Sections updatedPrevSections = prevSections.updateArrival(nextStation, prevToThis);
        subwayMap.put(prevStation, updatedPrevSections);

        final Sections nextSections = subwayMap.get(nextStation);
        final Sections updatedNextSections = nextSections.updateArrival(prevStation, nextToThis);
        subwayMap.put(nextStation, updatedNextSections);
    }

    public void addInitialStations(final Section upToDown, final Section downToUp) {
        if (lineHasStation(upToDown)) {
            throw new IllegalArgumentException("해당 라인은 초기 등록이 아닙니다.");
        }

        final Station upEndpoint = upToDown.getDeparture();
        final Station downEndpoint = upToDown.getArrival();

        subwayMap.put(upEndpoint, new Sections(List.of(upToDown)));
        subwayMap.put(downEndpoint, new Sections(List.of(downToUp)));

        endpointMap.put(upToDown.getLine(), upEndpoint);
    }

    private boolean lineHasStation(final Section other) {
        return subwayMap.values().stream()
                .anyMatch(sections -> sections.hasSameLine(other));
    }

    public void addUpEndPoint(final Section thisToEnd) {
        addEndPointStation(thisToEnd);
        endpointMap.replace(thisToEnd.getLine(), thisToEnd.getDeparture());
    }

    private void addEndPointStation(final Section thisToEnd) {
        final Station thisStation = thisToEnd.getDeparture();
        final Station endPoint = thisToEnd.getArrival();

        validateIsEndpoint(thisToEnd, endPoint);

        subwayMap.put(thisStation, new Sections(List.of(thisToEnd)));

        addSectionToEndpoint(endPoint, thisToEnd.getReverse());
    }

    private void validateIsEndpoint(final Section thisToEnd, final Station endPoint) {
        final Sections sections = subwayMap.get(endPoint);
        if (sections.countSameLine(thisToEnd) != 1) {
            throw new IllegalArgumentException("도착지로 선택한 역이 종점이 아닙니다.");
        }
    }

    private void addSectionToEndpoint(final Station station, final Section section) {
        final Sections sections = subwayMap.get(station);
        if (sections == null) {
            throw new IllegalArgumentException("역이 존재하지 않습니다.");
        }
        final Sections addedSections = sections.addSection(section);
        subwayMap.put(station, addedSections);
    }

    public void addDownEndPoint(final Section thisToEnd) {
        addEndPointStation(thisToEnd);
    }

    public void deleteStation(final Long id) {
        final Station station = getStationById(id);
        final Sections sectionsByStation = findSectionByStation(station);

        final Set<Line> includingLines = sectionsByStation.getLinesIncludingStation();

        for (final Line line : includingLines) {
            final Section sameLineSection = sectionsByStation.getSameLineSection(line);

            if (isLastTwoStations(line)) {
                final Station arrival = sameLineSection.getArrival();
                subwayMap.remove(arrival);
                continue;
            }
            if (isUpEndpoint(station, line)) {
                final Station arrival = sameLineSection.getArrival();
                replaceEndSection(station, arrival);
                endpointMap.replace(line, arrival);
                continue;
            }
            if (isEndpoint(sectionsByStation, line)) {
                final Station arrival = sameLineSection.getArrival();
                replaceEndSection(station, arrival);
                continue;
            }

            final List<Section> sameLineSections = sectionsByStation.getSameLineSections(line);
            final Section prevSection = sameLineSections.get(0);
            final Section nextSection = sameLineSections.get(1);
            final int distanceSum = prevSection.getDistance() + nextSection.getDistance();

            final Station prevStation = prevSection.getArrival();
            final Station nextStation = nextSection.getArrival();

            replaceSection(station, line, distanceSum, prevStation, nextStation);
            replaceSection(station, line, distanceSum, nextStation, prevStation);
        }
        subwayMap.remove(station);
    }

    private Station getStationById(final Long id) {
        return subwayMap.keySet().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 역이 존재하지 않습니다."));
    }

    public Sections findSectionByStation(final Station station) {
        return subwayMap.get(station);
    }

    private boolean isLastTwoStations(final Line line) {
        final long countSectionsInLine = countSectionsInLine(line);
        return countSectionsInLine == 2;
    }

    private boolean isUpEndpoint(final Station station, final Line line) {
        return station.getId().equals(endpointMap.get(line).getId());
    }

    private void replaceEndSection(final Station station, final Station arrival) {
        final Sections arrivalSections = subwayMap.get(arrival);
        final Sections removedSections = arrivalSections.removeArrival(station);
        subwayMap.replace(arrival, removedSections);
    }

    private boolean isEndpoint(final Sections sectionsByStation, final Line line) {
        return sectionsByStation.countSectionByLine(line) == 1;
    }

    private void replaceSection(final Station station, final Line line, final int distanceSum, final Station prevStation, final Station nextStation) {
        final Sections prevSections = findSectionByStation(prevStation);
        final Sections removedSections = prevSections.removeArrival(station);
        final Sections addedSections = removedSections.addSection(new Section(distanceSum, prevStation, nextStation, line));
        subwayMap.replace(prevStation, addedSections);
    }

    private long countSectionsInLine(final Line line) {
        return subwayMap.values().stream()
                .flatMap(sections -> sections.getSections().stream())
                .filter(section -> section.getLine().equals(line))
                .count();
    }

    public Map<Station, Sections> getSubwayMap() {
        return new HashMap<>(subwayMap);
    }

    public Map<Line, Station> getEndpointMap() {
        return new HashMap<>(endpointMap);
    }
}
