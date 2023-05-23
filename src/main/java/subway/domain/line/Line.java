package subway.domain.line;

import java.util.List;
import java.util.Objects;
import subway.domain.fare.Fare;

public class Line {

    private final Long id;
    private final String name;
    private final Fare extraFare;
    private final Sections sections;

    public Line(Long id, String name, Fare extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public boolean hasStation(Station station) {
        return sections.hasStation(station);
    }

    public boolean hasLeftStationInSection(Station station) {
        return sections.hasLeftStationInSection(station);
    }

    public boolean hasRightStationInSection(Station station) {
        return sections.hasRightStationInSection(station);
    }

    public Section findSectionByLeftStation(Station station) {
        return sections.findSectionByLeftStation(station);
    }

    public Section findSectionByRightStation(Station station) {
        return sections.findSectionByRightStation(station);
    }

    public boolean hasOneSection() {
        return sections.hasOneSection();
    }

    public boolean isLastStationAtLeft(Station station) {
        return sections.isLastStationAtLeft(station);
    }

    public boolean isLastStationAtRight(Station station) {
        return sections.isLastStationAtRight(station);
    }

    public List<Station> findLeftToRightRoute() {
        return sections.findLeftToRightRoute();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getExtraFare() {
        return extraFare.getValue();
    }

    public List<Section> getSections() {
        return sections.getSections();
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
        return Objects.equals(getId(), line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
