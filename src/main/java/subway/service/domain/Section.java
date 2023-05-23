package subway.service.domain;

public class Section {

    private Long id;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(Station previousStation,
                   Station nextStation,
                   Distance distance) {
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public Section(long id,
                   Station previousStation,
                   Station nextStation,
                   Distance distance) {
        this.id = id;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public boolean isContainsStation(Station station) {
        return previousStation.equals(station)
                || nextStation.equals(station);
    }

    public boolean isPreviousStationStation(Station station) {
        return previousStation.equals(station);
    }

    public boolean isNextStationStation(Station station) {
        return nextStation.equals(station);
    }

    public Long getId() {
        return id;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Integer getDistance() {
        return distance.getValue();
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", previousStation=" + previousStation +
                ", nextStation=" + nextStation +
                ", distance=" + distance +
                '}';
    }

}
