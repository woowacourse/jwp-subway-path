package subway.domain.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import subway.domain.station.Station;

public class Sections {

    private static final Distance MINIMUM_DISTANCE = Distance.from(1);
    private static final String INVALID_SECTION_INFO = "유효한 구간 정보가 아닙니다.";

    private final Map<Station, Section> sections;

    public Sections(final Map<Station, Section> sections) {
        if (!sections.isEmpty()) {
            validateSections(sections);
        }

        this.sections = sections;
    }

    private void validateSections(final Map<Station, Section> sections) {
        validateTargetDirectionSection(sections, Direction.UP);
        validateTargetDirectionSection(sections, Direction.DOWN);
    }

    private void validateTargetDirectionSection(final Map<Station, Section> sections, final Direction direction) {
        int count = 0;
        Station targetStation = findTerminalStation(sections, direction);

        while (count++ < sections.size() - 1) {
            final Section section = sections.get(targetStation);
            final Station nowStation = section.findStationByDirection(direction)
                    .orElseThrow(() -> new IllegalArgumentException(INVALID_SECTION_INFO));
            validateDistance(section, nowStation);
            targetStation = nowStation;
        }
    }

    private void validateDistance(final Section section, final Station targetStation) {
        final Distance distance = section.findDistanceByStation(targetStation);

        if (distance.isLessThan(MINIMUM_DISTANCE)) {
            throw new IllegalArgumentException(INVALID_SECTION_INFO);
        }
    }

    private Station findTerminalStation(final Map<Station, Section> sections, final Direction direction) {
        return sections.keySet().stream()
                .filter(station -> sections.get(station).isTerminalStation())
                .filter(station -> sections.get(station).findEndStationPathDirection().matches(direction))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_SECTION_INFO));
    }

    public static Sections create() {
        return new Sections(new HashMap<>());
    }

    public static Sections of(final Map<Station, Section> sections) {
        return new Sections(sections);
    }

    public Sections addSection(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Direction direction
    ) {
        final SectionsAdder sectionsAdder = SectionsAdder.from(sections);
        final Map<Station, Section> addedSections = sectionsAdder.addSection(
                sourceStation, targetStation, distance, direction);

        return new Sections(addedSections);
    }

    public Sections removeStation(final Station targetStation) {
        final SectionRemover remover = SectionRemover.from(sections);
        final Map<Station, Section> removedSections = remover.removeStation(targetStation);

        return new Sections(removedSections);
    }

    public boolean isRegisterStation(final Station targetStation) {
        return sections.containsKey(targetStation);
    }

    public List<Station> findAllAdjustStationByStation(final Station station) {
        validateRegisterStation(station);

        return sections.get(station)
                .findAllStation();
    }

    private void validateRegisterStation(final Station station) {
        if (!sections.containsKey(station)) {
            throw new IllegalArgumentException("해당 역은 해당 노선에 등록되지 않은 역입니다.");
        }
    }

    public List<Station> findStationsByOrdered() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        final Queue<Station> queue = new LinkedList<>();
        final Set<Station> visited = new LinkedHashSet<>();

        final Station upStation = findTerminalStation(sections, Direction.DOWN);
        queue.add(upStation);
        visited.add(upStation);

        while (!queue.isEmpty()) {
            final Station nowStation = queue.poll();
            for (final Station nextStation : sections.get(nowStation).findAllStation()) {
                if (!visited.contains(nextStation)) {
                    queue.add(nextStation);
                    visited.add(nextStation);
                }
            }
        }

        return new ArrayList<>(visited);
    }

    public Map<Station, Section> sections() {
        return new HashMap<>(sections);
    }
}
