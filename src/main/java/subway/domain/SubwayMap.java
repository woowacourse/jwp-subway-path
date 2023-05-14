package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class SubwayMap {

    private final Map<Station, Sections> subwayMap;
    private final Map<Line, Optional<Station>> endpointMap;

    public SubwayMap() {
        this.subwayMap = new HashMap<>();
        this.endpointMap = new HashMap<>();
    }

    public SubwayMap(final Map<Station, Sections> subwayMap, final Map<Line, Optional<Station>> endpointMap) {
        this.subwayMap = new HashMap<>(subwayMap);
        this.endpointMap = new HashMap<>(endpointMap);
    }

    public List<Station> getStationsOrderById() {
        return subwayMap.keySet().stream().sorted(Comparator.comparingLong(Station::getId)).collect(Collectors.toList());
    }

    public List<Station> stationsInLine(final Line line) {
        final Station upEndpoint = endpointMap.get(line).orElseThrow(() -> new IllegalArgumentException("라인에 역이 없습니다."));
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
            return sameLineSections.stream().filter(s -> !s.isArrival(visited)).findAny();
        }
        throw new IllegalArgumentException("한 라인에 갈림길이 존재할 수 없습니다.");
    }

    private void updateSections(final Station prevStation, final Section prevToThis, final Station nextStation, final Section nextToThis) {
        final Sections prevSections = subwayMap.get(prevStation);
        final Sections updatedPrevSections = prevSections.updateArrival(nextStation, prevToThis);
        subwayMap.put(prevStation, updatedPrevSections);

        final Sections nextSections = subwayMap.get(nextStation);
        final Sections updatedNextSections = nextSections.updateArrival(prevStation, nextToThis);
        subwayMap.put(nextStation, updatedNextSections);
    }

    public void deleteStation(final Long id) {
        final Station station = getStationById(id);
        final Sections sectionsByStation = subwayMap.get(station);

        Set<Line> includingLines = sectionsByStation.getLinesIncludingStation();


        for (final Line line : includingLines) {
            final Section sameLineSection = sectionsByStation.getSameLineSection(line);

            if (isLastTwoStations(line)) {
                final Station arrival = sameLineSection.getArrival();
                subwayMap.remove(arrival);
                endpointMap.replace(line, Optional.empty());
                continue;
            }
            if (isUpEndpoint(station, line)) {
                final Station arrival = sameLineSection.getArrival();
                replaceEndSection(station, arrival);
                endpointMap.replace(line, Optional.of(arrival));
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

    private boolean isLastTwoStations(final Line line) {
        final long countSectionsInLine = countSectionsInLine(line);
        return countSectionsInLine == 2;
    }

    private boolean isUpEndpoint(final Station station, final Line line) {
        return station.getId().equals(endpointMap.get(line).orElseThrow(() -> new IllegalArgumentException("라인에 역이 없습니다")).getId());
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
        final Sections prevSections = subwayMap.get(prevStation);
        final Sections removedSections = prevSections.removeArrival(station);
        final Sections addedSections = removedSections.addSection(new Section(distanceSum, prevStation, nextStation, line));
        subwayMap.replace(prevStation, addedSections);
    }

    private long countSectionsInLine(final Line line) {
        return subwayMap.values().stream().flatMap(sections -> sections.getSections().stream()).filter(section -> section.getLine().equals(line)).count();
    }

    public Station addStation(Station station) {
        if (subwayMap.containsKey(station)) {
            throw new IllegalStateException("해당 역은 이미 등록되어 있습니다.");
        }

        subwayMap.put(station, new Sections());
        return station;
    }

    public void connectInitStations(Long lineId, Long thisStationId, Long nextStationId, int distance) {
        final Station thisStation = findStationById(thisStationId);
        final Station nextStation = findStationById(nextStationId);
        final Line line = findLineById(lineId);

        Section thisToNext = new Section(distance, thisStation, nextStation, line);
        Section nextToThis = new Section(distance, nextStation, thisStation, line);

        subwayMap.replace(thisStation, new Sections(List.of(thisToNext)));
        subwayMap.replace(nextStation, new Sections(List.of(nextToThis)));

        endpointMap.put(thisToNext.getLine(), Optional.of(thisStation));
    }

    private Line findLineById(Long lineId) {
        return endpointMap.keySet().stream()
                .filter(line -> line.getId().equals(lineId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 라인은 없는 라인입니다."));
    }

    private Station findStationById(Long upStationId) {
        return subwayMap.keySet().stream()
                .filter(station -> station.equalsId(upStationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 역이 없습니다."));
    }

    public void connectUpEndpoint(Long lineId, Long stationId, int distance) {
        final Station thisStation = findStationById(stationId);
        Line line = findLineById(lineId);
        Station upEndpointStation = endpointMap.get(line).orElseThrow(() -> new IllegalArgumentException("라인에 역이 없습니다"));

        Section thisToUp = new Section(distance, thisStation, upEndpointStation, line);

        subwayMap.replace(thisStation, new Sections(List.of(thisToUp)));

        Sections upEndpointSections = subwayMap.get(upEndpointStation);
        Sections addedSections = upEndpointSections.addSection(thisToUp.getReverse());
        subwayMap.replace(upEndpointStation, addedSections);

        endpointMap.replace(line, Optional.of(thisStation));
    }

    public void connectDownEndpoint(Long lineId, Long stationId, int distance) {
        final Station thisStation = findStationById(stationId);
        Line line = findLineById(lineId);
        Station downEndpointStation = findDownEndpoint(line);

        Section thisToDown = new Section(distance, thisStation, downEndpointStation, line);

        subwayMap.replace(thisStation, new Sections(List.of(thisToDown)));

        Sections upEndpointSections = subwayMap.get(downEndpointStation);
        Sections addedSections = upEndpointSections.addSection(thisToDown.getReverse());
        subwayMap.replace(downEndpointStation, addedSections);

        endpointMap.replace(line, Optional.of(thisStation));
    }

    private Station findDownEndpoint(Line line) {
        List<Station> stations = stationsInLine(line);
        return stations.get(stations.size() - 1);
    }

    public void connectMidStation(Long lineId, Long stationId, Long prevStationId, Long nextStationId, int prevDistance) {
        final Station thisStation = findStationById(stationId);
        final Station prevStation = findStationById(prevStationId);
        final Station nextStation = findStationById(nextStationId);
        final Line line = findLineById(lineId);

        Section thisToPrev = new Section(prevDistance, thisStation, prevStation, line);

        final Sections prevSections = subwayMap.get(prevStation);
        int nextDistance = prevSections.thisToNextDistance(nextStation, thisToPrev);

        Section thisToNext = new Section(nextDistance, thisStation, nextStation, line);

        subwayMap.replace(thisStation, new Sections(List.of(thisToPrev, thisToNext)));
        updateSections(prevStation, thisToPrev.getReverse(), nextStation, thisToNext.getReverse());
    }

    public Station findStationByName(String name) {
        return subwayMap.keySet().stream().filter(station -> station.getName().equals(name)).findAny().orElseThrow(() -> new IllegalArgumentException("역 삽입후 삽입된 역의 이름을 찾지 못했습니다."));
    }

    public Map<Station, Sections> getSubwayMap() {
        return new HashMap<>(subwayMap);
    }

    public Map<Line, Optional<Station>> getEndpointMap() {
        return new HashMap<>(endpointMap);
    }

}
