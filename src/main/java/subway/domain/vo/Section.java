package subway.domain.vo;

public class Section {

    private final Long id;
    private final Station departure;
    private final Station arrival;
    private final Distance distance;


    public Section(final Long id, final Station departure, final Station arrival, final Distance distance) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.distance = distance;
    }

    public Section(final Long id, final String departure, final String arrival, final int distance) {
        this.id = id;
        this.departure = new Station(departure);
        this.arrival = new Station(arrival);
        this.distance = new Distance(distance);
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

    public String getDepartureValue() {
        return departure.getName();
    }

    public String getArrivalValue() {
        return arrival.getName();
    }

    public int getDistanceValue() {
        return distance.getDistance();
    }
}
