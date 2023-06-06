package subway.domain.subwaymap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import subway.exception.custom.LineDoesNotContainStationException;
import subway.exception.custom.SectionDistanceTooLongException;
import subway.exception.custom.StartStationNotExistException;
import subway.exception.custom.StationNotExistException;

public final class Sections {

    private final Map<Station, Section> sections;

    private Sections(final Map<Station, Section> sections) {
        this.sections = sections;
    }

    public static Sections of(final List<Section> sections) {
        SectionsValidator.validate(sections);
        final Map<Station, Section> classifiedSections = sections.stream()
            .collect(Collectors.toMap(Section::getUpStation, section -> section));
        return new Sections(classifiedSections);
    }

    public void addSection(final Station baseStation, final Section section) {
        if (sections.isEmpty() || isStartStation(section.getDownStation()) || isEndStation(section.getUpStation())) {
            extendSection(section);
            return;
        }
        divideSection(baseStation, section);
    }

    private boolean isStartStation(final Station station) {
        return getStartStation().map(value -> value.equals(station))
            .orElse(false);
    }

    private Optional<Station> getStartStation() {
        final List<Station> upStations = new ArrayList<>(sections.keySet());
        final List<Station> downStations = sections.values()
            .stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        upStations.removeAll(downStations);
        return upStations.stream().findAny();
    }

    private boolean isEndStation(final Station station) {
        return getEndStation().map(value -> value.equals(station))
            .orElse(false);
    }

    private Optional<Station> getEndStation() {
        final List<Station> upStations = new ArrayList<>(Objects.requireNonNull(sections.keySet()));
        final List<Station> downStations = sections.values()
            .stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        downStations.removeAll(upStations);
        return downStations.stream().findAny();
    }

    private void extendSection(final Section section) {
        sections.put(section.getUpStation(), section);
    }

    private void divideSection(final Station baseStation, final Section section) {
        validateExistStation(baseStation);

        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        if (upStation.equals(baseStation)) {
            divideWhenBaseStationInUp(upStation, downStation, section);
        }
        if (downStation.equals(baseStation)) {
            divideWhenBaseStationInDown(upStation, downStation, section);
        }

        sections.put(upStation, section);
    }

    private void validateExistStation(final Station station) {
        if (!getStations().contains(station)) {
            throw new StationNotExistException(String.format("역이 노선에 존재하지 않습니다 ( 존재하지 않는 역: %s )", station.getName()));
        }
    }

    private void divideWhenBaseStationInUp(final Station upStation, final Station downStation, final Section section) {
        final Station prevDownStation = sections.get(upStation).getDownStation();
        final int prevSectionDistance = sections.get(upStation).getDistance();
        final int subtractedDistance = prevSectionDistance - section.getDistance();

        if (subtractedDistance <= 0) {
            throw new SectionDistanceTooLongException(String.format("역이 들어가야할 구간의 길이가 충분하지 않습니다.( 추가될 구간의 길이 : %d )",
                prevSectionDistance));
        }

        sections.put(downStation, Section.withNullId(downStation, prevDownStation, subtractedDistance));
    }

    private void divideWhenBaseStationInDown(final Station upStation, final Station downStation,
        final Section section) {
        final Section prevSection = findSectionGetAsDownStation(downStation);
        final Station prevUpStation = prevSection.getUpStation();
        final int subtractedDistance = prevSection.getDistance() - section.getDistance();
        sections.put(prevUpStation, Section.withNullId(prevUpStation, upStation, subtractedDistance));
    }

    private Section findSectionGetAsDownStation(final Station baseStation) {
        return sections.values()
            .stream()
            .filter((section -> section.getDownStation().equals(baseStation)))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("기준역을 하행역으로 가지는 구간이 존재하지 않습니다."));
    }

    public void removeStation(final Station removeStation) {
        validateExistStation(removeStation);

        if (isStartStation(removeStation)) {
            sections.remove(removeStation);
            return;
        }

        final Section downSection = sections.get(removeStation);
        final Section upSection = findSectionGetAsDownStation(removeStation);

        if (isEndStation(removeStation)) {
            sections.remove(upSection.getUpStation());
            return;
        }

        final int addedDistance = upSection.getDistance() + downSection.getDistance();
        final Section mergedSection = Section.withNullId(upSection.getUpStation(), downSection.getDownStation(),
            addedDistance);
        sections.remove(removeStation);
        sections.put(upSection.getUpStation(), mergedSection);
    }

    public Station getStationByName(final String stationName) {
        return getStations().stream()
            .filter(station -> station.getName().equals(stationName))
            .findFirst()
            .orElseThrow(() -> new LineDoesNotContainStationException("노선에 역이 존재하지 않습니다. ( " + stationName + " )"));
    }

    public List<Station> getStations() {
        final Set<Station> stations = new HashSet<>();
        sections.values()
            .forEach(section -> {
                stations.add(section.getUpStation());
                stations.add(section.getDownStation());
            });

        return new ArrayList<>(stations);
    }

    public List<Station> getSortedStations() {
        final List<Station> sortedStations = new ArrayList<>();
        final ArrayList<Station> upStations = new ArrayList<>(sections.keySet());

        Station station = getStartStation().orElseThrow(() -> new StartStationNotExistException("상행종점이 존재하지 않습니다."));

        while (upStations.contains(station)) {
            sortedStations.add(station);
            station = sections.get(station).getDownStation();
        }
        sortedStations.add(station);

        return sortedStations;
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections.values());
    }
}
