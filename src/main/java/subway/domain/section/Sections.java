package subway.domain.section;

import static subway.exception.ErrorCode.SECTION_ADD_STATION_NOT_EXISTS;
import static subway.exception.ErrorCode.SECTION_ALREADY_ADD;
import static subway.exception.ErrorCode.SECTION_TOO_FAR_DISTANCE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.domain.station.Station;
import subway.exception.GlobalException;

public class Sections {

    private final List<Section> sections;
    private final List<Station> sortedStations;

    public Sections(final List<Section> sections) {
        this.sections = sections;
        this.sortedStations = sort(sections);
    }

    public void validateSections(final Section section) {
        if (sortedStations.isEmpty()) {
            return;
        }
        final Station sourceStation = section.getSource();
        final Station targetStation = section.getTarget();
        if (!sortedStations.contains(sourceStation) && !sortedStations.contains(targetStation)) {
            throw new GlobalException(SECTION_ADD_STATION_NOT_EXISTS);
        }
        if (sortedStations.contains(sourceStation) && sortedStations.contains(targetStation)) {
            throw new GlobalException(SECTION_ALREADY_ADD);
        }
    }

    public boolean isNewSection(final Section section) {
        return sortedStations.isEmpty() || isTargetUpward(section.getTarget()) || isSourceDownward(section.getSource());
    }

    public Optional<Section> getExistsSectionOfSource(final Section requestSection) {
        return sections.stream()
            .filter(section -> section.getSource().equals(requestSection.getSource()))
            .findFirst()
            .map(section -> validateDistance(requestSection, section));
    }

    public Optional<Section> getExistsSectionOfTarget(final Section requestSection) {
        return sections.stream()
            .filter(section -> section.getTarget().equals(requestSection.getTarget()))
            .findFirst()
            .map(section -> validateDistance(requestSection, section));
    }

    public Optional<Section> combineSection(final Station newStation) {
        if (isTargetUpward(newStation) || isSourceDownward(newStation)) {
            return Optional.empty();
        }
        Station newSourceStation = null, newTargetStation = null;
        SectionDistance newDistance = SectionDistance.zero();
        for (Section section : sections) {
            if (section.equalToSource(newStation)) {
                newDistance = newDistance.add(section.getDistance());
                newTargetStation = section.getTarget();
            }
            if (section.equalToTarget(newStation)) {
                newDistance = newDistance.add(section.getDistance());
                newSourceStation = section.getSource();
            }
        }
        return Optional.of(new Section(newSourceStation, newTargetStation, newDistance));
    }

    private boolean isTargetUpward(final Station targetStation) {
        return sortedStations.get(0).equals(targetStation);
    }

    private boolean isSourceDownward(final Station sourceStation) {
        return sortedStations.get(sortedStations.size() - 1).equals(sourceStation);
    }

    private Section validateDistance(final Section requestSection, final Section targetSection) {
        final SectionDistance requestDistance = requestSection.getDistance();
        final SectionDistance targetDistance = targetSection.getDistance();
        if (requestDistance.isGreaterAndEqualsThan(targetDistance)) {
            throw new GlobalException(SECTION_TOO_FAR_DISTANCE);
        }
        return targetSection;
    }

    private List<Station> sort(final List<Section> sections) {
        final Map<Station, Station> stationRelationShip = getStationRelationShip(sections);
        final Optional<Station> startStation = getStartStation(stationRelationShip);
        return startStation.map(station -> getSortedStations(stationRelationShip, station))
            .orElse(Collections.emptyList());
    }

    private List<Station> getSortedStations(final Map<Station, Station> stationRelationShip,
                                            final Station startStation) {
        final List<Station> sortedStations = new ArrayList<>();
        Optional<Station> nextStation = Optional.ofNullable(startStation);
        while (nextStation.isPresent()) {
            sortedStations.add(nextStation.get());
            nextStation = Optional.ofNullable(stationRelationShip.get(nextStation.get()));
        }
        return sortedStations;
    }

    private Map<Station, Station> getStationRelationShip(final List<Section> sections) {
        return sections.stream()
            .collect(Collectors.toMap(Section::getSource, Section::getTarget));
    }

    private Optional<Station> getStartStation(final Map<Station, Station> stationRelationShip) {
        return stationRelationShip.keySet().stream()
            .filter(station -> !stationRelationShip.containsValue(station))
            .findFirst();
    }

    public List<Station> getSortedStations() {
        return sortedStations;
    }
}
