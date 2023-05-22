package subway.domain.section;

import subway.domain.station.Station;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sort(sections);
    }

    // TODO: 정렬 로직 리팩터링
    private static List<Section> sort(final List<Section> sections) {
        if (sections.size() == 0) {
            return sections;
        }

        final List<Section> sortedSections = new LinkedList<>();
        sortedSections.add(sections.get(0));
        while (sortedSections.size() < sections.size()) {
            final Station head = sortedSections.get(0).getBeforeStation();
            final Station tail = sortedSections.get(sortedSections.size() - 1).getNextStation();
            for (final Section section : sections) {
                if (section.getNextStation().equals(head)) {
                    sortedSections.add(0, section);
                    break;
                } else if (section.getBeforeStation().equals(tail)) {
                    sortedSections.add(section);
                    break;
                }
            }
        }

        return sortedSections;
    }

    public boolean isHeadStation(final Station station) {
        return sections.isEmpty() || sections.get(0).getBeforeStation().equals(station);
    }

    public boolean isTailStation(final Station station) {
        return !sections.isEmpty() && sections.get(sections.size() - 1).getNextStation().equals(station);
    }

    public Sections addHead(final Section newSection) {
        validateDuplicate(newSection);
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(0, newSection);
        return new Sections(newSections);
    }

    public Sections addTail(final Section newSection) {
        validateDuplicate(newSection);
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(sections.size(), newSection);
        return new Sections(newSections);
    }

    public Sections addCentral(final Section newSection) {
        validateDuplicate(newSection);
        final List<Section> copiedSections = new LinkedList<>(sections);
        final Section originSection = findOriginSection(newSection, copiedSections);
        final int originIndex = copiedSections.indexOf(originSection);

        copiedSections.remove(originIndex);
        copiedSections.add(originIndex, newSection);
        copiedSections.add(originIndex + 1,
                new Section(
                        newSection.getNextStation(),
                        originSection.getNextStation(),
                        originSection.getDistance().minusValue(newSection.getDistance())
                )
        );

        return new Sections(copiedSections);
    }

    private void validateDuplicate(final Section newSection) {
        if (newSection.getBeforeStation().equals(newSection.getNextStation())) {
            throw new IllegalArgumentException("이전 역과 다음 역은 동일할 수 없습니다.");
        }
        if (isExist(newSection.getBeforeStation()) && isExist(newSection.getNextStation())) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }

    private boolean isExist(final Station station) {
        return !sections.isEmpty() && (
                sections.get(0).getBeforeStation().equals(station) ||
                        sections.stream().anyMatch(section -> section.getNextStation().equals(station))
        );
    }

    private Section findOriginSection(final Section newSection, final List<Section> sections) {
        return sections.stream()
                .filter(element -> element.getBeforeStation().equals(newSection.getBeforeStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
    }

    public Sections removeHead() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(0);
        return new Sections(newSections);
    }

    public Sections removeTail() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(sections.size() - 1);
        return new Sections(newSections);
    }

    public Sections removeCentral(final Station station) {
        final List<Section> copiedSections = new LinkedList<>(sections);
        final Section beforeSection = findBeforeSection(station, copiedSections);
        final Section nextSection = findNextSection(station, copiedSections);
        final int originBeforeSectionIndex = copiedSections.indexOf(beforeSection);

        copiedSections.remove(beforeSection);
        copiedSections.remove(nextSection);
        copiedSections.add(originBeforeSectionIndex,
                new Section(
                        beforeSection.getBeforeStation(),
                        nextSection.getNextStation(),
                        beforeSection.getDistance().plusValue(nextSection.getDistance())
                )
        );

        return new Sections(copiedSections);
    }

    private Section findBeforeSection(final Station station, final List<Section> sections) {
        return sections.stream()
                .filter(section -> section.getNextStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private Section findNextSection(final Station station, final List<Section> sections) {
        return sections.stream()
                .filter(section -> section.getBeforeStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    public Sections getDifferenceOfSet(final Sections otherSections) {
        final List<Section> result = new LinkedList<>(sections);
        result.removeAll(otherSections.getSections());
        return new Sections(result);
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
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
