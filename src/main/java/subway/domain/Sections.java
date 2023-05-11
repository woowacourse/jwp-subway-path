package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Sections updateArrival(Station arrival, Section section) {
        Section found = getSameLineAndArrivalIs(arrival, section);

        ArrayList<Section> update = new ArrayList<>(sections);
        update.remove(found);
        update.add(section);
        return new Sections(update);
    }

    private Section getSameLineAndArrivalIs(Station arrival, Section other) {
        return sections.stream()
                .filter(section -> section.LineEquals(other))
                .filter(section -> section.isArrival(arrival))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("같은라인에 arrival에 해당하는 section이 없습니다"));
    }

    public void validateDistanceBetween(Station arrival, Section s1, Section s2) {
        Section section = getSectionTo(arrival);
        if (section.getDistance() != s1.getDistance() + s2.getDistance()) {
            throw new IllegalArgumentException("요청의 이전역과 다음역 사이의 거리가 잘못되었습니다.");
        }
    }

    private Section getSectionTo(Station arrival) {
        return sections.stream()
                .filter(section -> section.isArrival(arrival))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("두 역이 연결되어 있지 않습니다."));
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean hasSameLine(Section other) {
        return sections.stream()
                .anyMatch(section -> section.LineEquals(other));
    }

    public Sections addSection(Section section) {
        ArrayList<Section> added = new ArrayList<>(sections);
        added.add(section);
        return new Sections(added);
    }

    public int countSameLine(Section other) {
        return (int) sections.stream()
                .filter(section -> section.LineEquals(other))
                .count();
    }
}
