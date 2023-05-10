package subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    private final long lineId;
    private final List<Section> sections;

    public Sections(final long lineId, final List<Section> sections) {
        this.lineId = lineId;
        this.sections = sections;
    }

    public Section getIncludeSection(Section section) {
        Section includeSection = sections.stream()
                .filter(section1 -> section1.hasIntersection(section))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("구간의 중간에 등록될 수 있는 구간이 아닙니다."));

        if (includeSection.isDistanceSmallOrSame(section)) {
            throw new IllegalArgumentException("중간에 등록될 수 없는 길이의 구간입니다. 너무 큽니다.");
        }
        return includeSection;
    }

    public boolean isDownEndAppend(Section section) {
        Section downEndSection = getDownEndSection();

        return downEndSection.isNextContinuable(section);
    }

    public Section getDownEndSection() {
        return sections.stream().filter(section1 -> section1.getNextSectionId() == 0)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("하행 종점이 존재하지 않습니다"));
    }

    public boolean isUpEndAppend(final Section section) {
        Section upEndSection = getUpEndSection();

        return upEndSection.isPreviousContinuable(section);
    }

    public Section getUpEndSection() {
        List<Long> downSectionIds = sections.stream().map(Section::getNextSectionId)
                .collect(Collectors.toList());

        return sections.stream().filter(section1 -> !downSectionIds.contains(section1.getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("상행 종점이 존재하지 않습니다."));
    }

    public boolean isInitialSave() {
        return sections.isEmpty();
    }

    public int size() {
        return this.sections.size();
    }

    public long getLineId() {
        return lineId;
    }

    public List<Section> getSections() {
        return sections;
    }
}
