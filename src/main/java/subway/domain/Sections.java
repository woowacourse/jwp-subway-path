package subway.domain;

import java.util.List;

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

    public void addHead(final Section section) {
        sections.add(0, section);
    }

    public void addTail(final Section section) {
        sections.add(sections.size(), section);
    }

    public void addCentral(final Section section) {
        final Section originSection = sections.stream()
                .filter(node -> node.getBeforeStation().equals(section.getBeforeStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
        final int originIndex = sections.indexOf(originSection);
        sections.remove(originSection);
        sections.add(originIndex, section);
        sections.add(originIndex + 1,
                new Section(
                        section.getNextStation(),
                        originSection.getNextStation(),
                        originSection.getDistance().minusValue(section.getDistance())
                )
        );
    }

    public void removeHead() {
        sections.remove(0);
    }

    public void removeTail() {
        sections.remove(sections.size() - 1);
    }

    public void removeCentral(final Station station) {
        final Section beforeSection = sections.stream()
                .filter(section -> section.getNextStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));

        final Section nextSection = sections.stream()
                .filter(section -> section.getBeforeStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));

        final int index = sections.indexOf(beforeSection);
        sections.remove(beforeSection);
        sections.remove(nextSection);
        final Section newSection = new Section(
                beforeSection.getBeforeStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        );
        sections.add(index, newSection);
    }
}
