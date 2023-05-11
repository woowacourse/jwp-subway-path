package subway.section.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.section.dto.SectionStationDto;
import subway.station.persistence.StationEntity;

public final class SectionsStationDtoSorter {

    private static final int START_STATION_COUNT = 1;

    private final List<SectionStationDto> sections;
    private final Map<String, SectionStationDto> sectionRelations;

    private SectionsStationDtoSorter(final List<SectionStationDto> sectionStationDtos) {
        this.sections = sectionStationDtos;
        this.sectionRelations = generateSectionRelation(sectionStationDtos);
    }

    public static SectionsStationDtoSorter use(final List<SectionStationDto> sections) {
        return new SectionsStationDtoSorter(sections);
    }

    private Map<String, SectionStationDto> generateSectionRelation(final List<SectionStationDto> sections) {
        return sections.stream()
            .collect(Collectors.toMap(SectionStationDto::getUpStationName, section -> section));
    }

    public List<SectionStationDto> getSortedSectionStationsDto() {
        final ArrayList<SectionStationDto> sortedSection = new ArrayList<>();
        final List<String> sortedStations = getSortedStationNames();

        for (int index = 0; index < sortedStations.size() - 1; index++) {
            final SectionStationDto findSectionStationDto = sectionRelations.getOrDefault(sortedStations.get(index),
                null);
            if (findSectionStationDto == null) {
                throw new IllegalArgumentException("해당 역에서 시작하는 구간이 존재하지 않습니다.");
            }
            sortedSection.add(findSectionStationDto);
        }
        return sortedSection;
    }

    public List<StationEntity> getSortedStationEntities() {
        final ArrayList<StationEntity> sortedStationEntities = new ArrayList<>();
        final List<String> sortedStationNames = getSortedStationNames();

        for (int index = 0; index < sortedStationNames.size() - 1; index++) {
            final SectionStationDto sectionStationDto = sectionRelations.get(sortedStationNames.get(index));
            sortedStationEntities.add(sectionStationDto.getUpStationEntity());
        }

        final String lastSectionUpStationName = sortedStationNames.get(sortedStationNames.size() - 2);
        final SectionStationDto lastSectionStationDto = sectionRelations.get(lastSectionUpStationName);
        sortedStationEntities.add(lastSectionStationDto.getDownStationEntity());

        return sortedStationEntities;
    }

    private List<String> getSortedStationNames() {
        final ArrayList<String> stations = new ArrayList<>();

        String currentStationName = findStartStationName();
        while (sectionRelations.containsKey(currentStationName)) {
            stations.add(currentStationName);
            currentStationName = sectionRelations.get(currentStationName).getDownStationName();
        }
        stations.add(currentStationName);

        return stations;
    }

    private String findStartStationName() {
        final List<String> upStationNames = new ArrayList<>(sectionRelations.keySet());
        final List<String> downStationNames = sectionRelations.values()
            .stream()
            .map(SectionStationDto::getDownStationName)
            .collect(Collectors.toList());

        final List<String> startStation = upStationNames.stream().
            filter((station -> !downStationNames.contains(station)))
            .collect(Collectors.toList());

        if (startStation.size() != START_STATION_COUNT) {
            throw new IllegalArgumentException("시작 지점은 반드시 한개 존재해야 합니다.");
        }

        return startStation.get(0);
    }
}
