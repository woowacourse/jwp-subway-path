package subway.domain;

public class Section {
    private final Long id;
    private final Line line;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(Long id, Line line, Station previousStation, Station nextStation, Distance distance) {
        this.id = id;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public static StationsBuilder builder() {
        return new StationsBuilder();
    }

    public static Section makeSectionToInsertDownDirection(Section newSection, Section originalSection) {
        return builder()
                .line(newSection.line)
                .startingStation(newSection.nextStation)
                .before(originalSection.nextStation)
                .distance(originalSection.distance.subtract(newSection.distance))
                .build();
    }

    public static Section makeSectionToUpdateDownDirection(Section newSection, Section originalSection) {
        return builder()
                .id(originalSection.id)
                .line(originalSection.line)
                .startingStation(originalSection.previousStation)
                .before(newSection.nextStation)
                .distance(newSection.distance)
                .build();
    }

    public static Section makeSectionToUpdateUpDirection(Section newSection, Section originalSection) {
        return builder()
                .id(originalSection.id)
                .line(newSection.line)
                .startingStation(originalSection.previousStation)
                .before(newSection.previousStation)
                .distance(originalSection.distance.subtract(newSection.distance))
                .build();
    }

    public static Section makeSectionToUpdateAfterDeletion(Section sectionLeft, Section sectionToDelete) {
        return builder()
                .id(sectionLeft.id)
                .startingStation(sectionLeft.previousStation)
                .before(sectionToDelete.nextStation)
                .distance(sectionToDelete.distance.add(sectionLeft.distance))
                .build();
    }

    public boolean isNextStationEmpty() {
        return nextStation.getName() == null;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }
}
