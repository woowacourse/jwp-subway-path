package subway.persistence.repository;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
@Transactional
public class SectionJdbcRepository implements SectionRepository {

	private final SectionDao sectionDao;
	private final StationDao stationDao;

	public SectionJdbcRepository(final SectionDao sectionDao, final StationDao stationDao) {
		this.sectionDao = sectionDao;
		this.stationDao = stationDao;
	}

	@Override
	public Section addSection(final Long lineId, final Section newSection) {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		final SectionEntity newSectionEntity = SectionEntity.of(lineId, newSection);
		final SectionEntity result = sectionDao.insert(newSectionEntity);

		return convertToSection(result, stationMap);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, List<Section>> findAllSections() {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		return sectionDao.findAll().stream()
			.collect(groupingBy(SectionEntity::getLineId,
				mapping(sectionEntity -> convertToSection(sectionEntity, stationMap), toList())));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Section> findSectionsByLineId(final Long lineId) {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		return sectionDao.findByLineId(lineId).stream()
			.map(sectionEntity -> convertToSection(sectionEntity, stationMap))
			.collect(toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Section> findSectionsByLineIdAndStationId(final Long lineId, final Long stationId) {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		return sectionDao.findByLineId(lineId).stream()
			.filter(sectionEntity -> hasStation(stationId, sectionEntity))
			.map(sectionEntity -> convertToSection(sectionEntity, stationMap))
			.collect(toList());
	}

	@Override
	public void removeSection(final Section section) {
		sectionDao.deleteById(section.getId());
	}

	private Map<Long, Station> mapToStations(final List<StationEntity> allStations) {
		return allStations.stream()
			.collect(toMap(StationEntity::getId, this::convertToStation));
	}

	private Station convertToStation(final StationEntity stationEntity) {
		return new Station(stationEntity.getId(), stationEntity.getName());
	}

	private Section convertToSection(final SectionEntity section, final Map<Long, Station> stationMap) {
		return new Section(section.getId(), stationMap.get(section.getDepartureId()),
			stationMap.get(section.getDepartureId()), new Distance(section.getDistance()));
	}

	private boolean hasStation(final Long stationId, final SectionEntity sectionEntity) {
		return Objects.equals(sectionEntity.getDepartureId(), stationId) || Objects.equals(sectionEntity.getArrivalId(),
			stationId);
	}
}
