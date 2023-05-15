package subway.persistence.repository;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
public class SectionJdbcRepository implements SectionRepository {

	private final SectionDao sectionDao;
	private final StationDao stationDao;

	public SectionJdbcRepository(final SectionDao sectionDao, final StationDao stationDao) {
		this.sectionDao = sectionDao;
		this.stationDao = stationDao;
	}

	@Override
	public Map<Long, List<Section>> findAllSections() {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		return sectionDao.findAll().stream()
			.collect(groupingBy(SectionEntity::getLineId,
				mapping(sectionEntity -> convertToSection(sectionEntity, stationMap), toList())));
	}

	@Override
	public List<Section> findSectionsByLineId(final Long lineId) {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		return sectionDao.findByLineId(lineId).stream()
			.map(sectionEntity -> convertToSection(sectionEntity, stationMap))
			.collect(toList());
	}

	@Override
	public List<Section> findSectionsByLineIdAndStationId(final Long lineId, final Long stationId) {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());

		return sectionDao.findByLineId(lineId).stream()
			.filter(sectionEntity -> hasStation(stationId, sectionEntity))
			.map(sectionEntity -> convertToSection(sectionEntity, stationMap))
			.collect(toList());
	}

	@Override
	public Section findByStationNames(final String departure, final String arrival, final Integer distance) {
		final Station departureStation = convertToStation(stationDao.findByName(departure));
		final Station arrivalStation = convertToStation(stationDao.findByName(arrival));

		return new Section(null, departureStation, arrivalStation, new Distance(distance));
	}

	@Override
	public List<Section> addStation(final Long lineId, final List<Section> sections) {
		final Map<Long, Station> stationMap = mapToStations(stationDao.findAll());
		final SectionEntity upLineSection = sectionDao.insert(SectionEntity.of(lineId, sections.get(0)));

		if (sections.size() == 1) {
			return List.of(convertToSection(upLineSection, stationMap));
		}

		final SectionEntity downLineSection = sectionDao.insert(SectionEntity.of(lineId, sections.get(1)));
		sectionDao.deleteById(sections.get(0).getId());

		return List.of(convertToSection(upLineSection, stationMap), convertToSection(downLineSection, stationMap));
	}

	@Override
	public void removeStation(final Long lineId, final List<Section> sections) {
		if (sections.size() == 1) {
			sectionDao.deleteById(sections.get(0).getId());
			return;
		}
		sectionDao.deleteById(sections.get(0).getId());
		sectionDao.deleteById(sections.get(1).getId());
		sectionDao.insert(SectionEntity.of(lineId, sections.get(2)));
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
