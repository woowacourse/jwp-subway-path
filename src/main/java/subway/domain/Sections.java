package subway.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sections {

    private final List<Section> oldSections = new ArrayList<>();

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
        return !oldSections.isEmpty();
    }

    private boolean isBaseStationNotExist(final Section newSection) {
        return oldSections.stream().noneMatch(section -> section.isBaseStationExist(newSection));
    }

    private boolean isAlreadyExist(final Section newSection) {
        return oldSections.contains(newSection);
    }

    private void addSectionBy(final Section newSection) {
        final Optional<Section> maybeOldInnerSection = findOldInnerSection(newSection);
        final Optional<Section> maybeOldOuterSection = findOldOuterSection(newSection);

        if (maybeOldInnerSection.isPresent()) {
            addSectionAfterRemoveOld(newSection, maybeOldInnerSection.get());
            return;
        }
        if (maybeOldOuterSection.isPresent()) {
            addSectionAfterRemoveOld(newSection, maybeOldOuterSection.get());
            return;
        }
        oldSections.add(newSection);
    }

    private Optional<Section> findOldInnerSection(final Section newSection) {
        return oldSections.stream()
                .dropWhile(oldSection -> isNotInnerSection(newSection, oldSection))
                .findFirst();
    }

    private boolean isNotInnerSection(final Section newSection, final Section oldSection) {
        return !(oldSection.isSameDownStationBy(newSection.getDownStation())
                || oldSection.isSameUpStationBy(newSection.getUpStation()));
    }

    private Optional<Section> findOldOuterSection(final Section newSection) {
        return oldSections.stream()
                .dropWhile(oldSection -> isNotOuterSection(newSection, oldSection))
                .findFirst();
    }

    private boolean isNotOuterSection(final Section newSection, final Section oldSection) {
        return !(oldSection.isSameDownStationBy(newSection.getUpStation())
                || oldSection.isSameUpStationBy(newSection.getDownStation()));
    }

    private void addSectionAfterRemoveOld(final Section newSection, final Section oldSection) {
        final int oldSectionIndex = oldSections.indexOf(oldSection);
        oldSections.remove(oldSection);
        oldSections.addAll(oldSectionIndex, oldSection.divide(newSection));
    }

    public void removeStation(final Station removeStation) {
        final List<Section> removeSections = collectRemoveSections(removeStation);
        if (isStationBetween(removeSections)) {
            addCombineSection(removeSections);
        }
        oldSections.removeAll(removeSections);
    }

    private List<Section> collectRemoveSections(final Station removeStation) {
        return oldSections.stream()
                .filter(oldSection -> oldSection.isSameUpStationBy(removeStation) || oldSection.isSameDownStationBy(removeStation))
                .collect(Collectors.toList());
    }

    private boolean isStationBetween(final List<Section> removeSections) {
        return removeSections.size() == 2;
    }

    private void addCombineSection(final List<Section> removeSections) {
        final Deque<Section> currentSections = new ArrayDeque<>(removeSections);
        final Section leftSection = currentSections.pollFirst();
        final Section rightSection = currentSections.pollFirst();
        final Section newSection = leftSection.merge(rightSection);
        final int targetIndex = oldSections.indexOf(leftSection);

        oldSections.add(targetIndex, newSection);
    }

    public List<Station> collectAllStations() {
        return oldSections.stream()
                .map(oldSection -> Stream.of(oldSection.getUpStation(), oldSection.getDownStation()))
                .flatMap(Stream::distinct)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getOldSections() {
        return new ArrayList<>(oldSections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sections sections1 = (Sections) o;
        return Objects.equals(oldSections, sections1.oldSections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldSections);
    }
}
