package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import subway.exception.custom.StartStationNotExistException;

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
        validateBaseStation(baseStation);

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

    private void validateBaseStation(final Station station) {
        sections.values()
            .stream()
            .filter((section -> section.containsStation(station)))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("기준이 되는 역이 노선에 존재하지 않습니다."));
    }

    private void divideWhenBaseStationInUp(final Station upStation, final Station downStation, final Section section) {
        final Station prevDownStation = sections.get(upStation).getDownStation();
        final int subtractedDistance = sections.get(upStation).getDistance() - section.getDistance();
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
        if (isStartStation(removeStation)) {
            sections.remove(removeStation);
            return;
        }

        final Section upSection = findSectionGetAsDownStation(removeStation);
        final Section downSection = sections.get(removeStation);

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

    public List<Station> getStations() {
        final Set<Station> upStations = sections.keySet();
        final List<Station> downStations = sections.values().stream().map(Section::getDownStation)
            .collect(Collectors.toList());

        final Set<Station> stations = new HashSet<>();
        stations.addAll(upStations);
        stations.addAll(downStations);

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
