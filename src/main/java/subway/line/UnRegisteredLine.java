package subway.line;

import subway.line.application.exception.NotRegisteredLineException;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;
import java.util.Optional;

public class UnRegisteredLine extends Line {
    public UnRegisteredLine(String name, String color) {
        super(null, name, color);
    }

    @Override
    public Long getId() {
        throw new NotRegisteredLineException();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getColor() {
        return super.getColor();
    }

    @Override
    public Station getHead() {
        throw new NotRegisteredLineException();
    }

    @Override
    public List<Section> getSections() {
        throw new NotRegisteredLineException();
    }

    @Override
    public Optional<Section> findSectionByPreviousStation(Station station) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void addSections(List<Section> sections) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void addSection(Section section) {
        throw new NotRegisteredLineException();
    }

    @Override
    public boolean hasSection() {
        throw new NotRegisteredLineException();
    }

    @Override
    public Optional<Section> findSectionByNextStation(Station station) {
        throw new NotRegisteredLineException();
    }

    @Override
    public List<Station> findAllStationsOrderByUp() {
        throw new NotRegisteredLineException();
    }

    @Override
    public Distance findDistanceBetween(Station stationA, Station stationB) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void updateSection(Section section) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void changeHead(Station station) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void changeName(String name) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void changeColor(String color) {
        throw new NotRegisteredLineException();
    }

    @Override
    public void clearSection() {
        throw new NotRegisteredLineException();
    }

    @Override
    public int getStationsSize() {
        throw new NotRegisteredLineException();
    }

    @Override
    public void removeSection(Section section) {
        throw new NotRegisteredLineException();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
