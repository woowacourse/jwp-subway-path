package subway.domain;

import java.util.Objects;

//TODO: getDistance() 제거 도전
public class Station {

    public static final Station emptyStation = new Station("");
    private Long id;
    private String name;
    private Station next = emptyStation;
    private Distance distance;

    public Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name, Station next, Distance distance) {
        validateSameStations(name, next);
        this.id = id;
        this.name = name;
        this.next = next;
        this.distance = distance;
    }

    public Station(String name, Station next, Distance distance) {
        validateSameStations(name, next);
        this.name = name;
        this.next = next;
        this.distance = distance;
    }

    public boolean isDownEndStation() {
        return next.equals(Station.emptyStation);
    }

    private void validateSameStations(String name, Station next) {
        if (next.name.equals(name)) {
            throw new IllegalArgumentException("상행역과 하행역은 같은 이름을 가질 수 없습니다.");
        }
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station getNext() {
        return next;
    }

    public void setNext(Station next) {
        this.next = next;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Distance minusDistance(Station station) {
        return distance.minus(station.distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
