package subway.domain.edge;

import subway.domain.station.Station;

public class Edge {

    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Edge() {
    }

    public Edge(Long id, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Edge(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
