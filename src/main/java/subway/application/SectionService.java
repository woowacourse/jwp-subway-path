package subway.application;

import org.springframework.stereotype.Service;

import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.repository.SectionRepository;
import subway.ui.dto.SectionResponse;
import subway.ui.dto.StationResponse;

import java.util.List;

@Service
public class SectionService {
	private final SectionRepository sectionRepository;

	public SectionService(final SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	public void createSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
		final Section section = Section.of(
			sectionCreateRequest.getUpStationName(),
			sectionCreateRequest.getDownStationName(),
			sectionCreateRequest.getDistance()
		);

		final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));
		sections.addSection(section);
		sectionRepository.createSection(lineId, sections.getSections());
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
