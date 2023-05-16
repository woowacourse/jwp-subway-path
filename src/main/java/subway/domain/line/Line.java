package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.line.AlreadyExistStationException;
import subway.exception.line.InvalidDistanceException;
import subway.exception.line.NotDownBoundStationException;
import subway.exception.line.NotUpBoundStationException;

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
        this(
            id,
            new Name(name),
            new Color(color),
            new Sections(sections)
        );
    }

    public Line (final Long id, final String name, final String color) {
        this(
            id,
            new Name(name),
            new Color(color),
            new Sections(List.of())
        );
    }

    public Line(final String name, final String color) {
        this(
            null,
            new Name(name),
            new Color(color),
            new Sections(List.of())
        );
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isBoundStation(Station baseStation) {
        Station upBoundStation = sections.findUpBoundStation();
        Station downBoundStation = sections.findDownBoundStation();
        return baseStation.equals(upBoundStation) || baseStation.equals(downBoundStation);
    }

    public void addUpBoundStation(Station baseStation, Station station) {
        if (!baseStation.equals(sections.findUpBoundStation())) {
            throw new NotUpBoundStationException();
        }
        validateAlreadyExistStation(station);
    }

    public void addDownBoundStation(Station baseStation, Station station) {
        if (!baseStation.equals(sections.findDownBoundStation())) {
            throw new NotDownBoundStationException();
        }
        validateAlreadyExistStation(station);
    }

    public void addInterStation(Station baseStation, Station station, String direction, int distance) {
        validateAlreadyExistStation(station);
        Section section =  sections.findSection(baseStation, direction);
        if (section.isShort(distance)) {
            throw new InvalidDistanceException();
        }
    }

    private void validateAlreadyExistStation(Station station) {
        if(sections.isContainStation(station)) {
            throw new AlreadyExistStationException();
        }
    }

    public Section findSection(Station baseStation, String direction) {
        return sections.findSection(baseStation, direction);
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
