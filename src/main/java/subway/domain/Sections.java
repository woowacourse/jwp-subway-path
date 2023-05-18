package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {
    private static final int ONE_SECTION = 1;
    private static final int END_POINT_SIZE = 1;
    private static final int FIND_END_POINT_INDEX = 0;
    private static final int ORIGIN_INDEX = 0;
    private List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void addSection(final Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        isSectionDuplicate(newSection);

        if (newSection.validateEqualEndPoint(findUpEndPoint(),findDownEndPoint())) {
            sections.add(newSection);
            return;
        }

        addMiddleSection(newSection);
    }

    private void addMiddleSection(final Section newSection) {
        Section originSection = getOriginSection(newSection);
        sections.remove(originSection);

        if (originSection.validateDistance(newSection)) {
            throw new IllegalArgumentException("새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같을 수 없습니다.");
        }

        originSection.changeSection(newSection);
        sections.add(originSection);
        sections.add(newSection);
    }

    private Station findUpEndPoint() {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();

        for (Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        upStations.removeAll(downStations);

        return upStations.get(FIND_END_POINT_INDEX);
    }

    private Station findDownEndPoint() {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();

        for (Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        downStations.removeAll(upStations);

        return downStations.get(FIND_END_POINT_INDEX);
    }

    public void remove(final Station station) {
        validateStationExistence(station);

        if (sections.size() == ONE_SECTION) {
            sections.clear();
            return;
        }

        final List<Section> stationSection = getStationSection(station);
        if (stationSection.size() == END_POINT_SIZE) {
            sections.removeAll(stationSection);
            return;
        }

        removeMiddleStation(stationSection, station);
    }

    private void validateStationExistence(final Station station) {
        if (sections.stream()
                .noneMatch(section -> section.hasStation(station))) {
            throw new IllegalArgumentException("삭제할 역이 존재하지 않습니다.");
        }
    }

    private void removeMiddleStation(final List<Section> stationSection, final Station station) {
        Section section = stationSection.get(ORIGIN_INDEX).mergedSection(stationSection.get(ORIGIN_INDEX + 1), station);

        sections.removeAll(stationSection);
        sections.add(section);
    }

    private List<Section> getStationSection(final Station station) {
        return sections.stream()
                .filter(section -> section.hasStation(station))
                .collect(Collectors.toList());
    }

    private void isSectionDuplicate(final Section newSection) {
        if (sections.stream()
                .anyMatch(section -> section.validateDuplicateSection(newSection))) {
            throw new IllegalArgumentException("이미 존재하는 구간입니다.");
        }
    }

    private Section getOriginSection(final Section newSection) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(newSection.getUpStation()) || section.getDownStation().equals(newSection.getDownStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("접하는 역이 없습니다."));
    }

    public List<Station> getSortedStations() {
        Map<Station, Station> upToDown = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));

        List<Station> orderedStations = new ArrayList<>();
        Station now = findUpEndPoint();

        while (now != null) {
            orderedStations.add(now);
            now = upToDown.get(now);
        }

        return orderedStations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
