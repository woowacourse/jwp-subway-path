package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SectionsDomain {

    private final List<SectionDomain> sections;

    public SectionsDomain(final List<SectionDomain> sections) {
        this.sections = sections;
    }

    public static SectionsDomain from(final List<SectionDomain> sections) {
        return new SectionsDomain(sections);
    }

    public void addSection(final SectionDomain newSection) {
        validate(newSection);
        this.sections.add(newSection);
    }

    private void validate(final SectionDomain newSection) {
        if (sections.contains(newSection)) {
            throw new IllegalArgumentException("노선에 이미 존재하는 구간을 등록할 수 없습니다.");
        }
        if (!isBaseStationExists(newSection)) {
            throw new IllegalArgumentException("구간이 하나 이상 존재하는 노선에 새로운 구간을 등록할 때 기준이 되는 지하철역이 존재하지 않는다면 예외가 발생한다.");
        }
    }

    private boolean isBaseStationExists(final SectionDomain newSection) {
        return sections.isEmpty() || sections.stream()
                .anyMatch(section -> section.isBaseStationExists(newSection));
    }

    public List<StationDomain> collectAllStations() {
        final List<StationDomain> stations = new ArrayList<>();

        stations.addAll(sections.stream()
                .map(SectionDomain::getUpStation)
                .collect(Collectors.toList()));

        stations.addAll(sections.stream()
                .map(SectionDomain::getDownStation)
                .collect(Collectors.toList()));

        return stations;
    }

    public List<SectionDomain> getSections() {
        return new ArrayList<>(sections);
    }
}
