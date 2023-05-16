package subway.domain;

import subway.exception.EndStationNotExistException;
import subway.exception.InvalidSectionLengthException;
import subway.exception.SectionNotFoundException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sections {
    public static final int DOWN_END_ID = 0;
    public static final int ONE_REMAINED = 1;
    private final long lineId;
    private final List<Section> sections;

    public Sections(final long lineId, final List<Section> sections) {
        this.lineId = lineId;
        this.sections = new ArrayList<>(sections);
    }

    public Section getIncludeSection(Section newSection) {
        Section includeSection = sections.stream()
                .filter(existingSection -> existingSection.hasIntersection(newSection))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);

        if (includeSection.isDistanceSmallOrSame(newSection)) {
            throw new InvalidSectionLengthException();
        }
        return includeSection;
    }

    public boolean isDownEndAppend(Section section) {
        Section downEndSection = getDownEndSection();

        return downEndSection.isNextContinuable(section);
    }


    public boolean isUpEndAppend(final Section section) {
        Section upEndSection = getUpEndSection();

        return upEndSection.isPreviousContinuable(section);
    }

    public Section getDownEndSection() {
        return sections.stream()
                .filter(existingSection -> existingSection.getNextSectionId() == 0)
                .findFirst().orElseThrow(EndStationNotExistException::new);
    }

    public Section getUpEndSection() {
        List<Long> downSectionIds = sections.stream()
                .map(Section::getNextSectionId)
                .collect(Collectors.toList());

        return sections.stream()
                .filter(isNotContainedId(downSectionIds))
                .findFirst()
                .orElseThrow(EndStationNotExistException::new);
    }

    private static Predicate<Section> isNotContainedId(List<Long> sectionIds) {
        return existingSection -> !sectionIds.contains(existingSection.getId());
    }

    public boolean isInitialSave() {
        return sections.isEmpty();
    }

    public int size() {
        return this.sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section findPreviousSection(Section section) {
        return sections.stream()
                .filter(existingSection -> existingSection.getNextSectionId().equals(section.getId()))
                .findFirst().orElseThrow(SectionNotFoundException::new);
    }

    public Section findSectionByUpStation(final long stationId) {
        return sections.stream().filter(section -> section.isSameUpStationId(stationId))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    public Section findSectionByDownStation(final long stationId) {
        return sections.stream().filter(section -> section.isSameDownStationId(stationId))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    public boolean isNotExistStation(final long stationId) {
        return sections.stream()
                .noneMatch(section -> section.containsStation(stationId));
    }


    public boolean isUpEnd(long stationId) {
        return this.getUpEndSection().isSameUpStationId(stationId);
    }

    public boolean isDownEnd(long stationId) {
        return this.getDownEndSection().isSameDownStationId(stationId);
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Section> sortedSections = this.getSorted();

        List<Station> sortedStations = new ArrayList<>();
        sortedStations.add(sortedSections.get(0).getUpStation());
        for (Section section : sortedSections) {
            sortedStations.add(section.getDownStation());
        }
        return sortedStations;
    }

    private List<Section> getSorted() {
        Map<Long, Section> sectionIdMappings = new HashMap<>();
        sections.forEach(section -> sectionIdMappings.put(section.getId(), section));

        return sortSections(sectionIdMappings);
    }

    private List<Section> sortSections(Map<Long, Section> sectionIdMapping) {
        Section upEndSection = getUpEndSection();
        List<Section> result = new ArrayList<>();
        result.add(upEndSection);
        Section currentSection = upEndSection;
        while (currentSection != null && currentSection.getNextSectionId() != DOWN_END_ID) {
            Section nextSection = sectionIdMapping.getOrDefault(currentSection.getNextSectionId(), null);
            result.add(nextSection);
            currentSection = nextSection;
        }
        return result;
    }


    public boolean isLastRemained() {
        return sections.size() == ONE_REMAINED;
    }

    @Override
    public String toString() {
        return "Sections{" +
                "lineId=" + lineId +
                ", sections=" + sections +
                '}';
    }
}
