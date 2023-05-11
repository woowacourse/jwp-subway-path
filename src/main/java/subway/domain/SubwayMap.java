package subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubwayMap {

    private final Map<Station, Sections> subwayMap;
    private final Map<Line, Station> endpointMap;

    public SubwayMap(final Map<Station, Sections> subwayMap, final Map<Line, Station> endpointMap) {
        this.subwayMap = new HashMap<>(subwayMap);
        this.endpointMap = new HashMap<>(endpointMap);
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

    public Map<Station, Sections> getSubwayMap() {
        return new HashMap<>(subwayMap);
    }

    public Map<Line, Station> getEndpointMap() {
        return new HashMap<>(endpointMap);
    }

    public Sections findSectionByStation(final Station station) {
        return subwayMap.get(station);
    }
}
