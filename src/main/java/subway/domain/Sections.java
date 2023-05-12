package subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import subway.domain.dto.ChangesByAddition;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Optional<Station> findStationByName(String name) {
        for (Section section : sections) {
            if (section.hasGivenNamedStation(name)) {
                return Optional.of(section.getStationWithGivenName(name));
            }
        }
        return Optional.empty();
    }

    public ChangesByAddition getChangesWhenAdded(Station upStation, Station downStation, Line line, int distance) {
        if (sections.size() == 0) {
            return getChangeWithSingleSection(upStation, downStation, line, distance);
        }
        validateNotContainsBoth(upStation, downStation);
        if (hasStationWithGivenName(upStation.getName())) {
            return getChangesWhenDownStationAdded(upStation, downStation, line, distance);
        }
        return getChangesWhenUpStationAdded(upStation, downStation, line, distance);
    }

    private void validateNotContainsBoth(Station station1, Station station2) {
        boolean station1Exist = hasStationWithGivenName(station1.getName());
        boolean station2Exist = hasStationWithGivenName(station2.getName());
        System.out.println(station1Exist + " " + station2Exist);
        if (station1Exist == station2Exist) {
            throw new IllegalArgumentException("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }
    }

    private boolean hasStationWithGivenName(String name) {
        for (Section section : sections) {
            if (section.hasGivenNamedStation(name)) {
                return true;
            }
        }
        return false;
    }

    private ChangesByAddition getChangesWhenDownStationAdded(Station upStation, Station downStation, Line line,
        int distance) {
        List<Section> relatedSections = getRelatedSections(upStation);
        // (down ->) up -> original 인 경우
        if (relatedSections.size() == 1 && relatedSections.get(0).isDownStationGiven(upStation)) {
            return getChangeWithSingleSection(upStation, downStation, line, distance);
        }
        Section originalSection = getAnySectionWithGivenUpStation(relatedSections, upStation);
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance),
                new Section(downStation, originalSection.getDownStation(), line,
                    originalSection.getReducedDistanceBy(distance))),
            List.of(originalSection)
        );
    }

    private ChangesByAddition getChangesWhenUpStationAdded(Station upStation, Station downStation, Line line,
        int distance) {
        List<Section> relatedSections = getRelatedSections(downStation);
        // original -> down (-> up) 인 경우
        if (relatedSections.size() == 1 && relatedSections.get(0).isUpStationGiven(downStation)) {
            return getChangeWithSingleSection(upStation, downStation, line, distance);
        }
        Section originalSection = getAnySectionWithGivenDownStation(relatedSections, downStation);
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance),
                new Section(originalSection.getUpStation(), upStation, line,
                    originalSection.getReducedDistanceBy(distance))),
            List.of(originalSection)
        );
    }

    private ChangesByAddition getChangeWithSingleSection(Station upStation, Station downStation, Line line,
        int distance) {
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance)),
            Collections.emptyList()
        );
    }

    private Section getAnySectionWithGivenUpStation(List<Section> sections, Station station) {
        for (Section section : sections) {
            if (section.isUpStationGiven(station)) {
                return section;
            }
        }
        throw new IllegalArgumentException("주어진 역이 상행인 경우가 존재하지 않습니다.");
    }

    private Section getAnySectionWithGivenDownStation(List<Section> sections, Station station) {
        for (Section section : sections) {
            if (section.isDownStationGiven(station)) {
                return section;
            }
        }
        throw new IllegalArgumentException("주어진 역이 하행인 경우가 존재하지 않습니다.");
    }

    private List<Section> getRelatedSections(Station station) {
        return sections.stream()
            .filter(section -> section.hasGivenNamedStation(station.getName()))
            .collect(Collectors.toList());
    }
}
