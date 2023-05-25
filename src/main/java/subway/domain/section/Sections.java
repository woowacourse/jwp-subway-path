package subway.domain.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.station.Station;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    // TODO: sections 초기화시 구간이 연속되는지 validate
    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        Direction direction = Direction.of(sections, section);

        if (isEndPoint(section)) {
            sections.add(section);
            return;
        }

        divideSection(direction, section);
    }

    private boolean isEndPoint(final Section section) {
        Station upEndPoint = getUpEndPoint();
        Station downEndPoint = getDownEndPoint();
        return section.getUpStation().equals(downEndPoint) || section.getDownStation().equals(upEndPoint);
    }

    private Station getUpEndPoint() {
        return getSubtractionStation(getUpStations(), getDownStations());
    }

    private Station getDownEndPoint() {
        return getSubtractionStation(getDownStations(), getUpStations());
    }

    private Station getSubtractionStation(List<Station> minuendStations, List<Station> subtrahendStations) {
        final int UP_DOWN_DIFF_SIZE = 1;

        minuendStations.removeAll(subtrahendStations);
        if (minuendStations.size() != UP_DOWN_DIFF_SIZE) {
            throw new IllegalStateException("Section이 이어지지 않은 상태입니다.");
        }
        return minuendStations.get(0);
    }

    private List<Station> getUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private void divideSection(final Direction direction, final Section section) {
        Station originStation = getOriginStation(direction, section);
        Section originSection = getOriginSection(direction, originStation);

        int oldDistance = originSection.getDistance();
        int newDistance = section.getDistance();

        if (oldDistance <= newDistance) {
            throw new SubwayIllegalArgumentException("거리는 기존 구간보다 짧아야합니다.");
        }

        List<Section> newSections = new ArrayList<>();
        newSections.add(section);
        newSections.add(getDividedSection(direction, originSection, section, oldDistance - newDistance));

        sections.remove(originSection);
        sections.addAll(newSections);
    }

    private Station getOriginStation(final Direction direction, final Section section) {
        if (direction == Direction.UP) {
            return section.getDownStation();
        }
        return section.getUpStation();
    }

    private Section getOriginSection(final Direction direction, final Station station) {
        return sections.stream()
                .filter(it -> getBaseStation(it, direction).equals(station))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private Station getBaseStation(final Section section, final Direction direction) {
        if (direction == Direction.UP) {
            return section.getDownStation();
        }
        return section.getUpStation();
    }

    private Section getDividedSection(final Direction direction, final Section originSection, final Section newSection,
            final int dividedDistance) {
        if (direction == Direction.UP) {
            return new Section(originSection.getUpStation(), newSection.getUpStation(), dividedDistance);
        }
        return new Section(newSection.getDownStation(), originSection.getDownStation(), dividedDistance);
    }


    public void remove(final Station station) {
        final int ONLY_SECTION = 1;

        if (sections.isEmpty()) {
            throw new SubwayIllegalArgumentException("해당 역이 노선에 존재하지 않습니다.");
        }

        List<Section> stationSections = sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .collect(Collectors.toList());

        if (stationSections.isEmpty()) {
            throw new SubwayIllegalArgumentException("해당 역이 노선에 존재하지 않습니다.");
        }

        if (stationSections.size() == ONLY_SECTION) {
            sections.remove(stationSections.get(0));
            return;
        }

        Section upSection = stationSections.stream()
                .filter(section -> station.equals(section.getDownStation()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
        Section downSection = stationSections.stream()
                .filter(section -> station.equals(section.getUpStation()))
                .findAny()
                .orElseThrow(IllegalStateException::new);

        Section newSection = new Section(upSection.getUpStation(), downSection.getDownStation(),
                upSection.getDistance() + downSection.getDistance());

        sections.add(newSection);
        sections.remove(upSection);
        sections.remove(downSection);
    }

    public List<Station> findOrderedStation() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Station, Station> upToDown = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));

        Station upEndPoint = getUpEndPoint();

        List<Station> orderedStations = new ArrayList<>();
        Station now = upEndPoint;

        while (now != null) {
            orderedStations.add(now);
            now = upToDown.get(now);
        }

        return orderedStations;
    }

    public boolean contains(final Section section) {
        return sections.contains(section);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    private enum Direction {
        UP,
        DOWN;

        public static Direction of(final List<Section> sections, final Section section) {
            boolean isUpward = contains(sections, section.getDownStation());
            boolean isDownward = contains(sections, section.getUpStation());

            if (isUpward && isDownward) {
                throw new SubwayIllegalArgumentException("이미 존재하는 구간입니다.");
            }
            if (!isUpward && !isDownward) {
                throw new SubwayIllegalArgumentException("하나의 역은 반드시 노선에 존재해야합니다.");
            }

            if (isUpward) {
                return UP;
            }
            return DOWN;
        }

        private static boolean contains(final List<Section> sections, final Station station) {
            return sections.stream()
                    .anyMatch(section -> section.contains(station));
        }
    }

}
