package subway.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Stations;
import subway.entity.StationEntity;

import java.util.Optional;

@Repository
public class SubwayRepository {

    public Stations getStations() {
        return null;
    }

    public void addIfNotExist(Station addStation) {

    }

    public Line getLineByName(String lineName) {
        return null;
    }

    public void addStation(Station stationToAdd) {
    }

    public Optional<Long> findStationIdByName() {
        return null;
    }

    public void updateLine(Line line) {

    }
}
