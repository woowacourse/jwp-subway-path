package subway.domain.section;

import subway.domain.station.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Sections {

    private static final String CAT_NOT_EXIST_FORK = "갈림길이 존재할 수 없습니다.";
    private final Map<Long, Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new HashMap<>();
        for (final Section section : sections) {
            this.sections.put(section.getLeftStation().getId(), section);
        }
        validateForkRoad(sections);
    }

    private void validateForkRoad(final List<Section> sections) {
        if (this.sections.size() == sections.size()) {
            return;
        }

        throw new IllegalArgumentException(CAT_NOT_EXIST_FORK);
    }

    public Map<Long, Section> getSections() {
        return sections;
    }

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void addSection(Section section) {
        int beforeSize = sections.size();
        sections.put(section.getLeftStation().getId(), section);
        validateForkRoadInstantly(beforeSize, sections.size());
    }

    private void validateForkRoadInstantly(int beforeSize, int nowSize) {
        if (beforeSize + 1 == nowSize) {
            return;
        }

        throw new IllegalArgumentException(CAT_NOT_EXIST_FORK);
    }

    public void split(Station newStation, Station leftBaseStation, int leftDistance, Station rightBaseStation, int rightDistance) {
        Section section = sections.get(leftBaseStation.getId());
        if (section.getRightStation().equals(rightBaseStation)) {
            Queue<Section> splitSections = section.split(newStation, leftDistance, rightDistance);
            sections.remove(leftBaseStation.getId());
            addSplitSections(splitSections);
            return;
        }

        throw new IllegalArgumentException("분할하려는 영역이 존재하지 않습니다.");
    }

    private void addSplitSections(Queue<Section> splitSections) {
        if (splitSections.size() != 2) {
            throw new IllegalStateException("분할된 영역은 2개여야 합니다.");
        }

        Section section = splitSections.remove();
        sections.put(section.getLeftStation().getId(), section);
        section = splitSections.remove();
        sections.put(section.getLeftStation().getId(), section);
    }

    public Section getSectionByLeftStation(Station station) {
        return sections.get(station.getId());
    }

    public void delete(Section section) {
        sections.remove(section.getLeftStation().getId());
    }

    public void mixSection(Section leftSection, Section rightSection) {
        validateLinkedSections(leftSection, rightSection);
        delete(leftSection);
        delete(rightSection);
        sections.put(leftSection.getLeftStation().getId(), new Section(leftSection.getLeftStation(), rightSection.getRightStation(), leftSection.getDistance().getDistance() + rightSection.getDistance().getDistance()));
    }

    private void validateLinkedSections(Section leftSection, Section rightSection) {
        if (leftSection.getRightStation().equals(rightSection.getLeftStation())) {
            return;
        }

        throw new IllegalArgumentException("연결되지 않은 구간을 합칠 수 없습니다.");
    }

    public Section getSectionByRightStation(Station deleteStation) {
        for (final Map.Entry<Long, Section> section : sections.entrySet()) {
            if (section.getValue().getRightStation().equals(deleteStation)) {
                return section.getValue();
            }
        }

        throw new IllegalArgumentException("지정한 역이 우측에 존재하는 영역이 없습니다.");
    }
}
