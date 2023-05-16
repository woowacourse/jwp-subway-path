package subway.domain.subwayMap.domain;

import subway.domain.station.domain.Station;
import subway.domain.section.domain.Section;

import java.util.*;

/**
 * LineId로 검색한 List<Section>을 정렬된 List<Station>로 반환하는 Converter
 */
public class SectionToStationConverter {

    private final Map<Station, List<Section>> stationGraph;

    public SectionToStationConverter(final Map<Station, List<Section>> stationGraph) {
        this.stationGraph = stationGraph;
    }

    public static SectionToStationConverter of(final List<Section> sections) {
        final SectionToStationConverter sectionToStationConverter = new SectionToStationConverter(new HashMap<>());
        for (final Section section : sections) {
            sectionToStationConverter.put(section.getUpStation(), section);
            sectionToStationConverter.put(section.getDownStation(), section);
        }
        return sectionToStationConverter;
    }

    private void put(final Station station, final Section section) {
        stationGraph.computeIfAbsent(station, key -> new ArrayList<>()).add(section);
    }

    private Station findFirstSection() {
        Station firstStation = stationGraph.keySet().iterator().next();

        final Queue<Station> queue = new LinkedList<>();
        queue.add(firstStation);
        while (!queue.isEmpty()) {
            final Station poll = queue.poll();
            final List<Section> sections = stationGraph.get(poll);
            for (final Section section : sections) {
                if (section.getDownStation().equals(poll)) {
                    queue.add(section.getUpStation());
                    firstStation = section.getUpStation();
                }
            }
        }
        return firstStation;
    }

    public List<Station> getSortedStation() {
        final List<Station> result = new ArrayList<>();
        final Queue<Station> queue = new LinkedList<>();

        Station firstSection = findFirstSection();
        queue.add(firstSection);
        result.add(firstSection);
        while (!queue.isEmpty()) {
            final Station station = queue.poll();
            final List<Section> sections = stationGraph.get(station);

            for (final Section section : sections) {
                if (section.getUpStation().equals(station)) {
                    Station downStation = section.getDownStation();
                    queue.add(downStation);
                    result.add(downStation);
                }
            }
        }
        return result;
    }
}
