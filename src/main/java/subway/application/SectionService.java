package subway.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.core.Section;
import subway.domain.core.Sections;
import subway.domain.core.Station;
import subway.dto.request.SectionDeleteRequest;
import subway.dto.request.SectionUpdateRequest;
import subway.dto.response.SectionResponse;
import subway.dto.response.StationResponse;

@Service
@Transactional
public class SectionService {
	private final SectionDao sectionDao;
	private final LineDao lineDao;
	private final StationDao stationDao;

	public SectionService(final SectionDao sectionDao, final LineDao lineDao,
		final StationDao stationDao) {
		this.sectionDao = sectionDao;
		this.lineDao = lineDao;
		this.stationDao = stationDao;
	}

	public SectionResponse createSection(final SectionUpdateRequest sectionUpdateRequest) {
		final Sections sections = new Sections(sectionDao.findAllByLineName(sectionUpdateRequest.getLineName()));
		final Section section = Section.of(
			sectionUpdateRequest.getLineName(),
			sectionUpdateRequest.getUpStationName(),
			sectionUpdateRequest.getDownStationName(),
			sectionUpdateRequest.getDistance()
		);
		sections.addSection(section);
		sectionDao.createSection(sectionUpdateRequest.getLineName(), sections.getSections());
		final Long sectionId = sectionDao.findIdByUpDown(sectionUpdateRequest.getUpStationName(),
			sectionUpdateRequest.getDownStationName()).getId();
		return new SectionResponse(sectionId, section.getLine().getName(), section.getUpStation().getName(),
			section.getDownStation().getName(), section.getDistance());
	}

	public List<SectionResponse> findAll() {
		return SectionResponse.of(sectionDao.findAll());
	}

	public List<StationResponse> findAllByLine(final long lineId) {
		final String lineName = lineDao.findById(lineId).getName();
		final Sections sections = new Sections(sectionDao.findAllByLineName(lineName));
		final List<Station> sortedStations = sections.getSortedStations();

		List<Station> stations = new ArrayList<>();
		for (Station station : sortedStations) {
			stations.add(stationDao.findStationWithId(station));
		}
		return StationResponse.of(stations);
	}

	public void deleteSection(final long lineId, final SectionDeleteRequest deleteRequest) {
		final String lineName = lineDao.findById(lineId).getName();
		sectionDao.deleteBySection(lineName, deleteRequest.getUpStationName(),
			deleteRequest.getDownStationName());
	}
}
