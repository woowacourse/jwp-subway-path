package subway.repository;

import org.springframework.stereotype.Repository;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Line;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

	private final LineDao lineDao;
	private final SectionDao sectionDao;
	private final StationDao stationDao;

	public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
		this.lineDao = lineDao;
		this.sectionDao = sectionDao;
		this.stationDao = stationDao;
	}

	public String insertLine(final LineEntity lineEntity) {
		return lineDao.insert(lineEntity);
	}

	public List<LineEntity> findAll() {
		return lineDao.findAll();
	}

	public void deleteLineById(final Long id) {
		sectionDao.deleteByLineId(id);
		lineDao.deleteById(id);
	}

	public LineEntity findLineById(final Long id) {
		return lineDao.findById(id);
	}

	public Line findLineWithSections(final String lineName, final Sections sections) {
		LineEntity lineEntity = lineDao.findByName(lineName);
		return new Line(sections, lineEntity.getName());
	}

	public void insertSectionInLine(final Sections sections, final String lineName) {
		LineEntity lineEntity = lineDao.findByName(lineName);
		List<SectionEntity> sectionEntities = sections.getSections().stream()
			.map(section -> {
				Station upStation = section.getUpStation();
				Station downStation = section.getDownStation();
				StationEntity upStationEntity = stationDao.findByName(upStation.getName())
					.orElseThrow(StationNotFoundException::new);
				StationEntity downStationEntity = stationDao.findByName(downStation.getName())
					.orElseThrow(StationNotFoundException::new);

				return new SectionEntity(null, lineEntity.getLineId(), upStationEntity.getStationId(),
					downStationEntity.getStationId(), section.getDistance());
			})
			.collect(Collectors.toList());

		sectionDao.deleteByLineId(lineEntity.getLineId());
		sectionDao.insertBatch(sectionEntities);
	}

	public void updateLine(final String lineName, final LineEntity lineEntity) {
		lineDao.updateLine(lineName, lineEntity);
	}

	public long findIdByName(final String lineName) {
		return lineDao.findByName(lineName).getLineId();
	}

	public LineEntity findLineByName(final String lineName) {
		return lineDao.findByName(lineName);
	}
}
