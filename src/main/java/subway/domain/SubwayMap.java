package subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubwayMap {

    private final Map<Station, Sections> subwayMap;

    public SubwayMap(final Map<Station, Sections> subwayMap) {
        this.subwayMap = new HashMap<>(subwayMap);
    }

    public void addIntermediateStation(Section thisToPrev, Section thisToNext) {
        Station prevStation = thisToPrev.getArrival();
        Station thisStation = thisToPrev.getDeparture();
        Station nextStation = thisToNext.getArrival();

        Sections prevSections = subwayMap.get(prevStation);
        prevSections.validateDistanceBetween(nextStation, thisToPrev, thisToNext);

        subwayMap.put(thisStation, new Sections(List.of(thisToPrev, thisToNext)));
        updateSections(prevStation, thisToPrev.getReverse(), nextStation, thisToNext.getReverse());
    }

    private void updateSections(Station prevStation, Section prevToThis, Station nextStation, Section nextToThis) {
        Sections prevSections = subwayMap.get(prevStation);
        Sections updatedPrevSections = prevSections.updateArrival(nextStation, prevToThis);
        subwayMap.put(prevStation, updatedPrevSections);

        Sections nextSections = subwayMap.get(nextStation);
        Sections updatedNextSections = nextSections.updateArrival(prevStation, nextToThis);
        subwayMap.put(nextStation, updatedNextSections);
    }

    public void addInitialStations(Section upToDown, Section downToUp) {
        if (lineHasStation(upToDown)) {
            throw new IllegalArgumentException("해당 라인은 초기 등록이 아닙니다.");
        }

        Station upEndpoint = upToDown.getDeparture();
        Station downEndpoint = upToDown.getArrival();

        subwayMap.put(upEndpoint, new Sections(List.of(upToDown)));
        subwayMap.put(downEndpoint, new Sections(List.of(downToUp)));
    }

    private boolean lineHasStation(Section other) {
        return subwayMap.values().stream()
                .anyMatch(sections -> sections.hasSameLine(other));
    }

    public void addEndPointStation(Section thisToEnd) {
        Station thisStation = thisToEnd.getDeparture();
        Station endPoint = thisToEnd.getArrival();

        validateIsEndpoint(thisToEnd, endPoint);

        subwayMap.put(thisStation, new Sections(List.of(thisToEnd)));

        addSectionToEndpoint(endPoint, thisToEnd.getReverse());
    }

    private void validateIsEndpoint(Section thisToEnd, Station endPoint) {
        Sections sections = subwayMap.get(endPoint);
        if (sections.countSameLine(thisToEnd) != 1) {
            throw new IllegalArgumentException("도착지로 선택한 역이 종점이 아닙니다.");
        }
    }

    private void addSectionToEndpoint(Station station, Section section) {
        Sections sections = subwayMap.get(station);
        if (sections == null) {
            throw new IllegalArgumentException("역이 존재하지 않습니다.");
        }
        Sections addedSections = sections.addSection(section);
        subwayMap.put(station, addedSections);
    }

    public Map<Station, Sections> getSubwayMap() {
        return new HashMap<>(subwayMap);
    }

    public Sections findSectionByStation(final Station station) {
        return subwayMap.get(station);
    }
}
