package subway.domain;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.section.add.AddSectionHandler;
import subway.domain.section.add.strategy.AddSectionStrategy;
import subway.domain.section.delete.DeleteStationHandler;
import subway.domain.section.delete.strategy.DeleteStationStrategy;

import java.util.List;
import java.util.regex.Pattern;

public class Line {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣0-9]*$");
    private static final Pattern COLOR_PATTERN = Pattern.compile("^bg-[a-z]{3,7}-\\d{2,3}$");
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 9;

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    private Line(final Long id, final String name, final String color, final Sections sections) {
        validateLineInfo(name, color);

        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line of(final String name, final String color) {
        return new Line(null, name, color, Sections.create());
    }

    public static Line of(final Long id, final String name, final String color) {
        return new Line(id, name, color, Sections.create());
    }

    public static Line of(final Long id, final String name, final String color, final Section section) {
        return new Line(id, name, color, Sections.from(section));
    }

    private void validateLineInfo(final String name, final String color) {
        validateNameFormat(name);
        validateNameLength(name);
        validateColorFormat(color);
    }

    private void validateNameFormat(final String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("노선 이름은 한글과 숫자만 가능합니다");
        }
    }

    private void validateNameLength(final String name) {
        if (!(MINIMUM_LENGTH <= name.length() && name.length() <= MAXIMUM_LENGTH)) {
            throw new IllegalArgumentException("노선 이름은 2글자 ~ 9글자만 가능합니다");
        }
    }

    private void validateColorFormat(final String color) {
        if (!COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalArgumentException("노선 색깔은 tailwindcss 형식만 가능합니다");
        }
    }

    public void addSection(final Section section) {
        validateSameSection(section);

        final AddSectionStrategy addSectionStrategy = AddSectionHandler.bind(sections, section);
        addSectionStrategy.addSection(sections, section);
    }

    private void validateSameSection(final Section section) {
        if (sections.hasSameSection(section)) {
            throw new IllegalArgumentException("이미 등록된 구간입니다.");
        }
    }

    public void deleteStation(final Station station) {
        final DeleteStationStrategy deleteStationStrategy = DeleteStationHandler.bind(sections, station);
        deleteStationStrategy.deleteSection(sections, station);
    }

    public List<Station> findStationsByOrdered() {
        return sections.getStations();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }
}
