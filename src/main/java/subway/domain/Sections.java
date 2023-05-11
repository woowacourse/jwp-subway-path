package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {
    private final long lineId;
    private final List<Section> sections;

    public Sections(final long lineId, final List<Section> sections) {
        this.lineId = lineId;
        this.sections = new ArrayList<>(sections);
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


    public boolean isUpEndAppend(final Section section) {
        Section upEndSection = getUpEndSection();

        return upEndSection.isPreviousContinuable(section);
    }

    public Section getDownEndSection() {
        return sections.stream().filter(section1 -> section1.getNextSectionId() == 0)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("하행 종점이 존재하지 않습니다"));
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

    public Section findSectionByNextSection(Section section) {
        return sections.stream()
                .filter(section1 -> section1.getNextSectionId().equals(section.getNextSectionId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("노선에 해당 구간이 존재하지 않습니다."));
    }

    public Section findSectionByUpStation(final long stationId) {
        return sections.stream().filter(section -> section.isSameUpStationId(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상행역을 가진 구간이 존재하지 않습니다."));
    }


    public Section findSectionByDownStation(final long stationId) {
        return sections.stream().filter(section -> section.isSameDownStationId(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 하행역을 가진 구간이 존재하지 않습니다."));
    }

    public boolean isNotExistStation(final long stationId) {
        return sections.stream()
                .noneMatch(section -> section.containsStation(stationId));
    }

    private List<Section> getSorted() {
        Map<Long, Section> sectionIdMapping = new HashMap<>();

        sections.forEach(section -> sectionIdMapping.put(section.getId(), section));
        Section upEndSection = getUpEndSection();
        Section currentSection = upEndSection;
        List<Section> result = new ArrayList<>();

        result.add(upEndSection);
        while (currentSection.getNextSectionId() != 0) {
            Section nextSection = sectionIdMapping.get(currentSection.getNextSectionId());
            result.add(nextSection);
            currentSection = nextSection;
        }

        return result;
    }

    public List<Station> getStationsInOrder() {
        List<Section> sorted = this.getSorted();

        if (sorted.size() == 0) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();

        stations.add(sorted.get(0).getUpStation());
        for (Section section : sorted) {
            stations.add(section.getDownStation());
        }
        return stations;
    }

    @Override
    public String toString() {
        return "Sections{" +
                "lineId=" + lineId +
                ", sections=" + sections +
                '}';
    }
}
