package subway.application;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.ui.dto.response.LineResponse;
import subway.ui.dto.request.SectionRequest;
import subway.ui.dto.response.SectionResponse;
import subway.ui.dto.response.StationResponse;

import java.util.List;

@Service
public class SectionService {
	private final SectionRepository sectionRepository;
	private final LineRepository lineRepository;

	public SectionService(final SectionRepository sectionRepository, final LineRepository lineRepository) {
		this.sectionRepository = sectionRepository;
		this.lineRepository = lineRepository;
	}

	public SectionResponse createSection(final SectionRequest sectionRequest) {
		final Section section = Section.of(
			sectionRequest.getLineName(),
			sectionRequest.getUpStationName(),
			sectionRequest.getDownStationName(),
			sectionRequest.getDistance()
		);
		final Sections sections = new Sections(sectionRepository.findAllByLineName(section.getLine().getName()));
		sections.addSection(section);
		sectionRepository.createSection(section.getLine().getName(), sections.getSections());
		final Long sectionId = sectionRepository.findIdByUpDown(section.getUpStation().getName(),
			section.getDownStation().getName()).getId();
		return new SectionResponse(sectionId, section.getLine().getName(), section.getUpStation().getName(),
			section.getDownStation().getName(), section.getDistance());
	}

	public List<SectionResponse> findAll(){
		return SectionResponse.of(sectionRepository.findAll());
	}

	public List<StationResponse> findAllByLine(final Long lineId) {
		final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));

		return StationResponse.of(sections.getSortedStations());
	}

	public void deleteSection(final Long lineId, final LineResponse.SectionDeleteRequest sectionDeleteRequest) {
		final Section section = Section.of(
			sectionDeleteRequest.getUpStationName(),
			sectionDeleteRequest.getDownStationName()
		);

		sectionRepository.deleteBySection(lineId, section);
	}
}
