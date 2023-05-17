package subway.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import subway.domain.dto.ChangesByAddition;
import subway.domain.dto.ChangesByDeletion;

public class SortedSingleLineSections {

    private final List<Section> sections;
    private Map<Station, Section> sectionByUpStation = new HashMap<>();
    private Map<Station, Section> sectionByDownStation = new HashMap<>();

    public SortedSingleLineSections(List<Section> sections) {
        this.sections = sort(sections);
    }

    private List<Section> sort(List<Section> sections) {
        if (sections.size() == 0) {
            return Collections.emptyList();
        }
        sectionByUpStation = sections.stream()
            .collect(toMap(Section::getUpStation, Function.identity()));
        sectionByDownStation = sections.stream()
            .collect(toMap(Section::getDownStation, Function.identity()));

        Set<Station> upStations;
        HashSet<Station> downStations;

        upStations = new HashSet<>(sectionByUpStation.keySet());
        downStations = new HashSet<>(sectionByDownStation.keySet());
        upStations.removeAll(downStations);
        Station headStation = new ArrayList<>(upStations).get(0);

        upStations = new HashSet<>(sectionByUpStation.keySet());
        downStations = new HashSet<>(sectionByDownStation.keySet());
        downStations.removeAll(upStations);
        Station tailStation = new ArrayList<>(downStations).get(0);

        List<Section> sorted = new ArrayList<>();
        Station current = headStation;
        while (current != tailStation) {
            Section target = sectionByUpStation.get(current);
            sorted.add(target);
            current = target.getDownStation();
        }
        return sorted;
    }

    public Optional<Station> findStationById(Long id) {
        return sections.stream()
            .filter(section -> section.getStationWithGivenId(id).isPresent())
            .findAny()
            .flatMap(section -> section.getStationWithGivenId(id));
    }

    public Section findAnySectionWithGivenStations(Station upStation, Station downStation) {
        if (sectionByUpStation.containsKey(upStation) && sectionByUpStation.get(upStation)
            .isDownStationGiven(downStation)) {
            return sectionByUpStation.get(upStation);
        }
        if (sectionByDownStation.containsKey(downStation) && sectionByDownStation.get(downStation)
            .isUpStationGiven(upStation)) {
            return sectionByDownStation.get(downStation);
        }
        throw new IllegalArgumentException("주어진 역으로 구성된 구간이 존재하지 않습니다.");
    }

    public ChangesByAddition findChangesWhenAdd(Station upStation, Station downStation, Line line, int distance) {
        if (sections.size() == 0) {
            return findChangeWithSingleSection(upStation, downStation, line, distance);
        }
        validateNotContainsBoth(upStation, downStation);
        if (sectionByUpStation.containsKey(upStation) || sectionByDownStation.containsKey(upStation)) {
            return findChangesWhenDownStationAdded(upStation, downStation, line, distance);
        }
        return findChangesWhenUpStationAdded(upStation, downStation, line, distance);
    }

    private ChangesByAddition findChangeWithSingleSection(Station upStation, Station downStation, Line line,
        int distance) {
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance)),
            Collections.emptyList()
        );
    }

    private void validateNotContainsBoth(Station station1, Station station2) {
        boolean station1Exist = sectionByUpStation.containsKey(station1) || sectionByDownStation.containsKey(station1);
        boolean station2Exist = sectionByUpStation.containsKey(station2) || sectionByDownStation.containsKey(station2);
        if (station1Exist == station2Exist) {
            throw new IllegalArgumentException("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }
    }

    private ChangesByAddition findChangesWhenDownStationAdded(Station upStation, Station downStation, Line line,
        int distance) {
        if (getTailSection().isDownStationGiven(upStation)) {
            return findChangeWithSingleSection(upStation, downStation, line, distance);
        }
        Section originalSection = findAnySectionWithGivenUpStation(upStation);
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance),
                new Section(downStation, originalSection.getDownStation(), line,
                    originalSection.getReducedDistanceBy(distance))),
            List.of(originalSection)
        );
    }

    private ChangesByAddition findChangesWhenUpStationAdded(Station upStation, Station downStation, Line line,
        int distance) {
        if (getHeadSection().isUpStationGiven(downStation)) {
            return findChangeWithSingleSection(upStation, downStation, line, distance);
        }
        Section originalSection = findAnySectionWithGivenDownStation(downStation);
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance),
                new Section(originalSection.getUpStation(), upStation, line,
                    originalSection.getReducedDistanceBy(distance))),
            List.of(originalSection)
        );
    }

    private Section findAnySectionWithGivenUpStation(Station station) {
        if (sectionByUpStation.containsKey(station)) {
            return sectionByUpStation.get(station);
        }
        throw new IllegalArgumentException("주어진 역이 상행인 경우가 존재하지 않습니다.");
    }

    private Section findAnySectionWithGivenDownStation(Station station) {
        if (sectionByDownStation.containsKey(station)) {
            return sectionByDownStation.get(station);
        }
        throw new IllegalArgumentException("주어진 역이 하행인 경우가 존재하지 않습니다.");
    }

    private SortedSingleLineSections findRelatedSections(Station station) {
        List<Section> sections = new ArrayList<>();
        if (sectionByUpStation.containsKey(station)) {
            sections.add(sectionByUpStation.get(station));
        }
        if (sectionByDownStation.containsKey(station)) {
            sections.add(sectionByDownStation.get(station));
        }
        return new SortedSingleLineSections(sections);
    }

    public ChangesByDeletion findChangesWhenDelete(Station station) {
        SortedSingleLineSections relatedSortedSingleLineSections = findRelatedSections(station);
        if (relatedSortedSingleLineSections.size() == 0) {
            return new ChangesByDeletion(Collections.emptyList(), Collections.emptyList());
        }
        if (relatedSortedSingleLineSections.size() == 1) {
            return new ChangesByDeletion(Collections.emptyList(),
                removeAll(relatedSortedSingleLineSections).getSections());
        }
        return relatedSortedSingleLineSections.getChangesByMidStationDeletion();
    }

    private ChangesByDeletion getChangesByMidStationDeletion() {
        Section upSection = sections.get(0);
        Section downSection = sections.get(1);
        return new ChangesByDeletion(
            List.of(new Section(upSection.getUpStation(), downSection.getDownStation(), upSection.getLine(),
                (upSection.getDistance() + downSection.getDistance()))),
            List.of(upSection, downSection)
        );
    }

    private Section getHeadSection() {
        return sections.get(0);
    }

    private Section getTailSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Station> getStationsInOrder() {
        if (sections.size() == 0) {
            return Collections.emptyList();
        }
        List<Station> stations = sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());
        System.out.println(stations);
        System.out.println(getTailSection().getDownStation());
        stations.add(getTailSection().getDownStation());
        System.out.println(stations);
        return stations;
    }

    public int size() {
        return sections.size();
    }

    private SortedSingleLineSections removeAll(SortedSingleLineSections sortedSingleLineSections) {
        this.sections.removeAll(sortedSingleLineSections.getSections());
        return new SortedSingleLineSections(this.sections);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
