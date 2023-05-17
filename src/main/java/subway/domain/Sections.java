package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {

    private static final String DUPLICATE_SECTION_MESSAGE = "이미 존재하는 구간입니다.";
    private static final String EMPTY_TARGET_SECTION_MESSAGE = "노선에 기준이 되는 역이 존재하지 않습니다.";
    private static final String EMPTY_SECTIONS_MESSAGE = "구간이 존재하지 않습니다.";
    private static final int TERMINAL_CHECK_NUMBER = 1;

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(final Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        final List<Section> upSections = findSections(section.getUpStation());
        final List<Section> downSections = findSections(section.getDownStation());
        validateSection(section, upSections, downSections);

        if (isUpSectionInMiddle(upSections, section) && downSections.isEmpty()) {
            addSectionInMiddle(section, section.getUpStation());
        }
        if (isDownSectionInMiddle(downSections, section) && upSections.isEmpty()) {
            addSectionInMiddle(section, section.getDownStation());
        }

        sections.add(section);
    }

    private List<Section> findSections(final Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation().getId().equals(station.getId())
                        || it.getDownStation().getId().equals(station.getId()))
                .collect(Collectors.toList());
    }

    private void validateSection(final Section section, final List<Section> upSections, final List<Section> downSections) {
        if (sections.contains(section)) {
            throw new IllegalArgumentException(DUPLICATE_SECTION_MESSAGE);
        }
        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_TARGET_SECTION_MESSAGE);
        }
    }

    private boolean isUpSectionInMiddle(final List<Section> upSections, final Section section) {
        return upSections.size() >= TERMINAL_CHECK_NUMBER &&
                upSections.stream().anyMatch(it -> it.getUpStation().getId().equals(section.getUpStation().getId()));
    }

    private boolean isDownSectionInMiddle(final List<Section> downSections, final Section section) {
        return downSections.size() >= TERMINAL_CHECK_NUMBER &&
                downSections.stream().anyMatch(it -> it.getDownStation().getId().equals(section.getDownStation().getId()));
    }

    private void addSectionInMiddle(final Section section, final Station station) {
        final Section previousSection = sections.stream()
                .filter(it -> it.getUpStation().getId().equals(station.getId())
                        || it.getDownStation().getId().equals(station.getId()))
                .findAny().orElseThrow(IllegalStateException::new);
        final int dividedDistance = previousSection.getDistance() - section.getDistance();

        sections.remove(previousSection);
        if (previousSection.getUpStation().getId().equals(section.getUpStation().getId())) {
            sections.add(new Section(section.getDownStation(), previousSection.getDownStation(), dividedDistance));
            return;
        }
        sections.add(new Section(previousSection.getUpStation(), section.getUpStation(), dividedDistance));
    }

    public void removeSectionByStation(final Station station) {
        final List<Section> relatedSections = sections.stream()
                .filter(it -> it.getUpStation().getId().equals(station.getId()) ||
                        it.getDownStation().getId().equals(station.getId()))
                .collect(Collectors.toList());
        validateEmptySections(relatedSections);

        if (relatedSections.size() == TERMINAL_CHECK_NUMBER) {
            sections.remove(relatedSections.get(0));
            return;
        }

        final Section upSection = relatedSections.stream()
                .filter(it -> it.getDownStation().getId().equals(station.getId()))
                .findAny().orElseThrow(IllegalStateException::new);
        final Section downSection = relatedSections.stream()
                .filter(it -> it.getUpStation().getId().equals(station.getId()))
                .findAny().orElseThrow(IllegalStateException::new);
        final int newDistance = upSection.getDistance() + downSection.getDistance();
        sections.remove(upSection);
        sections.remove(downSection);
        sections.add(new Section(upSection.getUpStation(), downSection.getDownStation(), newDistance));
    }

    private void validateEmptySections(final List<Section> relatedSections) {
        if (relatedSections.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_SECTIONS_MESSAGE);
        }
    }

    public List<Station> sortStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        final Map<Station, Station> stationRelation = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));

        final Station rootStation = stationRelation.keySet().stream()
                .filter(it -> !stationRelation.containsValue(it))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        return linkStations(stationRelation, rootStation);
    }

    private List<Station> linkStations(final Map<Station, Station> stationRelation, final Station rootStation) {
        final List<Station> stations = new ArrayList<>();
        Station nextStation = rootStation;
        while (nextStation != null) {
            stations.add(nextStation);
            nextStation = stationRelation.get(nextStation);
        }
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
