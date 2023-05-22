package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import subway.domain.vo.Distance;
import subway.exception.BusinessException;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Long charge;
    private final Sections sections;

    public Line(final Long id, final String name, final String color, final Long charge, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.charge = charge;
        this.sections = sections;
    }

    public Line(final String name, final String color, final Long charge) {
        this(null, name, color, charge, new Sections(new ArrayList<>()));
    }

    public Line(final Long id, final String name, final String color, final Long charge) {
        this(id, name, color, charge, new Sections(new ArrayList<>()));
    }

    public Line(final String name, final String color, final Long charge, final Sections sections) {
        this(null, name, color, charge, sections);
    }

    public Line addInitStations(final Station upStation, final Station downStation, final Distance distance) {
        checkSectionsNotEmpty();
        final Section section = new Section(upStation, downStation, distance);
        final Sections addedSections = sections.addAll(section);
        return new Line(id, name, color, charge, addedSections);
    }

    public Line addTopStation(final Station station, final Distance distance) {
        checkSectionsEmpty();
        final Station currentTopStation = sections.findTopStation();
        final Section section = new Section(station, currentTopStation, distance);
        final Sections addedSections = sections.addTop(section);
        return new Line(id, name, color, charge, addedSections);
    }

    public Line addBottomStation(final Station station, final Distance distance) {
        checkSectionsEmpty();
        final Station currentBottomStation = sections.findBottomStation();
        final Section section = new Section(currentBottomStation, station, distance);
        final Sections addedSections = sections.addBottom(section);
        return new Line(id, name, color, charge, addedSections);
    }

    public Line addBetweenStation(final Station addStation, final Station upStation, final Station downStation,
        final Distance distance) {
        checkSectionsEmpty();
        final Section existedSection = sections.findSection(upStation, downStation);
        final Sections removedSections = sections.remove(existedSection);
        final Section upSection = new Section(existedSection.getUpStation(), addStation, distance);
        final Section downSection = new Section(addStation, existedSection.getDownStation(),
            existedSection.getDistance().minus(distance));
        final Sections addedSections = removedSections.addAll(upSection, downSection);
        return new Line(id, name, color, charge, addedSections);
    }

    private void checkSectionsEmpty() {
        if (sections.isEmpty()) {
            throw new BusinessException("호선에 최소 2개의 역이 필요합니다.");
        }
    }

    private void checkSectionsNotEmpty() {
        if (!sections.isEmpty()) {
            throw new BusinessException("빈 호선이 아닙니다.");
        }
    }

    public Line removeStation(final Station station) {
        final Sections removedSections = sections.remove(station);
        return new Line(id, name, color, charge, removedSections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        if (getId() == null || line.id == null) {
            return Objects.equals(getName(), line.getName());
        }
        return Objects.equals(getId(), line.getId()) && Objects.equals(getName(), line.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public List<Section> getAllSection() {
        return List.copyOf(sections.getValue());
    }

    public int getStationsSize() {
        return sections.getStationsSize();
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
