package subway.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.persistence.StationJdbcRepository;
import subway.ui.dto.request.SectionDeleteRequest;
import subway.ui.dto.request.SectionRequest;
import subway.ui.dto.response.SectionResponse;
import subway.ui.dto.response.StationResponse;

@Service
@Transactional
public class SectionService {
	private final SectionRepository sectionRepository;
	private final LineRepository lineRepository;
	private final StationJdbcRepository stationRepository;

	public SectionService(final SectionRepository sectionRepository, final LineRepository lineRepository,
		final StationJdbcRepository stationRepository) {
		this.sectionRepository = sectionRepository;
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public SectionResponse createSection(final SectionRequest sectionRequest) {
		final Sections sections = new Sections(sectionRepository.findAllByLineName(sectionRequest.getLineName()));
		final Section section = Section.of(
			sectionRequest.getLineName(),
			sectionRequest.getUpStationName(),
			sectionRequest.getDownStationName(),
			sectionRequest.getDistance()
		);
		sections.addSection(section);
		sectionRepository.createSection(sectionRequest.getLineName(), sections.getSections());
		final Long sectionId = sectionRepository.findIdByUpDown(sectionRequest.getUpStationName(),
			sectionRequest.getDownStationName()).getId();
		return new SectionResponse(sectionId, section.getLine().getName(), section.getUpStation().getName(),
			section.getDownStation().getName(), section.getDistance());
	}

	public List<SectionResponse> findAll() {
		return SectionResponse.of(sectionRepository.findAll());
	}

	public List<StationResponse> findAllByLine(final long lineId) {
		final String lineName = lineRepository.findById(lineId).getName();
		final Sections sections = new Sections(sectionRepository.findAllByLineName(lineName));
		final List<Station> sortedStations = sections.getSortedStations();

		List<Station> stations = new ArrayList<>();
		for (Station station : sortedStations) {
			stations.add(stationRepository.findStationWithId(station));
		}
		return StationResponse.of(stations);
	}

	public void deleteSection(final Long lineId, final SectionDeleteRequest deleteRequest) {
		final String lineName = lineRepository.findById(lineId).getName();
		sectionRepository.deleteBySection(lineName, deleteRequest.getUpStationName(),
			deleteRequest.getDownStationName());
	}
}
