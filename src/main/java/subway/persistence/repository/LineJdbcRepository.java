package subway.persistence.repository;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.domain.line.Station;
import subway.error.exception.LineNotFoundException;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
@Transactional
public class LineJdbcRepository implements LineRepository {

	private final LineDao lineDao;
	private final SectionDao sectionDao;
	private final StationDao stationDao;

	public LineJdbcRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
		this.lineDao = lineDao;
		this.sectionDao = sectionDao;
		this.stationDao = stationDao;
	}

	@Override
	public Line addLine(final Line line) {
		final LineEntity insert = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
		return new Line(insert.getId(), insert.getName(), insert.getColor());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Line> findLines() {
		final List<StationEntity> allStations = stationDao.findAll();
		final List<SectionEntity> sectionEntities = sectionDao.findAll();
		final List<LineEntity> lineEntities = lineDao.findAll();

		final Map<Long, Station> stationMap = mapToStations(allStations);
		final Map<Long, List<Section>> sections = sectionEntities.stream()
			.collect(groupingBy(SectionEntity::getLineId,
				mapping(sectionEntity -> convertToSection(sectionEntity, stationMap), toList())));

		return lineEntities.stream()
			.map(lineEntity -> convertToLine(sections, lineEntity))
			.collect(toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Line findLineById(final Long id) {
		final List<StationEntity> allStations = stationDao.findAll();
		final List<SectionEntity> lineSections = sectionDao.findByLineId(id);
		final Optional<LineEntity> optionalLineEntity = lineDao.findById(id);

		final Map<Long, Station> stationMap = mapToStations(allStations);
		final List<Section> sections = lineSections.stream()
			.map(sectionEntity -> convertToSection(sectionEntity, stationMap))
			.collect(toList());

		final LineEntity lineEntity = optionalLineEntity
			.orElseThrow(LineNotFoundException::new);

		return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections));
	}

	@Override
	public void updateLine(final Line line) {
		final LineEntity lineEntity = new LineEntity(line.getId(), line.getName(), line.getColor());
		lineDao.update(lineEntity);
	}

	@Override
	public void removeLine(final Long id) {
		lineDao.deleteById(id);
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
			stationMap.get(section.getArrivalId()), new Distance(section.getDistance()));
	}

	private Line convertToLine(final Map<Long, List<Section>> sections, final LineEntity lineEntity) {
		return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
			new Sections(sections.getOrDefault((lineEntity.getId()), Collections.emptyList())));
	}

}
