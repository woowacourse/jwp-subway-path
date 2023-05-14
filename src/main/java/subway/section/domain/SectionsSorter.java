package subway.section.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.station.domain.Station;

public final class SectionsSorter {

    private static final int START_STATION_COUNT = 1;

    private final List<Section> sections;
    private final Map<Station, Section> sectionRelations;

    private SectionsSorter(final List<Section> sections) {
        this.sections = sections;
        this.sectionRelations = generateSectionRelation(sections);
    }

    public static SectionsSorter use(final List<Section> sections) {
        return new SectionsSorter(sections);
    }

    private Map<Station, Section> generateSectionRelation(final List<Section> sections) {
        return sections.stream()
            .collect(Collectors.toMap(Section::getUpStation, section -> section));
    }

    public List<Section> getSortedSections() {
        final ArrayList<Section> sortedSection = new ArrayList<>();
        final List<Station> sortedStations = getSortedStation();

        for (int index = 0; index < sortedStations.size() - 1; index++) {
            final Section section = sectionRelations.getOrDefault(sortedStations.get(index),
                null);
            if (section == null) {
                throw new IllegalArgumentException("해당 역에서 시작하는 구간이 존재하지 않습니다.");
            }
            sortedSection.add(section);
        }
        return sortedSection;
    }

    private List<Station> getSortedStation() {
        final ArrayList<Station> stations = new ArrayList<>();

        Station currentStation = findStartStationName();
        while (sectionRelations.containsKey(currentStation)) {
            stations.add(currentStation);
            currentStation = sectionRelations.get(currentStation).getDownStation();
        }
        stations.add(currentStation);

        return stations;
    }

    private Station findStartStationName() {
        final List<Station> upStations = new ArrayList<>(sectionRelations.keySet());
        final List<Station> downStations = sectionRelations.values()
            .stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        final List<Station> startStation = upStations.stream().
            filter((station -> !downStations.contains(station)))
            .collect(Collectors.toList());

        if (startStation.size() != START_STATION_COUNT) {
            throw new IllegalArgumentException("시작 지점은 반드시 한개 존재해야 합니다.");
        }

        return startStation.get(0);
    }

//    public List<StationEntity> getSortedStationEntities() {
//        final ArrayList<StationEntity> sortedStationEntities = new ArrayList<>();
//        final List<String> sortedStationNames = getSortedStationNames();
//
//        for (int index = 0; index < sortedStationNames.size() - 1; index++) {
//            final SectionStationDto sectionStationDto = sectionRelations.get(sortedStationNames.get(index));
//            sortedStationEntities.add(sectionStationDto.getUpStationEntity());
//        }
//
//        final String lastSectionUpStationName = sortedStationNames.get(sortedStationNames.size() - 2);
//        final SectionStationDto lastSectionStationDto = sectionRelations.get(lastSectionUpStationName);
//        sortedStationEntities.add(lastSectionStationDto.getDownStationEntity());
//
//        return sortedStationEntities;
//    }
}
