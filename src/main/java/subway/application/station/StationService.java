package subway.application.station;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;
import subway.ui.dto.StationRequest;

@Service
@Transactional
public class StationService {
	private final StationDao stationDao;

	public StationService(StationDao stationDao) {
		this.stationDao = stationDao;
	}

	public StationEntity saveStation(final StationRequest stationRequest) {
		final StationEntity station = new StationEntity(stationRequest.getName());
		return stationDao.insert(station);
	}

	@Transactional(readOnly = true)
	public StationEntity findStationById(final Long id) {
		return stationDao.findById(id);
	}

	@Transactional(readOnly = true)
	public List<StationEntity> findAllStation() {
		return stationDao.findAll();
	}

	public void updateStation(Long id, StationRequest stationRequest) {
		final StationEntity newStation = new StationEntity(id, stationRequest.getName());
		stationDao.update(newStation);
	}

	public void deleteStationById(Long id) {
		stationDao.deleteById(id);
	}
}
