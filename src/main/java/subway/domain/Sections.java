package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.exception.custom.StartStationNotExistException;

public class Sections {

    private final Map<Station, Section> sections;

    public Sections(final List<Section> sections) {
        SectionsValidator.validate(sections);
        this.sections = sections.stream()
            .collect(Collectors.toMap(Section::getUpStation, (section) -> section));
    }

    public void addSection(final Station baseStation, final Section section) {
        if (sections.isEmpty() || isStartStation(section.getDownStation()) || isEndStation(section.getUpStation())) {
            sections.put(section.getUpStation(), section);
            return;
        }
        validateBaseStation(baseStation);
        divideSection(baseStation, section);
    }

    private void validateBaseStation(final Station station) {
        sections.values()
            .stream()
            .filter((section -> section.containsStation(station)))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("기준이 되는 역이 노선에 존재하지 않습니다."));
    }

    private void divideSection(final Station baseStation, final Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        if (upStation.equals(baseStation)) {
            final Station prevDownStation = sections.get(upStation).getDownStation();
            final int distanceLeft = sections.get(upStation).getDistance() - section.getDistance();
            sections.put(downStation, Section.of(downStation, prevDownStation, distanceLeft));
            sections.put(upStation, section);
            return;
        }

        final Section prevSection = findSectionGetAsDownStation(downStation);
        final Station prevUpStation = prevSection.getUpStation();
        final int distanceLeft = prevSection.getDistance() - section.getDistance();
        sections.put(prevUpStation, Section.of(prevUpStation, upStation, distanceLeft));
        sections.put(upStation, section);
    }

    private Section findSectionGetAsDownStation(final Station baseStation) {
        return sections.values()
            .stream()
            .filter((section -> section.getDownStation().equals(baseStation)))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("기준역을 하행역으로 가지는 구간이 존재하지 않습니다."));
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

    private boolean isStartStation(final Station station) {
        return getStartStation().map(value -> value.equals(station))
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

    private boolean isEndStation(final Station station) {
        return getEndStation().map(value -> value.equals(station))
            .orElse(false);
    }

    public void removeStation(final Station removeStation) {
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

        final Section mergedSection = Section.of(upSection.getUpStation(), downSection.getDownStation(),
            upSection.getDistance() + downSection.getDistance());
        sections.remove(removeStation);
        sections.put(upSection.getUpStation(), mergedSection);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections.values());
    }

    public List<Station> getSortedStations() {
        final List<Station> sortedStations = new ArrayList<>();
        final ArrayList<Station> upStations = new ArrayList<>(sections.keySet());

        Station currentStation = getStartStation().orElseThrow(
            () -> new StartStationNotExistException("상행종점이 존재하지 않습니다."));

        while (upStations.contains(currentStation)) {
            sortedStations.add(currentStation);
            currentStation = sections.get(currentStation).getDownStation();
        }
        sortedStations.add(currentStation);
        
        return sortedStations;
    }
}
