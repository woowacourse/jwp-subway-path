package subway.domain;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import subway.exception.notfound.SectionNotFoundException;

public enum DeleteCase {

    END_POINT_STATION(DeleteCase::endPointStation),
    NOT_END_POINT_STATION(DeleteCase::notEndPointStation);

    private final BiConsumer<Sections, Station> biConsumer;

    DeleteCase(BiConsumer<Sections, Station> biConsumer) {
        this.biConsumer = biConsumer;
    }

    public static DeleteCase from(final int nearStationCount) {
        if (nearStationCount == 1) {
            return END_POINT_STATION;
        }

        return NOT_END_POINT_STATION;
    }

    private static void endPointStation(final Sections sections, final Station targetStation) {
        Section targetSection = sections.getSections().stream()
                .filter(section -> section.contains(targetStation))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);

        sections.getSections().remove(targetSection);
    }

    private static void notEndPointStation(final Sections sections, final Station targetStation) {
        List<Section> targetSections = sections.getSections().stream()
                .filter(section -> section.getUpStation().getName().equals(targetStation.getName()) || section.getDownStation().getName().equals(targetStation.getName()))
                .collect(Collectors.toList());

        Section upStationTargetSection = targetSections.stream()
                .filter(section -> section.getUpStation().getName().equals(targetStation.getName()))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);

        Section downStationTargetSection = targetSections.stream()
                .filter(section -> section.getDownStation().getName().equals(targetStation.getName()))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);

        sections.getSections().remove(upStationTargetSection);
        sections.getSections().remove(downStationTargetSection);
        sections.getSections()
                .add(new Section(downStationTargetSection.getUpStation(), upStationTargetSection.getDownStation(), upStationTargetSection.getDistance() + downStationTargetSection.getDistance()));
    }

    public void execute(final Sections sections, final Station targetStation) {
        biConsumer.accept(sections, targetStation);
    }
}
