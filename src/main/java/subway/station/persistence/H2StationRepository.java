package subway.station.persistence;

import org.springframework.stereotype.Repository;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Repository
public class H2StationRepository implements StationRepository {

  private final StationDao stationDao;

  public H2StationRepository(StationDao stationDao) {
    this.stationDao = stationDao;
  }

  @Override
  public Station findById(Long id) {
    final StationEntity stationEntity = stationDao.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 존재하지 않습니다."));

    return new Station(stationEntity.getId(), stationEntity.getStationName());
  }

  @Override
  public Station createStation(String name) {
    final Station station = new Station(name);
    final Long stationId = stationDao.insert(new StationEntity(station.getName()));
    return new Station(stationId, station.getName());
  }

}
