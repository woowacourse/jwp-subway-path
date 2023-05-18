package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections = new ArrayList<>();

    public static Sections from(final List<Section> newSections) {
        final Sections sections = new Sections();
        newSections.forEach(sections::addSection);
        return sections;
    }

    public void addSection(final Section newSection) {
        validate(newSection);
        addSectionBy(newSection);
    }

    private void validate(final Section newSection) {
        if (isNotEmpty() && isBaseStationNotExist(newSection)) {
            throw new IllegalArgumentException("구간이 하나 이상 존재하는 노선에 새로운 구간을 등록할 때 기준이 되는 지하철역이 존재해야 합니다.");
        }
        if (isAlreadyExist(newSection)) {
            throw new IllegalArgumentException("노선에 이미 존재하는 구간을 등록할 수 없습니다.");
        }
    }

    private boolean isNotEmpty() {
        return !sections.isEmpty();
    }

    private boolean isBaseStationNotExist(final Section newSection) {
        return sections.stream().noneMatch(section -> section.isBaseStationExist(newSection));
    }

    private boolean isAlreadyExist(final Section newSection) {
        return sections.contains(newSection);
    }

    private void addSectionBy(final Section newSection) {
        if (addWhenInnerSectionBaseIsUpStation(newSection)) {
            return;
        }
        if (addInnerSectionBaseIsDownStation(newSection)) {
            return;
        }
        if (addOuterSectionBaseIsUpStation(newSection)) {
            return;
        }
        if (addOuterSectionBaseIsDownStation(newSection)) {
            return;
        }
        sections.add(newSection);
    }

    private boolean addWhenInnerSectionBaseIsUpStation(final Section newSection) {
        final Optional<Section> maybeInnerSectionBaseIsUp = findSectionWithOldUpStation(newSection.getUpStation());

        if (maybeInnerSectionBaseIsUp.isPresent()) {
            addInnerSection(newSection, maybeInnerSectionBaseIsUp.get());
            return true;
        }
        return false;
    }

    private void addInnerSection(final Section newSection, final Section oldInnerSection) {
        if (newSection.isDistanceEqualsOrGreaterThan(oldInnerSection)) {
            throw new IllegalArgumentException("기존 구간 내부에 들어올 새로운 구간이 더 길 수 없습니다.");
        }
        addNewSectionAfterRemoveOldSection(newSection, oldInnerSection);
    }

    private Optional<Section> findSectionWithOldUpStation(final Station station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameUpStationBy(station))
                .findFirst();
    }

    private void addNewSectionAfterRemoveOldSection(final Section newSection, final Section oldSection) {
        final int oldSectionIndex = sections.indexOf(oldSection);
        sections.remove(oldSection);
        sections.addAll(oldSectionIndex, oldSection.separateBy(newSection));
    }

    private boolean addInnerSectionBaseIsDownStation(final Section newSection) {
        final Optional<Section> maybeInnerSectionBaseIsDown = findSectionWithOldDownStation(newSection.getDownStation());

        if (maybeInnerSectionBaseIsDown.isPresent()) {
            addInnerSection(newSection, maybeInnerSectionBaseIsDown.get());
            return true;
        }
        return false;
    }

    private Optional<Section> findSectionWithOldDownStation(final Station station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameDownStationBy(station))
                .findFirst();
    }

    private boolean addOuterSectionBaseIsUpStation(final Section newSection) {
        final Optional<Section> maybeOuterSectionBaseIsUp = findSectionWithOldUpStation(newSection.getDownStation());

        if (maybeOuterSectionBaseIsUp.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeOuterSectionBaseIsUp.get());
            return true;
        }
        return false;
    }

    private boolean addOuterSectionBaseIsDownStation(final Section newSection) {
        final Optional<Section> maybeOuterSectionBaseIsDown = findSectionWithOldDownStation(newSection.getUpStation());

        if (maybeOuterSectionBaseIsDown.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeOuterSectionBaseIsDown.get());
            return true;
        }
        return false;
    }

    public void removeStation(final Station removeStation) {
        final List<Section> removeSections = collectRemoveSections(removeStation);
        if (isStationBetween(removeSections)) {
            addCombineSection(removeSections);
        }
        sections.removeAll(removeSections);
    }

    private List<Section> collectRemoveSections(final Station removeStation) {
        return sections.stream()
                .filter(section -> section.isSameUpStationBy(removeStation) || section.isSameDownStationBy(removeStation))
                .collect(Collectors.toList());
    }

    private boolean isStationBetween(final List<Section> removeSections) {
        return removeSections.size() == 2;
    }

    private void addCombineSection(final List<Section> removeSections) {
        final Deque<Section> currentSections = new ArrayDeque<>(removeSections);
        final Section leftSection = currentSections.pollFirst();
        final Section rightSection = currentSections.pollFirst();
        final Section newSection = leftSection.combine(rightSection);
        final int targetIndex = sections.indexOf(leftSection);

        sections.add(targetIndex, newSection);
    }

    public List<Station> collectAllStations() {
        final List<Station> stations = new ArrayList<>();

        stations.addAll(sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList()));

        stations.addAll(sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));

        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
