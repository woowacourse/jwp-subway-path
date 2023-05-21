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

        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        boolean isUpExists = sections.stream().anyMatch(it -> it.contains(upStation));
        boolean isDownExists = sections.stream().anyMatch(it -> it.contains(downStation));

        validateSection(isUpExists, isDownExists);

        if (isUpExists && !isUpEndPoint(upStation)) {
            replaceSection(upStation, section);
        }

        if (isDownExists && !isDownEndPoint(downStation)) {
            replaceSection(downStation, section);
        }

        sections.add(section);
    }

    private boolean isUpEndPoint(final Station upStation) {
        return sections.stream()
                .noneMatch((section -> section.getUpStation().equals(upStation)));
    }

    private boolean isDownEndPoint(final Station downStation) {
        return sections.stream()
                .noneMatch((section -> section.getDownStation().equals(downStation)));
    }

    private void validateSection(boolean isUpExists, boolean isDownExists) {
        if (isUpExists && isDownExists) {
            throw new ApiIllegalArgumentException("이미 존재하는 구간입니다.");
        }

        if (!isUpExists && !isDownExists) {
            throw new ApiIllegalArgumentException("하나의 역은 반드시 노선에 존재해야합니다.");
        }
    }

    private void replaceSection(final Station station, final Section section) {
        Section originSection = sections.stream()
                .filter(it -> it.contains(station))
                .findAny()
                .orElseThrow(IllegalStateException::new);

        int oldDistance = originSection.getDistance();
        int newDistance = section.getDistance();

        sections.remove(originSection);

        if (originSection.getDownStation().equals(section.getDownStation())) {
            sections.add(new Section(originSection.getUpStation(), section.getUpStation(), oldDistance - newDistance));
            return;
        }

        sections.add(new Section(section.getDownStation(), originSection.getDownStation(), oldDistance - newDistance));
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

    public Map<Station, Station> getStationUpToDown() {
        return sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
