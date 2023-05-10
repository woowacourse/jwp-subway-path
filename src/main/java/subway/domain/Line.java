package subway.domain;

import subway.exception.DuplicateStationInLineException;
import subway.exception.NameLengthException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.LinkedList;
import java.util.List;

public class Line {

    public static final int MINIMUM_NAME_LENGTH = 2;
    public static final int MAXIMUM_NAME_LENGTH = 15;

    private String name;
    private LinkedList<Section> sections;

    public Line(String name, List<Section> sections) {
        String stripped = name.strip();
        validateNameLength(stripped);
        this.name = stripped;
        this.sections = new LinkedList<>(sections);
    }

    private void validateNameLength(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new NameLengthException("이름 길이는 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public Line(Line otherLine) {
        this(otherLine.getName(), otherLine.getSections());
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return new LinkedList<>(sections);
    }

    public List<Section> addStation(Station newStation, Station upstream, Station downstream, int distanceToUpstream) {
        validateDuplicateStations(newStation);
        validateCorrespondingSection(upstream, downstream);
        return null;
    }

    private void validateCorrespondingSection(Station upstream, Station downstream) {
        if (hasCorrespondingSection(upstream, downstream)) {
            throw new SectionNotFoundException("노선에 해당하는 구간이 존재하지 않습니다.");
        }
    }

    private boolean hasCorrespondingSection(Station upstream, Station downstream) {
        return sections.stream()
                .noneMatch(section -> section.isCorrespondingSection(upstream, downstream));
    }

    private void validateDuplicateStations(Station newStation) {
        if (hasStation(newStation)) {
            throw new DuplicateStationInLineException("노선에 이미 존재하는 역입니다.");
        }
    }

    public void deleteStation(Station stationToDelete) {
        validateStationExist(stationToDelete);
    }

    private void validateStationExist(Station stationToDelete) {
        if (!hasStation(stationToDelete)) {
            throw new StationNotFoundException("노선에 존재하지 않는 역입니다.");
        }
    }

    private boolean hasStation(Station newStation) {
        return sections.stream()
                .anyMatch(section -> section.contains(newStation));
    }
}
