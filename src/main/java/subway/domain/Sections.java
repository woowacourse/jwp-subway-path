package subway.domain;

import subway.exception.ApiIllegalArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {

        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        Map<Station, Station> upToDown = getStationUpToDown();

        boolean isUpExists = isExists(upToDown, section.getUpStation());
        boolean isDownExists = isExists(upToDown, section.getDownStation());
        if (isUpExists && isDownExists) {
            throw new ApiIllegalArgumentException("이미 존재하는 구간입니다.");
        }
        if (!isUpExists && !isDownExists) {
            throw new ApiIllegalArgumentException("하나의 역은 반드시 노선에 존재해야합니다.");
        }

        if (isUpExists) {
            Station existStation = section.getUpStation();

            if (!upToDown.containsKey(existStation)) {
                sections.add(section);
                return;
            }

            Section originSection = sections.stream()
                    .filter(it -> it.getUpStation().equals(existStation))
                    .findAny()
                    .orElseThrow(IllegalStateException::new);

            int oldDistance = originSection.getDistance();
            int newDistance = section.getDistance();

            if (oldDistance <= newDistance) {
                throw new ApiIllegalArgumentException("거리는 기존 구간보다 짧아야합니다.");
            }

            sections.add(section);
            sections.add(
                    new Section(section.getDownStation(), originSection.getDownStation(), oldDistance - newDistance));
            sections.remove(originSection);

        }

        if (isDownExists) {
            Station existStation = section.getDownStation();

            if (!upToDown.containsValue(existStation)) {
                sections.add(section);
                return;
            }

            Section originSection = sections.stream()
                    .filter(it -> it.getDownStation().equals(existStation))
                    .findAny()
                    .orElseThrow(IllegalStateException::new);

            int oldDistance = originSection.getDistance();
            int newDistance = section.getDistance();

            if (oldDistance <= newDistance) {
                throw new ApiIllegalArgumentException("거리는 기존 구간보다 짧아야합니다.");
            }

            sections.add(section);
            sections.add(new Section(originSection.getUpStation(), section.getUpStation(), oldDistance - newDistance));
            sections.remove(originSection);

        }
    }

    private boolean isExists(final Map<Station, Station> upToDown, final Station station) {
        return upToDown.containsKey(station) || upToDown.containsValue(station);
    }

    public void remove(final Station station) {
        if (sections.isEmpty()) {
            throw new ApiIllegalArgumentException("해당 역이 노선에 존재하지 않습니다.");
        }

        List<Section> stationSections = sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .collect(Collectors.toList());

        if (stationSections.isEmpty()) {
            throw new ApiIllegalArgumentException("해당 역이 노선에 존재하지 않습니다.");
        }

        if (stationSections.size() == 1) {
            sections.remove(stationSections.get(0));
            return;
        }

        Section section1 = stationSections.stream()
                .filter(section -> station.equals(section.getDownStation()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
        Section section2 = stationSections.stream()
                .filter(section -> station.equals(section.getUpStation()))
                .findAny()
                .orElseThrow(IllegalStateException::new);

        Section newSection = new Section(section1.getUpStation(), section2.getDownStation(),
                section1.getDistance() + section2.getDistance());

        sections.add(newSection);

        sections.remove(section1);
        sections.remove(section2);
    }

    public List<Station> findOrderedStation() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Station, Station> upToDown = getStationUpToDown();

        List<Station> downStations = new ArrayList<>(upToDown.values());
        Station upEndPoint = upToDown.keySet().stream()
                .filter(upStation -> !downStations.contains(upStation))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        List<Station> orderedStations = new ArrayList<>();
        Station now = upEndPoint;

        while (now != null) {
            orderedStations.add(now);
            now = upToDown.get(now);
        }

        return orderedStations;
    }

    private Map<Station, Station> getStationUpToDown() {
        return sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
