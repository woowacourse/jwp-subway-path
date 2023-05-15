package subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import subway.domain.dto.ChangesByAddition;
import subway.domain.dto.ChangesByDeletion;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Optional<Station> findStationById(Long id) {
        return sections.stream()
            .filter(section -> section.getStationWithGivenId(id).isPresent())
            .findAny()
            .flatMap(section -> section.getStationWithGivenId(id));
    }

    public ChangesByAddition findChangesWhenAdd(Station upStation, Station downStation, Line line, int distance) {
        if (sections.size() == 0) {
            return findChangeWithSingleSection(upStation, downStation, line, distance);
        }
        validateNotContainsBoth(upStation, downStation);
        if (hasStationWithGivenName(upStation.getName())) {
            return findChangesWhenDownStationAdded(upStation, downStation, line, distance);
        }
        return findChangesWhenUpStationAdded(upStation, downStation, line, distance);
    }

    private void validateNotContainsBoth(Station station1, Station station2) {
        boolean station1Exist = hasStationWithGivenName(station1.getName());
        boolean station2Exist = hasStationWithGivenName(station2.getName());
        if (station1Exist == station2Exist) {
            throw new IllegalArgumentException("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }
    }

    private boolean hasStationWithGivenName(String name) {
        return sections.stream()
            .anyMatch(section -> section.hasGivenNamedStation(name));
    }

    private ChangesByAddition findChangesWhenDownStationAdded(Station upStation, Station downStation, Line line,
        int distance) {
        Sections relatedSections = findRelatedSections(upStation);
        // (down ->) up -> original 인 경우
        if (relatedSections.size() == 1 && relatedSections.get(0).isDownStationGiven(upStation)) {
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
        Sections relatedSections = findRelatedSections(downStation);
        // original -> down (-> up) 인 경우
        if (relatedSections.size() == 1 && relatedSections.get(0).isUpStationGiven(downStation)) {
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

    private ChangesByAddition findChangeWithSingleSection(Station upStation, Station downStation, Line line,
        int distance) {
        return new ChangesByAddition(
            List.of(new Section(upStation, downStation, line, distance)),
            Collections.emptyList()
        );
    }

    private Sections findRelatedSections(Station station) {
        List<Section> sectionList = sections.stream()
            .filter(section -> section.hasGivenNamedStation(station.getName()))
            .collect(Collectors.toList());
        return new Sections(sectionList);
    }

    public Section findAnySectionWithGivenStations(Station upStation, Station downStation) {
        for (Section section : sections) {
            if (section.isUpStationGiven(upStation) && section.isDownStationGiven(downStation)) {
                return section;
            }
        }
        throw new IllegalArgumentException("주어진 역으로 구성된 구간이 존재하지 않습니다.");
    }

    private Section findAnySectionWithGivenUpStation(Station station) {
        return sections.stream()
            .filter(section -> section.isUpStationGiven(station))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("주어진 역이 상행인 경우가 존재하지 않습니다."));
    }

    private Section findAnySectionWithGivenDownStation(Station station) {
        return sections.stream()
            .filter(section -> section.isDownStationGiven(station))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("주어진 역이 하행인 경우가 존재하지 않습니다."));
    }

    public ChangesByDeletion findChangesWhenDelete(Station station) {
        ChangesByDeletion changes = new ChangesByDeletion(Collections.emptyList(), Collections.emptyList());
        Map<Line, List<Section>> sectionsByLine = sections.stream()
            .collect(Collectors.groupingBy(Section::getLine));
        for (Map.Entry<Line, List<Section>> entry : sectionsByLine.entrySet()) {
            changes = changes.combine(findChangesWhenDelete(station, new Sections(entry.getValue())));
        }
        return changes;
    }

    private ChangesByDeletion findChangesWhenDelete(Station station, Sections sections) {
        Sections relatedSections = sections.findRelatedSections(station);
        if (relatedSections.size() == 0) {
            return new ChangesByDeletion(Collections.emptyList(), Collections.emptyList());
        }
        if (relatedSections.size() == 1) {
            return new ChangesByDeletion(Collections.emptyList(), sections.removeAll(relatedSections).getSections());
        }
        return getChangesByMidStationDeletion(station, relatedSections);
    }

    private ChangesByDeletion getChangesByMidStationDeletion(Station station, Sections relatedSections) {
        Section upSection;
        Section downSection;
        if (relatedSections.get(0).isUpStationGiven(station)) {
            upSection = relatedSections.get(0);
            downSection = relatedSections.get(1);
        } else {
            upSection = relatedSections.get(1);
            downSection = relatedSections.get(0);
        }
        return new ChangesByDeletion(
            List.of(new Section(downSection.getUpStation(), upSection.getDownStation(), upSection.getLine(),
                (upSection.getDistance() + downSection.getDistance()))),
            List.of(upSection, downSection)
        );
    }

    public int size() {
        return sections.size();
    }

    private Section get(int index) {
        return sections.get(index);
    }

    private Sections removeAll(Sections sections) {
        this.sections.removeAll(sections.getSections());
        return new Sections(this.sections);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
