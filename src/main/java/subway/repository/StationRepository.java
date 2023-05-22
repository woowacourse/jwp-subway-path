package subway.repository;

import org.springframework.stereotype.Repository;

import subway.dao.StationDao;
import subway.domain.subway.Station;

@Repository
public class StationRepository {

	private final StationDao stationDao;

	public StationRepository(final StationDao stationDao) {
		this.stationDao = stationDao;
	}

	public long insertStation(final Station station) {
		return stationDao.insert(station);
	}
}
