package subway.domain;

import java.util.function.BiConsumer;

public enum InsertCase {

    EMPTY_SECTIONS(InsertCase::emptySections),
    EXIST_ONLY_UP_STATION(InsertCase::existOnlyUpStation),
    EXIST_ONLY_DOWN_STATION(InsertCase::existOnlyDownStation);

    private final BiConsumer<Sections, Section> biConsumer;

    InsertCase(BiConsumer<Sections, Section> biConsumer) {
        this.biConsumer = biConsumer;
    }

    public static InsertCase of(final boolean isExistUpStation, final boolean isExistDownStation) {
        if (!isExistUpStation && !isExistDownStation) {
            return EMPTY_SECTIONS;
        }
        if (isExistUpStation && !isExistDownStation) {
            return EXIST_ONLY_UP_STATION;
        }
        return EXIST_ONLY_DOWN_STATION;
    }

    private static void emptySections(final Sections sections, final Section targetSection) {
        sections.getSections().add(targetSection);
    }

    private static void existOnlyUpStation(final Sections sections, final Section targetSection) {
        Station station = targetSection.getUpStation();
        if (sections.isExistAsUpStation(station)) {
            Section sectionWithUpStation = sections.findSectionWithUpStation(station);
            sectionWithUpStation.validateDistance(targetSection.getDistance());
            sections.getSections().remove(sectionWithUpStation);
            sections.getSections().add(targetSection);
            sections.getSections().add(new Section(targetSection.getDownStation(), sectionWithUpStation.getDownStation(), sectionWithUpStation.getDistance() - targetSection.getDistance()));
            return;
        }

        sections.getSections().add(targetSection);
    }

    private static void existOnlyDownStation(final Sections sections, final Section targetSection) {
        Station station = targetSection.getDownStation();
        if (sections.isExistAsUpStation(station)) {
            sections.getSections().add(targetSection);
            return;
        }

        Section sectionWithDownStation = sections.findSectionWithDownStation(targetSection.getDownStation());
        sectionWithDownStation.validateDistance(targetSection.getDistance());
        sections.getSections().remove(sectionWithDownStation);
        sections.getSections().add(targetSection);
        sections.getSections().add(new Section(sectionWithDownStation.getUpStation(), targetSection.getUpStation(), sectionWithDownStation.getDistance() - targetSection.getDistance()));
    }

    public void execute(final Sections sections, final Section targetSection) {
        biConsumer.accept(sections, targetSection);
    }
}
