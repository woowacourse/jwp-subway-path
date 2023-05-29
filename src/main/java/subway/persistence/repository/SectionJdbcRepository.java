package subway.persistence.repository;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.SectionRepository;
import subway.domain.line.Station;
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
		final SectionEntity newSectionEntity = SectionEntity.of(lineId, newSection);

		final SectionEntity result = sectionDao.insert(newSectionEntity);
		final Map<Long, Station> stationMap = getStationMap();

		return convertToSection(result, stationMap);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Section> findSectionsByLineId(final Long lineId) {
		final Map<Long, Station> stationMap = getStationMap();
		final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

		return sectionEntities.stream()
			.map(sectionEntity -> convertToSection(sectionEntity, stationMap))
			.collect(toList());
	}

	@Override
	public void removeSection(final Section section) {
		sectionDao.deleteById(section.getId());
	}

	private Map<Long, Station> getStationMap() {
		return stationDao.findAll().stream()
			.collect(toMap(StationEntity::getId, this::convertToStation));
	}

	private Station convertToStation(final StationEntity stationEntity) {
		return new Station(stationEntity.getId(), stationEntity.getName());
	}

	private Section convertToSection(final SectionEntity section, final Map<Long, Station> stationMap) {
		return new Section(section.getId(), stationMap.get(section.getDepartureId()),
			stationMap.get(section.getArrivalId()), new Distance(section.getDistance()));
	}
}
