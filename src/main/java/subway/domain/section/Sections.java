package subway.domain.section;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        validateSections(sections);
        this.sections = sections;
    }

    public static Sections emptySections() {
        return new Sections(new ArrayList<>());
    }

    private void validateSections(final List<Section> sections) {
        if (sections == null) {
            throw new IllegalArgumentException("노선에 구간들은 없을 수 없습니다.");
        }
    }

    public Sections addSection(final Section section) {
        validateContains(section);

        if (isEmpty()) {
            sections.add(section);
            return new Sections(sections);
        }

        if (isAddableOnFrontOfUpTerminal(section)) {
            return addSectionToIndex(section, 0);
        }

        if (isAddableOnBackOfDownTerminal(section)) {
            return addSectionToIndex(section, sections.size());
        }

        return addSectionInMiddle(section);
    }

    private void validateContains(final Section other) {
        if (isContainAllSectionStations(other)) {
            throw new IllegalArgumentException("두 역이 이미 존재합니다.");
        }
    }

    private boolean isContainAllSectionStations(final Section other) {
        return sections.stream().anyMatch(
                section -> section.isSameUpStation(other) || section.isSameDownStationByFlip(other)) &&
                sections.stream().anyMatch(
                        section -> section.isSameDownStation(other) || section.isSameUpStationByFlip(other));
    }

    private boolean isAddableOnFrontOfUpTerminal(final Section other) {
        return sections.get(0).isAssemblableOnFront(other);
    }

    private Sections addSectionToIndex(final Section section, final int index) {
        final List<Section> updatedSections = new LinkedList<>(sections);
        updatedSections.add(index, section);
        return new Sections(updatedSections);
    }

    private boolean isAddableOnBackOfDownTerminal(final Section other) {
        return sections.get(sections.size() - 1).isAssemblableOnBack(other);
    }

    private Sections addSectionInMiddle(final Section other) {
        if (baseStationIsUpStation(other)) {
            return addNewSectionToNext(other);
        }

        return addNewSectionToPrevious(other);
    }

    private boolean baseStationIsUpStation(final Section other) {
        return sections.stream()
                .anyMatch(section -> section.isSameUpStation(other));
    }

    private Sections addNewSectionToNext(final Section other) {
        final Section originalSection = getSectionByPredicate((section) -> section.isSameUpStation(other));

        Section nextSection = originalSection.createDownToDownSection(other);

        return new Sections(addNewSection(
                originalSection,
                other,
                nextSection
        ));
    }

    private List<Section> addNewSection(
            final Section originalSection,
            final Section previousSection,
            final Section nextSection
    ) {
        final List<Section> updatedSections = new LinkedList<>(sections);
        final int originalIndex = sections.indexOf(originalSection);
        updatedSections.remove(originalSection);
        updatedSections.add(originalIndex, previousSection);
        updatedSections.add(originalIndex + 1, nextSection);
        return updatedSections;
    }

    private Sections addNewSectionToPrevious(final Section other) {
        final Section originalSection = getSectionByPredicate((section) -> section.isSameDownStation(other));
        return new Sections(addNewSection(
                originalSection,
                other.createUpToUpSection(originalSection),
                other
        ));
    }

    private Section getSectionByPredicate(final Predicate<Section> sectionFilter) {
        return sections.stream()
                .filter(sectionFilter)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("기준역을 찾을 수 없습니다."));
    }

    public Sections removeStation(final Station station) {
        if (isUpStationOfHeadTerminal(station)) {
            return removeSectionByIndex(0);
        }

        if (isDownStationOfTailTerminal(station)) {
            return removeSectionByIndex(sections.size() - 1);
        }

        return removeStationInMiddle(station);
    }

    private boolean isUpStationOfHeadTerminal(final Station station) {
        return sections.get(0).isSameUpStation(station);
    }

    private Sections removeSectionByIndex(final int index) {
        final List<Section> updateSection = new LinkedList<>(sections);
        updateSection.remove(index);
        return new Sections(updateSection);
    }

    private boolean isDownStationOfTailTerminal(final Station station) {
        return sections.get(sections.size() - 1).isSameDownStation(station);
    }

    private Sections removeStationInMiddle(final Station removeCandidate) {
        final List<Section> updateSection = new LinkedList<>(sections);

        final Section prviousSection = getSectionByPredicate((section) -> section.isSameDownStation(removeCandidate));
        final Section nextSection = getSectionByPredicate((section) -> section.isSameUpStation(removeCandidate));
        final Section newSection = prviousSection.addSection(nextSection);

        final int insertIndex = sections.indexOf(prviousSection);
        updateSection.remove(prviousSection);
        updateSection.remove(nextSection);
        updateSection.add(insertIndex, newSection);

        return new Sections(updateSection);
    }

    public Stations getStations() {
        return new Stations(sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toUnmodifiableList()));
    }

    public Distance totalDistance() {
        return sections.stream().
                reduce(Section::addSection)
                .orElseThrow(() -> new IllegalArgumentException("구간이 없습니다."))
                .getDistance();
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public List<Section> sections() {
        return new ArrayList<>(sections);
    }
}
