package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import subway.entity.LineEntity;

public class Line {
    private Long id;
    private String name;
    private String color;
    private Station headStation;


    public static Line from(LineEntity entity, Station headStation){
        return new Line(entity.getId(), entity.getName(), entity.getColor(), headStation);
    }
    public Line() {
    }

    public Line(Long id, String name, String color, Station headStation) {
        validate(headStation);
        //headStation이 emptyStation이면 안 된다
        this.id=id;
        this.name = name;
        this.color = color;
        this.headStation = headStation;
    }

    private void validate(Station headStation) {
        validateNotEmptyStation(headStation);
        validateHaveTwoStations(headStation);
    }

    private void validateNotEmptyStation(Station headStation) {
        if (headStation.equals(Station.emptyStation)) {
            throw new IllegalArgumentException("상행종점은 비어있을 수 없습니다.");
        }
    }

    private void validateHaveTwoStations(Station headStation) {
        if (headStation.isDownEndStation()) {
            throw new IllegalArgumentException("노선을 생성할 때 최소 2개 이상의 역이 존재해야 합니다.");
        }
    }

    public void addStation(Station station) {
        validateStation(station);
        Station upStation;
        Station downStation;

        if (hasStation(station.getName())) { //상행역이 이미 존재
            upStation = findStation(station.getName());
            downStation = station.getNext();
        } else { //하행역이 이미 존재
            upStation = station;
            downStation = findStation(station.getNext().getName());
        }

        Station lastStation = findDownEndStation();

        if (downStation.equals(headStation)) {
            headStation = station;
        } else if (upStation.equals(lastStation)) {
            lastStation.setNext(downStation);
            lastStation.setDistance(station.getDistance());
        } else {
            // 상행: 강남, 하행: newStation일 경우
            if (hasStation(station.getName())) {
                Distance newDistance = upStation.minusDistance(station);
                Station newStation = new Station(downStation.getName(), upStation.getNext(), newDistance);

                upStation.setNext(newStation);
                upStation.setDistance(station.getDistance());
            } else {
                // 상행: newStation, 하행: 역삼일 경우
                Station newStation2 = new Station(station.getName(), downStation, station.getDistance());
                Station foundStation = findPreviousStation(downStation.getName());

                Distance newDistance2 = foundStation.minusDistance(station);
                foundStation.setNext(newStation2);
                foundStation.setDistance(newDistance2);
            }
        }
    }

    private void validateStation(Station station) {
        validateExistStations(station);
    }

    private void validateExistStations(Station station) {
        if (hasStation(station.getName()) && hasStation(station.getNext().getName())) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }

    private Station findPreviousStation(String name) {
        Station station = headStation;
        while (!station.isDownEndStation()) {
            if (station.getNext().isSameName(name)) {
                return station;
            }
            station = station.getNext();
        }
        throw new IllegalArgumentException("해당 역의 상행 역이 존재하지 않습니다.");
    }

    private Station findDownEndStation() {
        int lastStationIndex = getStations().size() - 1;
        return getStations().get(lastStationIndex);
    }

    public Station findStation(String name) {
        List<Station> stations = getStations();
        return stations.stream()
                .filter((station) -> station.isSameName(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("노선에 존재하지 않는 역입니다."));
    }

    public boolean hasStation(String name) {
        List<Station> stations = getStations();
        return stations.stream()
                .anyMatch((station) -> station.isSameName(name));
    }

    public List<Station> getStations() {
        List<Station> stations = new LinkedList<>();
        Station station = headStation;

        do {
            stations.add(station);
            station = station.getNext();

        } while (station != null && !station.equals(Station.emptyStation));

        return stations;
    }

    public boolean hasSameName(Line line) {
        return name.equals(line.name);
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public int findSize() {
        return getStations().size();
    }

    public void removeStation(String name) {
        int size = findSize();
        Station downEndStation = getStations().get(size - 1);

        if (headStation.isSameName(name)) { //상행 종점 삭제
            headStation = headStation.getNext();
        } else if (downEndStation.getName().equals(name)) { //하행 종점 삭제
            Station previousStation = findPreviousStation(name);
            previousStation.setNext(Station.emptyStation);
        } else {
            // 역과 역 사이에 있는 역을 삭제
            if (hasStation(name)) {
                Station previousStation = findPreviousStation(name);
                Station station = findStation(name);
                Distance newDistance = previousStation.plusDistance(station);

                previousStation.setDistance(newDistance);
                previousStation.setNext(station.getNext());
            }

        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Station getHeadStation() {
        return headStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

}
