package subway.domain.section;

import java.util.Map;
import subway.domain.station.Station;

public class SectionsAdder {

    private final Map<Station, Section> sections;

    private SectionsAdder(final Map<Station, Section> sections) {
        this.sections = sections;
    }

    public static SectionsAdder from(final Map<Station, Section> sections) {
        return new SectionsAdder(sections);
    }

    public Map<Station, Section> addSection(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Direction direction
    ) {
        if (sections.isEmpty()) {
            return initialSection(sourceStation, targetStation, distance, direction);
        }
        return addStation(sourceStation, targetStation, distance, direction);
    }

    private Map<Station, Section> initialSection(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Direction direction
    ) {
        final Section sourceStationSection = Section.create();
        final Section targetStationSection = Section.create();

        sourceStationSection.add(targetStation, distance, direction);
        targetStationSection.add(sourceStation, distance, direction.reverse());

        sections.put(sourceStation, sourceStationSection);
        sections.put(targetStation, targetStationSection);
        return sections;
    }

    private Map<Station, Section> addStation(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Direction direction
    ) {
        validateStation(sourceStation, targetStation);

        final Section section = sections.get(sourceStation);

        section.findStationByDirection(direction)
                .ifPresentOrElse(
                        station -> addSectionToMiddleStation(sourceStation, targetStation, distance, station),
                        () -> addSectionToTerminalStation(sourceStation, targetStation, distance, direction)
                );
        return sections;
    }

    private void validateStation(final Station sourceStation, final Station targetStation) {
        if (!isRegisterStation(sourceStation)) {
            throw new IllegalArgumentException("지정한 기준 역은 등록되어 있지 않은 역입니다.");
        }
        if (isRegisterStation(targetStation)) {
            throw new IllegalArgumentException("이미 등록된 역입니다.");
        }
    }

    private boolean isRegisterStation(final Station targetStation) {
        return sections.containsKey(targetStation);
    }

    private void addSectionToTerminalStation(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Direction direction
    ) {
        final Section section = sections.get(sourceStation);
        section.add(targetStation, distance, direction);

        final Section targetStationSection = Section.create();
        targetStationSection.add(sourceStation, distance, direction.reverse());
        sections.put(targetStation, targetStationSection);
    }

    private void addSectionToMiddleStation(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Station existsStation
    ) {
        final Section sourceStationSection = sections.get(sourceStation);
        final Section existsStationSection = sections.get(existsStation);
        final Section targetStationSection = Section.create();

        final Direction direction = sourceStationSection.findDirectionByStation(existsStation);
        final Distance middleDistance = sourceStationSection.calculateMiddleDistance(existsStation, distance);

        sourceStationSection.add(targetStation, distance, direction);
        targetStationSection.add(sourceStation, distance, direction.reverse());

        existsStationSection.add(targetStation, middleDistance, direction.reverse());
        targetStationSection.add(existsStation, middleDistance, direction);

        sourceStationSection.delete(existsStation);
        existsStationSection.delete(sourceStation);
        sections.put(targetStation, targetStationSection);
    }
}
