package subway.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import subway.dao.StationDao;
import subway.domain.subway.Station;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

@Repository
public class StationRepository {

	private final StationDao stationDao;

	public StationRepository(final StationDao stationDao) {
		this.stationDao = stationDao;
	}

	public long insertStation(final Station station) {
		return stationDao.insert(station);
	}

	public List<Station> findAll() {
		List<StationEntity> stationEntities = stationDao.findAll();
		return stationEntities.stream()
			.map(station -> new Station(station.getStationId(), station.getName()))
			.collect(Collectors.toList());
	}

	public Station findByStationId(final Long stationId) {
		StationEntity stationEntity = stationDao.findById(stationId)
			.orElseThrow(StationNotFoundException::new);
		return new Station(stationEntity.getStationId(), stationEntity.getName());
	}

	public void update(final long id, final Station station) {
		stationDao.update(id, station);
	}
}
