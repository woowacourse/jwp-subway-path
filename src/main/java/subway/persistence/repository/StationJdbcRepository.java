package subway.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.line.Station;
import subway.domain.line.StationRepository;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

@Repository
@Transactional
public class StationJdbcRepository implements StationRepository {

	private final StationDao stationDao;

	public StationJdbcRepository(final StationDao stationDao) {
		this.stationDao = stationDao;
	}

	@Override
	public Station addStation(final String stationName) {
		final StationEntity result = stationDao.insert(new StationEntity(stationName));
		return new Station(result.getId(), result.getName());
	}

	@Override
	public Optional<Station> findById(final Long id) {
		final Optional<StationEntity> optionalStationEntity = stationDao.findById(id);

		if (optionalStationEntity.isPresent()) {
			final StationEntity stationEntity = optionalStationEntity.get();
			return Optional.of(new Station(stationEntity.getId(), stationEntity.getName()));
		}
		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Station> findByNames(final String stationName) {
		final Optional<StationEntity> optionalStationEntity = stationDao.findByName(stationName);
		if (optionalStationEntity.isPresent()) {
			final StationEntity stationEntity = optionalStationEntity.get();
			return Optional.of(new Station(stationEntity.getId(), stationEntity.getName()));
		}
		return Optional.empty();
	}
}
