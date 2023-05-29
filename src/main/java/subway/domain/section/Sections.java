package subway.domain.section;

import static subway.domain.section.Location.END;
import static subway.domain.section.Location.MIDDLE;
import static subway.domain.section.Location.NONE;
import static subway.domain.section.Status.INCLUDED;
import static subway.domain.section.Status.INIT;
import static subway.domain.section.Status.NOT_INCLUDED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.domain.station.Station;

public class Sections {
    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public Sections() {
        this(new ArrayList<>());
    }

    public void addSection(Section section) {
        validateExistAll(section);
        validateNotExistAll(section);

        if (sections.size() == 0) {
            sections.add(section);
            return;
        }

        Station includedStation = getIncludedStation(section);
        List<Section> sections = findSectionBy(includedStation);
        addByDirection(section, includedStation, sections);
    }

    private void validateExistAll(final Section section) {
        if (checkStationStatus(section.getUpStation()) == INCLUDED
                && checkStationStatus(section.getDownStation()) == INCLUDED) {
            throw new IllegalArgumentException("해당 노선에 이미 두 역 모두 존재합니다.");
        }
    }

    private void validateNotExistAll(final Section section) {
        if (checkStationStatus(section.getUpStation()) == NOT_INCLUDED
                && checkStationStatus(section.getDownStation()) == NOT_INCLUDED) {
            throw new IllegalArgumentException("해당 노선에 두 역 모두 존재하지 않습니다.");
        }
    }

    private void addByDirection(final Section section, final Station includedStation, final List<Section> sections) {
        if (checkStationLocation(includedStation) == END && section.isConnectableDifferentDirection(sections.get(0))) {
            this.sections.add(section);
            return;
        }

        addIfSameDirectionSection(section, sections);
    }

    private Station getIncludedStation(final Section section) {
        if (checkStationStatus(section.getUpStation()) == NOT_INCLUDED) {
            return section.getDownStation();
        }
        return section.getUpStation();
    }

    private void addIfSameDirectionSection(final Section section, final List<Section> sections) {
        Section rawSection = getSameDirectionSection(section, sections);
        validateDistance(section, rawSection);

        if (section.equalsUpStation(rawSection)) {
            this.sections.add(Section.of(section.getDownStation(),
                    rawSection.getDownStation(),
                    rawSection.getDistance().minus(section.getDistance())));
            this.sections.remove(rawSection);
        }

        if (section.equalsDownStation(rawSection)) {
            this.sections.add(Section.of(rawSection.getUpStation(),
                    section.getUpStation(),
                    rawSection.getDistance().minus(section.getDistance())));
            this.sections.remove(rawSection);
        }

        this.sections.add(section);
    }

    private void validateDistance(final Section section, final Section rawSection) {
        if (!rawSection.getDistance().greaterThan(section.getDistance())) {
            throw new IllegalArgumentException("해당 구간의 거리가 너무 큽니다.");
        }
    }

    public Status checkStationStatus(Station station) {
        List<Section> sections = findSectionBy(station);
        if (this.sections.isEmpty()) {
            return INIT;
        }

        if (sections.isEmpty()) {
            return NOT_INCLUDED;
        }

        return INCLUDED;
    }

    private Section getSameDirectionSection(Section section, List<Section> sections) {
        return sections.stream()
                .filter(section2 -> section2.equalsUpStation(section) || section2.equalsDownStation(section))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("동일한 위치의 구간이 아닙니다."));
    }

    public void deleteSectionByStation(Station station) {
        Location location = checkStationLocation(station);
        validateStation(location);

        List<Section> sections = findSectionBy(station);

        if (location == MIDDLE) {
            Section connectedSection = createConnectedSection(sections.get(0), sections.get(1));
            this.sections.add(connectedSection);
        }
        this.sections.removeAll(sections);
    }

    private void validateStation(final Location location) {
        if (location == NONE) {
            throw new IllegalArgumentException("해당 노선에 존재하는 역이 아닙니다.");
        }
    }

    private Section createConnectedSection(final Section section, final Section other) {
        Station upStation = section.getUpStation();
        Station downStation = other.getDownStation();

        if (other.isNextSection(section)) {
            upStation = other.getUpStation();
            downStation = section.getDownStation();
        }

        Distance calculatedDistance = section.getDistance().add(other.getDistance());
        return Section.of(upStation, downStation, calculatedDistance);
    }

    public Location checkStationLocation(Station station) {
        List<Section> sections = findSectionBy(station);
        int sectionsSize = sections.size();

        if (sectionsSize == 0) {
            return NONE;
        }

        if (sectionsSize == 1) {
            return END;
        }

        return MIDDLE;
    }

    public List<Section> findSectionBy(Station station) {
        return sections.stream()
                .filter(section -> section.contains(station))
                .collect(Collectors.toList());
    }

    public List<Station> findAllStation() {
        Map<Station, Section> allSections = new HashMap<>();
        sections.forEach(section -> allSections.put(section.getUpStation(), section));

        return getSortedStations(allSections);
    }

    private List<Station> getSortedStations(final Map<Station, Section> allSections) {
        List<Station> stations = new ArrayList<>();
        Section currentSection = findUpEndSection();
        Station currentStation = currentSection.getDownStation();
        stations.add(currentSection.getUpStation());
        stations.add(currentSection.getDownStation());

        while (allSections.containsKey(currentStation)) {
            Section nextSection = allSections.get(currentStation);
            currentStation = nextSection.getDownStation();
            stations.add(currentStation);
        }

        return stations;
    }

    private Section findUpEndSection() {
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return sections.stream()
                .filter(section1 -> !downStations.contains(section1.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상행 종점이 존재하지 않습니다."));
    }

    public Station getNotIncludedStation(Section section) {
        validateExistAll(section);
        validateNotExistAll(section);

        if (checkStationLocation(section.getUpStation()) == NONE) {
            return section.getUpStation();
        }
        return section.getDownStation();
    }
    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
