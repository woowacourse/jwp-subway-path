package subway.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

@Repository
public class SectionRepository {
	private final LineDao lineDao;
	private final SectionDao sectionDao;
	private final StationDao stationDao;

	public SectionRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
		this.lineDao = lineDao;
		this.sectionDao = sectionDao;
		this.stationDao = stationDao;
	}

	public Sections findSectionsByLineName(final String lineName) {
		LineEntity lineEntity = lineDao.findByName(lineName);
		return getSections(lineEntity);
	}

	private Sections getSections(final LineEntity lineEntity) {
		List<SectionEntity> sectionEntities = sectionDao.findSectionsByLineId(lineEntity.getLineId());

		return sectionEntities.stream()
			.map(this::getSection)
			.collect(Collectors.collectingAndThen(Collectors.toList(), Sections::new));
	}

	private Section getSection(final SectionEntity sectionEntity) {
		StationEntity upStationEntity = stationDao.findById(sectionEntity.getUpStationId())
			.orElseThrow(StationNotFoundException::new);
		StationEntity downStationEntity = stationDao.findById(sectionEntity.getDownStationId())
			.orElseThrow(StationNotFoundException::new);

		Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());
		Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());
		return new Section(upStation, downStation, sectionEntity.getDistance());
	}
}
