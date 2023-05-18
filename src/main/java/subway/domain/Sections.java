package subway.domain;

import subway.exception.EndStationNotExistException;
import subway.exception.InvalidSectionConnectException;
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
        assignNextSectionIds();
    }

    private void assignNextSectionIds() {
        Map<Long, Section> upStationToSection = new HashMap<>();
        for (Section section : sections) {
            upStationToSection.put(section.getUpStation().getId(), section);
        }

        assignMatchingNext(upStationToSection);
    }

    private void assignMatchingNext(Map<Long, Section> upStationToSectionMap) {
        for (Section section : sections) {
            Section nextSection = upStationToSectionMap.getOrDefault(section.getDownStation().getId(), null);
            if (nextSection != null) {
                section.setNextSectionId(nextSection.getId());
                continue;
            }
            section.setNextSectionId(null);
        }
        validateNextConnections();
    }

    private void validateNextConnections() {
        Set<UUID> nextIds = sections.stream().map(section -> section.getNextSectionId()).collect(Collectors.toSet());

        if (sections.size() != nextIds.size()) {
            throw new InvalidSectionConnectException();
        }
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
                .filter(existingSection -> existingSection.getNextSectionId() == null)
                .findFirst().orElseThrow(EndStationNotExistException::new);
    }

    public Section getUpEndSection() {
        List<UUID> downSectionIds = sections.stream()
                .map(Section::getNextSectionId)
                .collect(Collectors.toList());

        return sections.stream()
                .filter(isNotContainedId(downSectionIds))
                .findFirst()
                .orElseThrow(EndStationNotExistException::new);
    }

    private static Predicate<Section> isNotContainedId(List<UUID> sectionIds) {
        return existingSection -> !sectionIds.contains(existingSection.getId());
    }

    public boolean isInitialSave() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section findSectionByUpStation(Station station) {
        return sections.stream().filter(section -> section.isSameUpStation(station))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    private Section findSectionByDownStation(final Station station) {
        return sections.stream().filter(section -> section.isSameDownStation(station))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    public boolean isNotExistStation(final Station station) {
        return sections.stream()
                .noneMatch(section -> section.containsStation(station));
    }

    private boolean isUpEnd(Station station) {
        return this.getUpEndSection().isSameUpStation(station);
    }

    private boolean isDownEnd(Station station) {
        return this.getDownEndSection().isSameDownStation(station);
    }

    public List<Station> getStationsInOrder() {
        if (this.sections.isEmpty()) {
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
        Map<UUID, Section> sectionIdMappings = new HashMap<>();
        sections.forEach(section -> sectionIdMappings.put(section.getId(), section));

        return sortSections(sectionIdMappings);
    }

    private List<Section> sortSections(Map<UUID, Section> sectionIdMapping) {
        Section upEndSection = getUpEndSection();
        List<Section> result = new ArrayList<>();
        result.add(upEndSection);
        Section currentSection = upEndSection;
        while (currentSection != null && currentSection.getNextSectionId() != null) {
            Section nextSection = sectionIdMapping.getOrDefault(currentSection.getNextSectionId(), null);
            result.add(nextSection);
            currentSection = nextSection;
        }
        return result;
    }

    private boolean isLastRemained() {
        return sections.size() == ONE_REMAINED;
    }

    public Sections add(Section requestedSection) {
        List<Section> sections = new ArrayList<>(this.sections);

        if (this.isInitialSave()) {
            sections.add(requestedSection);
            return new Sections(this.lineId, sections);
        }
        if (this.isDownEndAppend(requestedSection)) {
            Section downEndSection = this.getDownEndSection();
            sections.remove(downEndSection);
            Section updated = downEndSection.updateDuplicateStation(requestedSection);
            sections.add(updated);
            sections.add(requestedSection);

            return new Sections(this.lineId, sections);
        }
        if (this.isUpEndAppend(requestedSection)) {
            Section upEndSection = this.getUpEndSection();
            sections.remove(upEndSection);
            Section updated = upEndSection.updateDuplicateStation(requestedSection);
            sections.add(updated);
            sections.add(requestedSection);
            assignNextSectionIds();

            return new Sections(this.lineId, sections);
        }

        Section includeSection = this.getIncludeSection(requestedSection);
        sections.remove(includeSection);
        sections.add(requestedSection);
        Section updated = includeSection.updateDuplicateStation(requestedSection);
        sections.add(updated);
        return new Sections(this.lineId, sections);
    }

    public Sections delete(Station station) {
        if (this.isNotExistStation(station)) {
            throw new SectionNotFoundException();
        }

        if (this.isLastRemained()) {
            List<Section> temp = new ArrayList<>(this.sections);
            temp.clear();
            return new Sections(this.lineId, temp);
        }

        if (this.isUpEnd(station)) {
            List<Section> temp = new ArrayList<>(this.sections);
            Section upEndSection = this.getUpEndSection();
            temp.remove(upEndSection);
            return new Sections(this.lineId, temp);
        }

        if (this.isDownEnd(station)) {
            List<Section> temp = new ArrayList<>(this.sections);
            Section downEndSection = this.getDownEndSection();
            temp.remove(downEndSection);
            return new Sections(this.lineId, temp);
        }

        return deleteMiddle(station);
    }

    private Sections deleteMiddle(Station station) {
        List<Section> temp = new ArrayList<>(this.sections);

        Section innerRight = this.findSectionByUpStation(station);
        Section innerLeft = this.findSectionByDownStation(station);
        temp.remove(innerLeft);
        temp.remove(innerRight);

        Section mergedSection = new Section(innerLeft.getUpStation(), innerRight.getDownStation(),
                innerLeft.getDistance() + innerRight.getDistance(),
                innerRight.getNextSectionId());
        temp.add(mergedSection);
        return new Sections(this.lineId, temp);
    }

    public List<Section> getAdded(Sections other) {
        List<Section> temp = new ArrayList<>(this.sections);
        temp.removeAll(other.sections);
        if (temp.isEmpty()) {
            return Collections.emptyList();
        }
       return temp;
    }

    public List<Section> getRemoved(Sections other) {
        Sections temp = new Sections(other.lineId, other.sections);
        temp.sections.removeAll(this.sections);
        return new ArrayList<>(temp.sections);
    }

    public int size() {
        return sections.size();
    }
}
