package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Sections {
    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Sections updateArrival(final Station arrival, final Section section) {
        final Section found = getSameLineAndArrivalIs(arrival, section);

        final ArrayList<Section> update = new ArrayList<>(sections);
        update.remove(found);
        update.add(section);
        return new Sections(update);
    }

    private Section getSameLineAndArrivalIs(final Station arrival, final Section other) {
        return sections.stream()
                .filter(section -> section.LineEquals(other))
                .filter(section -> section.isArrival(arrival))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("같은라인에 arrival에 해당하는 section이 없습니다"));
    }

    public int thisToNextDistance(final Station next, final Section thisToPrev) {
        final Section prevToNext = getSectionTo(next);
        validateDistanceBetween(prevToNext, thisToPrev);

        return prevToNext.getDistance() - thisToPrev.getDistance();
    }

    private void validateDistanceBetween(final Section prevToNext, final Section thisToNext) {
        if (prevToNext.getDistance() - thisToNext.getDistance() < 2) {
            throw new IllegalArgumentException("요청의 이전역과 다음역 사이의 거리가 잘못되었습니다.");
        }
    }

    private Section getSectionTo(final Station arrival) {
        return sections.stream()
                .filter(section -> section.isArrival(arrival))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("두 역이 연결되어 있지 않습니다."));
    }

    public int countSectionByLine(final Line line) {
        return (int) sections.stream()
                .filter(section -> section.getLine().equals(line))
                .count();
    }

    public List<Section> getSameLineSections(final Line line) {
        return sections.stream()
                .filter(section -> section.getLine().equals(line))
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean hasSameLine(final Section other) {
        return sections.stream()
                .anyMatch(section -> section.LineEquals(other));
    }

    public Section getSameLineSection(final Line line) {
        return sections.stream()
                .filter(section -> section.getLine().equals(line))
                .findAny()
                .orElseThrow();
    }

    public Sections addSection(final Section section) {
        final ArrayList<Section> added = new ArrayList<>(sections);
        added.add(section);
        return new Sections(added);
    }

    public int countSameLine(final Section other) {
        return (int) sections.stream()
                .filter(section -> section.LineEquals(other))
                .count();
    }

    public Set<Line> getLinesIncludingStation() {
        return sections.stream()
                .map(Section::getLine)
                .collect(Collectors.toSet());
    }

    public Sections removeArrival(final Station station) {
        final List<Section> removed = new ArrayList<>(sections);
        final Section section = removed.stream()
                .filter(s -> s.isArrival(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("section의 도착역이 아닙니다"));
        removed.remove(section);
        return new Sections(removed);
    }

    public Map<Station, Sections> mapToArrivalAndReverseSections() {
        return sections.stream()
                .collect(Collectors.toMap(
                        Section::getArrival,
                        section -> reverseAll().removeArrival(section.getArrival())
                ));

    }


    public Sections reverseAll() {
        return new Sections(sections.stream()
                .map(Section::getReverse)
                .collect(Collectors.toList())
        );
    }

    public Map<Station, Section> reverseMap() {
        return sections.stream()
                .map(Section::getReverse)
                .collect(Collectors.toMap(
                        Section::getDeparture,
                        Function.identity()
                )
        );
    }

    public List<Station> getArrivals() {
        return sections.stream()
                .map(Section::getArrival)
                .collect(Collectors.toList());
    }

    public Sections remove(Section section) {
        ArrayList<Section> sections1 = new ArrayList<>(sections);
        sections1.remove(section);
        return new Sections(sections1);
    }
}
