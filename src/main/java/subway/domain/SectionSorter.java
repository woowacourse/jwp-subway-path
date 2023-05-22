package subway.domain;

import subway.exception.business.InvalidSectionConnectException;

import java.util.*;
import java.util.stream.Collectors;

public class SectionSorter {

    public List<Section> assignSectionConnection(List<Section> sections) {
        Map<Long, Section> upStationToSection = prepareUpToSectionMappings(sections);
        return assignMatchingNext(upStationToSection, sections);
    }

    private Map<Long, Section> prepareUpToSectionMappings(List<Section> sections) {
        Map<Long, Section> upStationToSection = new HashMap<>();
        for (Section section : sections) {
            upStationToSection.put(section.getUpStation().getId(), section);
        }
        return upStationToSection;
    }

    private List<Section> assignMatchingNext(Map<Long, Section> upStationToSectionMap, List<Section> sections) {
        for (Section section : sections) {
            Section nextSection = upStationToSectionMap.getOrDefault(section.getDownStation().getId(), null);
            if (nextSection != null) {
                section.setNextSectionId(nextSection.getId());
                continue;
            }
            section.setNextSectionId(null);
        }
        validateConnection(sections);
        return sections;
    }

    private void validateConnection(List<Section> sections) {
        Set<UUID> nextIds = sections.stream().map(Section::getNextSectionId).collect(Collectors.toSet());
        if (sections.size() != nextIds.size()) {
            throw new InvalidSectionConnectException();
        }
    }

    public List<Station> getStationsInOrder(List<Section> sectionsInput, Section upEndSection) {
        List<Section> sections = new ArrayList<>(sectionsInput);
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Map<UUID, Section> uuidSectionMap = prepareIdMappings(sections);
        List<Section> sorted = sortSections(uuidSectionMap, upEndSection);
        return getStations(sorted);
    }

    private static Map<UUID, Section> prepareIdMappings(List<Section> sections) {
        Map<UUID, Section> sectionIdMappings = new HashMap<>();
        sections.forEach(section -> sectionIdMappings.put(section.getId(), section));
        return sectionIdMappings;
    }

    private List<Section> sortSections(Map<UUID, Section> sectionIdMapping, Section upEndSection) {
        List<Section> result = new ArrayList<>();

        Section currentSection = upEndSection;
        result.add(currentSection);
        while (currentSection != null && currentSection.getNextSectionId() != null) {
            Section nextSection = sectionIdMapping.getOrDefault(currentSection.getNextSectionId(), null);
            result.add(nextSection);
            currentSection = nextSection;
        }
        return result;
    }

    private List<Station> getStations(List<Section> sections) {
        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        for (Section section : sections) {
            stations.add(section.getDownStation());
        }
        return stations;
    }
}
