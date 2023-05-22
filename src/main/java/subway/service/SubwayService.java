package subway.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.subway.Sections;
import subway.domain.subway.Subway;
import subway.dto.response.LineStationResponse;
import subway.dto.response.StationResponse;
import subway.repository.SectionRepository;

@Service
public class SubwayService {

	private final SectionRepository sectionRepository;

	public SubwayService(final SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	@Transactional(readOnly = true)
	public LineStationResponse findStationsByLineName(final String lineName) {
		Sections sections = sectionRepository.findSectionsByLineName(lineName);
		Subway subway = Subway.from(sections);

		return subway.getSortedStations(sections).stream()
			.map(StationResponse::from)
			.collect(Collectors.collectingAndThen(Collectors.toList(), LineStationResponse::from));
	}
}
