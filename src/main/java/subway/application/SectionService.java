package subway.application;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.ui.dto.SectionResponse;
import subway.ui.dto.StationResponse;

import java.util.List;

@Service
public class SectionService {
	private final SectionRepository sectionRepository;
	private final LineRepository lineRepository;

	public SectionService(final SectionRepository sectionRepository, final LineRepository lineRepository) {
		this.sectionRepository = sectionRepository;
		this.lineRepository = lineRepository;
	}

	public SectionResponse createSection(final SectionCreateRequest sectionCreateRequest) {
		final Line line = lineRepository.findByName(sectionCreateRequest.getLineName());
		final Section section = Section.of(
			sectionCreateRequest.getLineName(),
			sectionCreateRequest.getUpStationName(),
			sectionCreateRequest.getDownStationName(),
			sectionCreateRequest.getDistance()
		);

		final Sections sections = new Sections(sectionRepository.findAllByLineId(line.getId()));
		sections.addSection(section);
		sectionRepository.createSection(line.getId(), sections.getSections());
		return SectionResponse.of(line.getId(), section.getUpStation().getName(), section.getDownStation().getName(),section.getDistance());
	}

	public List<SectionResponse> findAll(){
		return SectionResponse.of(sectionRepository.findAll());
	}

	public List<StationResponse> findAllByLine(final Long lineId) {
		final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));

		return StationResponse.of(sections.getSortedStations());
	}

	public void deleteSection(final Long lineId, final SectionDeleteRequest sectionDeleteRequest) {
		final Section section = Section.of(
			sectionDeleteRequest.getUpStationName(),
			sectionDeleteRequest.getDownStationName()
		);

		sectionRepository.deleteBySection(lineId, section);
	}
}
