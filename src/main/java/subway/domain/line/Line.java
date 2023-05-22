package subway.domain.line;

import java.util.List;
import java.util.Objects;
import subway.domain.vo.Charge;
import subway.domain.vo.Distance;

public class Line {

    private Long id;
    private String name;
    private Charge extraCharge;
    private Sections sections;

    public Line(String name, Charge extraCharge) {
        this.name = name;
        this.extraCharge = extraCharge;
    }

    public Line(Long id, String name, Charge extraCharge, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.extraCharge = extraCharge;
        this.sections = new Sections(sections);
    }

    public Line(Long id, String name, Charge extraCharge, Sections sections) {
        this.id = id;
        this.name = name;
        this.extraCharge = extraCharge;
        this.sections = sections;
    }

    public Line(Long id, String name, Charge extraCharge) {
        this.id = id;
        this.name = name;
        this.extraCharge = extraCharge;
    }

    public static Line createLine(String name, Charge extraCharge, Station upStation, Station downStation,
                                  Distance distance) {
        Sections sections = Sections.createSections(new Section(upStation, downStation, distance));
        return new Line(null, name, extraCharge, sections);
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.addSection(upStation, downStation, distance);
    }

    public void deleteStation(Station station) {
        sections.deleteStation(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public List<Long> getStationIds() {
        return sections.getStationIds();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Charge getExtraCharge() {
        return extraCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", extraCharge=" + extraCharge +
                '}';
    }
}
