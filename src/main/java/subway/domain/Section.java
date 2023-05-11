package subway.domain;

public class Section {

    private Long id;
    private Station departure;
    private Station arrival;
    private Distance distance;

    public Section() {
    }

    public Section(final Long id, final Station departure, final Station arrival, final Distance distance) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getDeparture() {
        return departure;
    }

    public Station getArrival() {
        return arrival;
    }

    public Distance getDistance() {
        return distance;
    }

}
