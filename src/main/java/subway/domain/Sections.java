package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    private static final int ONE_SECTION = 1;
    private static final int END_POINT_SIZE = 1;
    private List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(final Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        isSectionDuplicate(newSection);

        final List<Station> endPoint = findEndPoint();

        if (newSection.validateEqualEndPoint(endPoint)) {
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

    private List<Station> findEndPoint() {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();

        for (Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        List<Station> upTemp = new ArrayList<>(upStations);
        upStations.removeAll(downStations);
        downStations.removeAll(upTemp);

        return List.of(upStations.get(0), downStations.get(0));
    }

    public void remove(final Station station) {
        validateStationExistence(station);

        if (sections.size() == ONE_SECTION) {
            sections.clear();
        }

        final List<Section> stationSection = getStationSection(station);
        if (stationSection.size() == END_POINT_SIZE) {
            sections.removeAll(stationSection);
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
        Section section = stationSection.get(0).mergedSection(stationSection.get(1), station);

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
        Station nowStation = findEndPoint().get(0);
        List<Station> sortStation = new ArrayList<>();
        sortStation.add(nowStation);

        for (int j = 0; j < sections.size(); j++) {
            for (Section section : sections) {
                if (section.isNowStation(nowStation)) {
                    sortStation.add(section.getDownStation());
                    nowStation = section.getDownStation();
                }
            }
        }
        return sortStation;
    }

    public List<Section> getSections() {
        return sections;
    }
}
