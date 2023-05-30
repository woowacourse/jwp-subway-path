package subway.domain;

import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Stations {

    private final Set<Station> stations;

    public Stations(Set<Station> stations) {
        this.stations = new HashSet<>(stations);
    }

    public WeightedMultigraph<Station, Section> addStationsToGraph(WeightedMultigraph<Station, Section> graph) {
        stations.forEach(graph::addVertex);
        return graph;
    }

    public void addNewStation(Station newStation) {
        stations.add(newStation);
    }

    public Station findStationById(long stationId) {
        System.out.println("in stations : " + stationId);
        return stations.stream()
                .filter(station -> station.getId() == stationId)
                .findAny()
                .orElseThrow(() -> new StationNotFoundException("찾는 역이 없습니다."));
    }

    public Optional<Station> findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findAny();
    }

    @Override
    public String toString() {
        return "Stations{" +
                "stations=" + stations +
                '}';
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }
}
