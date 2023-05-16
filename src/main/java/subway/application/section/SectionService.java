package subway.application.section;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.section.dto.SectionDto;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Sections.SectionsFactory;
import subway.domain.Sections.StationAddable;
import subway.domain.Sections.StationRemovable;

@Service
@Transactional
public class SectionService {

	private final SectionRepository sectionRepository;

	public SectionService(final SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	public List<SectionDto> addStationByLineId(final Long lineId, final SectionDto sectionDto) {
		final List<Section> lineSections = sectionRepository.findSectionsByLineId(lineId);
		final Section addSection = sectionRepository.findByStationNames(sectionDto.getDeparture(),
			sectionDto.getArrival(), sectionDto.getDistance());

		final StationAddable sections = SectionsFactory.createForAdd(lineSections, addSection);
		final List<Section> result = sections.addStation(addSection);

		return convertToDtos(sectionRepository.addStation(lineId, result));
	}

	@Transactional(readOnly = true)
	public Map<Long, List<SectionDto>> findAllSections() {
		return sectionRepository.findAllSections().entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> convertToDtos(entry.getValue())));
	}

	@Transactional(readOnly = true)
	public List<SectionDto> findSectionsByLineId(final Long lineId) {
		final List<Section> sections = sectionRepository.findSectionsByLineId(lineId);
		return convertToDtos(sections);
	}

	public void deleteSectionByLineIdAndStationId(final Long lineId, final Long stationId) {
		final List<Section> sections = sectionRepository.findSectionsByLineIdAndStationId(lineId, stationId);
		final StationRemovable removeSections = SectionsFactory.createForRemove(sections);
		final List<Section> result = removeSections.removeStation();
		sectionRepository.removeStation(lineId, result);
	}

	private List<SectionDto> convertToDtos(final List<Section> sections) {
		return sections.stream()
			.map(SectionDto::from)
			.collect(Collectors.toList());
	}
}
