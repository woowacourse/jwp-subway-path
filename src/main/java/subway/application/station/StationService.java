package subway.application.station;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.station.dto.StationDto;
import subway.error.exception.StationNotFoundException;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

@Service
@Transactional
public class StationService {
	private final StationDao stationDao;

	public StationService(StationDao stationDao) {
		this.stationDao = stationDao;
	}

	public StationDto saveStation(final StationDto stationDto) {
		final StationEntity addStation = new StationEntity(stationDto.getName());

		final StationEntity stationEntity = stationDao.insert(addStation);
		return new StationDto(stationEntity);
	}

	@Transactional(readOnly = true)
	public List<StationDto> findAllStation() {
		return stationDao.findAll().stream()
			.map(StationDto::new)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public StationDto findStationById(final Long id) {
		final StationEntity stationEntity = stationDao.findById(id)
			.orElseThrow(() -> StationNotFoundException.EXCEPTION);
		return new StationDto(stationEntity);
	}

	public void updateStation(final Long id, final StationDto stationDto) {
		final StationEntity newStation = new StationEntity(id, stationDto.getName());
		stationDao.update(newStation);
	}

	public void deleteStationById(Long id) {
		stationDao.deleteById(id);
	}

}
