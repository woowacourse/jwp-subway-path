package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isHeadStation(final Station station) {
        return sections.isEmpty() || sections.get(0).getBeforeStation().equals(station);
    }

    public boolean isTailStation(final Station station) {
        return sections.get(sections.size() - 1).getNextStation().equals(station);
    }

    public Sections addHead(final Section section) {
        final ArrayList<Section> newSections = new ArrayList<>(sections);
        newSections.add(0, section);
        return new Sections(newSections);
    }

    public Sections addTail(final Section section) {
        final ArrayList<Section> newSections = new ArrayList<>(sections);
        newSections.add(sections.size(), section);
        return new Sections(newSections);
    }

    public Sections addCentral(final Section section) {
        final ArrayList<Section> newSections = new ArrayList<>(sections);

        final Section originSection = newSections.stream()
                .filter(element -> element.getBeforeStation().equals(section.getBeforeStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
        final int originIndex = newSections.indexOf(originSection);
        newSections.remove(originSection);
        newSections.add(originIndex, section);
        newSections.add(originIndex + 1,
                new Section(
                        section.getNextStation(),
                        originSection.getNextStation(),
                        originSection.getDistance().minusValue(section.getDistance())
                )
        );

        return new Sections(newSections);
    }

    public Sections removeHead() {
        final ArrayList<Section> newSections = new ArrayList<>(sections);
        newSections.remove(0);
        return new Sections(newSections);
    }

    public Sections removeTail() {
        final ArrayList<Section> newSections = new ArrayList<>(sections);
        newSections.remove(sections.size() - 1);
        return new Sections(newSections);
    }

    public Sections removeCentral(final Station station) {
        final ArrayList<Section> newSections = new ArrayList<>(sections);
        final Section beforeSection = newSections.stream()
                .filter(section -> section.getNextStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));

        final Section nextSection = newSections.stream()
                .filter(section -> section.getBeforeStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));

        final int index = newSections.indexOf(beforeSection);
        newSections.remove(beforeSection);
        newSections.remove(nextSection);
        final Section newSection = new Section(
                beforeSection.getBeforeStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        );
        newSections.add(index, newSection);
        return new Sections(newSections);
    }

    public Sections getDifferenceOfSet(final Sections otherSections) {
        final List<Section> result = new ArrayList<>(sections);
        result.removeAll(otherSections.getSections());
        return new Sections(result);
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
