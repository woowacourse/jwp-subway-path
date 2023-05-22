package subway.domain;

import subway.exception.business.InvalidSectionLengthException;
import subway.exception.business.SectionNotFoundException;
import subway.exception.business.StationNotFoundException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sections {
    public static final int ONE_REMAINED = 1;
    public static final UUID DOWN_END_NEXT = null;

    private final long lineId;
    private final List<Section> sections;
    private final SectionSorter sectionSorter = new SectionSorter();

    public Sections(final long lineId, final List<Section> sections) {
        this.lineId = lineId;
        this.sections = sectionSorter.assignSectionConnection(sections);
    }

    public Sections add(Section requestedSection) {
        List<Section> sections = new ArrayList<>(this.sections);

        if (this.isInitialSave()) {
            sections.add(requestedSection);
            return new Sections(this.lineId, sections);
        }

        if (this.getDownEndSection().isNextContinuable(requestedSection)) {
            return updateSection(requestedSection, this.getDownEndSection(), sections);
        }

        if (this.getUpEndSection().isPreviousContinuable(requestedSection)) {
            return updateSection(requestedSection, this.getUpEndSection(), sections);
        }

        //saveInMiddle
        return updateSection(requestedSection, this.getIncludeSection(requestedSection), sections);
    }

    private Sections updateSection(Section requestedSection, Section updateTarget, List<Section> sections) {
        sections.remove(updateTarget);
        Section updated = updateTarget.updateDuplicateStation(requestedSection);
        sections.add(updated);
        sections.add(requestedSection);

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
            temp.remove(this.getUpEndSection());
            return new Sections(this.lineId, temp);
        }

        if (this.isDownEnd(station)) {
            List<Section> temp = new ArrayList<>(this.sections);
            temp.remove(this.getDownEndSection());
            return new Sections(this.lineId, temp);
        }

        return deleteMiddle(station);
    }

    private boolean isInitialSave() {
        return sections.isEmpty();
    }

    private boolean isLastRemained() {
        return sections.size() == ONE_REMAINED;
    }

    private boolean isUpEnd(Station station) {
        return this.getUpEndSection().isSameUpStation(station);
    }

    private boolean isDownEnd(Station station) {
        return this.getDownEndSection().isSameDownStation(station);
    }

    private Sections deleteMiddle(Station station) {
        List<Section> temp = new ArrayList<>(this.sections);

        Section innerRight = this.findSectionByUpStation(station);
        Section innerLeft = this.findSectionByDownStation(station);
        temp.remove(innerLeft);
        temp.remove(innerRight);

        Section mergedSection = new Section(innerLeft.getUpStation(), innerRight.getDownStation(),
                innerLeft.getDistance() + innerRight.getDistance(), innerRight.getNextSectionId());
        temp.add(mergedSection);
        return new Sections(this.lineId, temp);
    }

    private Section findSectionByUpStation(Station station) {
        return sections.stream().filter(section -> section.isSameUpStation(station))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    private Section findSectionByDownStation(final Station station) {
        return sections.stream().filter(section -> section.isSameDownStation(station))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    private Section getIncludeSection(Section newSection) {
        Section includeSection = sections.stream()
                .filter(existingSection -> existingSection.hasIntersection(newSection))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);

        if (includeSection.isDistanceSmallOrSame(newSection)) {
            throw new InvalidSectionLengthException();
        }
        return includeSection;
    }

    public Section getDownEndSection() {
        return sections.stream()
                .filter(existingSection -> existingSection.getNextSectionId() == DOWN_END_NEXT)
                .findFirst().orElseThrow(StationNotFoundException::new);
    }

    public Section getUpEndSection() {
        List<UUID> nextSectionIds = sections.stream()
                .map(Section::getNextSectionId)
                .collect(Collectors.toList());

        return sections.stream()
                .filter(isNotContainedId(nextSectionIds))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);
    }

    private static Predicate<Section> isNotContainedId(List<UUID> sectionIds) {
        return existingSection -> !sectionIds.contains(existingSection.getId());
    }

    private boolean isNotExistStation(final Station station) {
        return sections.stream()
                .noneMatch(section -> section.containsStation(station));
    }

    public List<Station> getStationsInOrder() {
        return sectionSorter.getStationsInOrder(this.sections, this.getUpEndSection());
    }

    public List<Section> getAddedCompareTo(Sections other) {
        List<Section> temp = new ArrayList<>(this.sections);
        temp.removeAll(other.sections);
        if (temp.isEmpty()) {
            return Collections.emptyList();
        }
       return temp;
    }

    public List<Section> getRemovedCompareTo(Sections other) {
        Sections temp = new Sections(other.lineId, other.sections);
        temp.sections.removeAll(this.sections);
        return new ArrayList<>(temp.sections);
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public long getLineId() {
        return lineId;
    }
}
