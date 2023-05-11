package subway.domain;

import subway.entity.StationEntity;

import java.util.List;
import java.util.Objects;
public class Station {

    public static final Station emptyStation = new Station("");
    private Long id;
    private String name;
    private Station next = emptyStation;
    private Distance distance;

    public Station() {
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

    public static Station from(List<StationEntity> entities, StationEntity headEntity) {
        validateMatchHeadEntity(entities, headEntity);
        StationEntity currentEntity = headEntity;
        Station headStation=Station.from(currentEntity);
        Station currentStation = headStation;
        connectStations(entities, currentEntity, currentStation);
        return headStation;
    }

    private static void connectStations(List<StationEntity> entities, StationEntity currentEntity,
        Station currentStation) {
        Station nextStation;
        StationEntity nextEntity;
        while (currentEntity.getNext() != 0L) {
            StationEntity finalCurrentEntity = currentEntity;
            nextEntity = entities.stream()
                .filter((e) -> e.getId() == finalCurrentEntity.getNext())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 노선에 존재하지 않습니다."));
            nextStation = Station.from(nextEntity);
            currentStation.setNext(nextStation);
            currentEntity =nextEntity;
            currentStation =nextStation;
        }
    }

    private static void validateMatchHeadEntity(List<StationEntity> entities, StationEntity headEntity) {
        if (!entities.contains(headEntity)) {
            throw new IllegalArgumentException("해당 노선의 상행 종점이 아닙니다.");
        }
    }

    private static Station from(StationEntity entity) {
        Distance distance = findRealDistance(entity);
        return new Station(entity.getId(), entity.getName(), emptyStation, distance);
    }

    private static Distance findRealDistance(StationEntity entity) {
        if (entity.getDistance() == 0) {
            return Distance.emptyDistance;
        }
        return new Distance(entity.getDistance());
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

    public void setNext(Station next) {
        this.next = next;
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

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Distance minusDistance(Station station) {
        return distance.minus(station.distance);
    }

    public Distance plusDistance(Station station) {
        return distance.plus(station.distance);
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
