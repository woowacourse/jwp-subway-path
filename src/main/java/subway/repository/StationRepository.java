package subway.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import subway.dao.StationDao;
import subway.domain.subway.Station;
import subway.entity.StationEntity;

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
}
