package subway.domain.section;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import subway.domain.station.Station;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section requestSection) {
        if (!sections.isEmpty()) {
            validateNonExists(requestSection);
        }
        sections.add(requestSection);
    }

    private void validateNonExists(final Section requestSection) {
        final List<Station> stations = sections.stream()
            .flatMap(section -> Stream.of(section.getSource(), section.getTarget()))
            .collect(Collectors.toUnmodifiableList());

        final Station sourceRequest = requestSection.getSource();
        final Station targetRequest = requestSection.getTarget();

        if (!stations.contains(sourceRequest) && !stations.contains(targetRequest)) {
            throw new IllegalArgumentException("존재하지 않는 역을 추가할 수 없습니다");
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
