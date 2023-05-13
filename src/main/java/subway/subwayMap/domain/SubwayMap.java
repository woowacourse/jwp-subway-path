package subway.subwayMap.domain;

import subway.section.domain.Section;
import subway.station.domain.Station;

import java.util.*;

public class SubwayMap {

    private final Map<Station, List<Section>> subwayMap;

    public SubwayMap(final Map<Station, List<Section>> subwayMap) {
        this.subwayMap = subwayMap;
    }

    public static SubwayMap of(final List<Section> sections) {
        final SubwayMap subwayMap = new SubwayMap(new HashMap<>());
        for (final Section section : sections) {
            subwayMap.put(section.getUpStation(), section);
            subwayMap.put(section.getDownStation(), section);
        }
        return subwayMap;
    }

    private void put(final Station station, final Section section) {
        subwayMap.computeIfAbsent(station, key -> new ArrayList<>()).add(section);
    }

    public List<Station> getStations() {
        final Station station = subwayMap.keySet().iterator().next();
        final List<Station> upStation = computeOneWayUp(station);
        final List<Station> downStation = computeOneWayDown(station);
        final List<Station> mergedList = new ArrayList<>();
        Collections.reverse(upStation);
        mergedList.addAll(upStation);
        mergedList.addAll(downStation);
        return mergedList;
    }

    private List<Station> computeOneWayUp(final Station station) {
        final List<Station> result = new ArrayList<>();
        final Queue<Station> queue = new LinkedList<>();
        queue.add(station);
        result.add(station);
        while (!queue.isEmpty()) {
            final Station poll = queue.poll();
            final List<Section> sections = subwayMap.get(poll);
            for (final Section section : sections) {
                if (section.getDownStation().equals(poll)) {
                    queue.add(section.getUpStation());
                    result.add(section.getUpStation());
                }
            }
        }
        return result;
    }

    private List<Station> computeOneWayDown(final Station station) {
        final List<Station> result = new ArrayList<>();
        final Queue<Station> queue = new LinkedList<>();
        queue.add(station);
        while (!queue.isEmpty()) {
            final Station poll = queue.poll();
            final List<Section> sections = subwayMap.get(poll);
            for (final Section section : sections) {
                if (section.getUpStation().equals(poll)) {
                    queue.add(section.getDownStation());
                    result.add(section.getDownStation());
                }
            }
        }
        return result;
    }
}
