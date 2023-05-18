package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.line.AlreadyExistStationException;
import subway.exception.line.InvalidDistanceException;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private static final int EMPTY = 0;
    private static final int NOT_EXIST = 0;

    private Long id;
    private Name name;
    private Color color;
    private Sections sections;

    public Line() {
    }

    public Line(final Long id, final Name name, final Color color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line (final Long id, final String name, final String color, final List<Section> sections) {
        this(id, new Name(name), new Color(color), new Sections(sections));
    }

    public Line (final Long id, final String name, final String color) {
        this(id, new Name(name), new Color(color), new Sections(new ArrayList<>()));
    }

    public Line(final String name, final String color) {
        this(null, new Name(name), new Color(color), new Sections(new ArrayList<>()));
    }

    public Section addInitStations(Section section) {
        sections.addInitSection(section);
        return section;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isBoundStation(Station baseStation) {
        Station upBoundStation = sections.findUpBoundStation();
        Station downBoundStation = sections.findDownBoundStation();
        return baseStation.equals(upBoundStation) || baseStation.equals(downBoundStation);
    }

    public boolean isUpBoundStation(Station baseStation) {
        return sections.findUpBoundStation().equals(baseStation);
    }

    public boolean isDownBoundStation(Station baseStation) {
        return sections.findDownBoundStation().equals(baseStation);
    }

    public void validateDistanceLength(final Section section, final int distance) {
        if (section.isShort(distance)) {
            throw new InvalidDistanceException();
        }
    }

    public void validateAlreadyExistStation(Station station) {
        if(sections.isContainStation(station)) {
            throw new AlreadyExistStationException();
        }
    }

    public Section findSection(Station baseStation, String direction) {
        return sections.findSection(baseStation, direction);
    }

    public Section findSectionByBoundStation(Station boundStation) {
        return sections.findBoundSection(boundStation);
    }

    public List<Section> findSectionByInterStation(Station station) {
        return sections.findInterSections(station);
    }

    public Section updateSection(List<Section> section) {
        return sections.linkSections(section);
    }

    public boolean hasOneSection() {
        return sections.isSizeOne();
    }

    public List<Station> sortStation() {
        return sections.sortStation();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public Sections getSections() {
        return sections;
    }
}
