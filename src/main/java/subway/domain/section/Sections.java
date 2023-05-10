package subway.domain.section;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import subway.domain.station.Station;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void validateSections(final Section requestSection) {
        final List<Station> stations = getExistStations();

        final Station sourceRequest = requestSection.getSource();
        final Station targetRequest = requestSection.getTarget();

        if (!stations.contains(sourceRequest) && !stations.contains(targetRequest)) {
            throw new IllegalArgumentException("존재하지 않는 역을 추가할 수 없습니다.");
        }

        if (stations.contains(sourceRequest) && stations.contains(targetRequest)) {
            throw new IllegalArgumentException("이미 추가된 구간입니다.");
        }
    }

    public boolean isTargetUpward(final Station targetStation) {
        return sections.get(0).getSource().equals(targetStation);
    }

    public boolean isSourceDownward(final Station sourceStation) {
        return sections.get(sections.size()-1).getTarget().equals(sourceStation);
    }

    public Optional<Section> getExistsSectionOfSource(final Section requestSection) {
        for (Section section : sections) {
            if (section.getSource().equals(requestSection.getSource())) {
                return validateDistance(requestSection, section);
            }
        }
        return Optional.empty();
    }

    public Optional<Section> getExistsSectionOfTarget(final Section requestSection) {
        for (Section section : sections) {
            if (section.getTarget().equals(requestSection.getTarget())) {
                return validateDistance(requestSection, section);
            }
        }
        return Optional.empty();
    }

    private List<Station> getExistStations() {
        return sections.stream()
            .flatMap(section -> Stream.of(section.getSource(), section.getTarget()))
            .collect(Collectors.toUnmodifiableList());
    }

    private Optional<Section> validateDistance(final Section requestSection, final Section section) {
        final int distance = section.getDistance();
        final int requestDistance = requestSection.getDistance();
        if (requestDistance >= distance) {
            throw new IllegalArgumentException("거리가 너무 커서 역을 추가할 수 없습니다.");
        }
        return Optional.of(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
