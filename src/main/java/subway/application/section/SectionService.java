package subway.application.section;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.section.dto.SectionDto;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.command.Result;
import subway.error.exception.SectionConnectionException;

@Service
@Transactional
public class SectionService {

	private final SectionRepository sectionRepository;
	private final StationRepository stationRepository;

	public SectionService(final SectionRepository sectionRepository, final StationRepository stationRepository) {
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
	}

	public SectionDto addStationByLineId(final Long lineId, final SectionDto sectionDto) {
		final List<Section> lineSections = sectionRepository.findSectionsByLineId(lineId);

		final Sections sections = new Sections(lineSections);
		final Section addSection = createNewSection(sectionDto);

		final Result result = sections.addStation(addSection);
		result.execute(lineId, sectionRepository::addSection, sectionRepository::removeSection);

		return SectionDto.from(addSection);
	}

	@Transactional(readOnly = true)
	public Map<Long, List<SectionDto>> findAllSections() {
		return sectionRepository.findAllSections().entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
				entry -> convertToDtos(new Sections(entry.getValue()).getSections())));
	}

	@Transactional(readOnly = true)
	public List<SectionDto> findSectionsByLineId(final Long lineId) {
		final List<Section> sections = sectionRepository.findSectionsByLineId(lineId);
		final Sections result = new Sections(sections);
		return convertToDtos(result.getSections());
	}

	public void deleteSectionByLineIdAndStationId(final Long lineId, final Long stationId) {
		final List<Section> sections = sectionRepository.findSectionsByLineIdAndStationId(lineId, stationId);
		final Sections removeSections = new Sections(sections);

		final Result result = removeSections.removeStation();
		result.execute(lineId, sectionRepository::addSection, sectionRepository::removeSection);
	}

	private Section createNewSection(final SectionDto sectionDto) {
		final Station departureStation = getStation(sectionDto.getDeparture());
		final Station arriavalStation = getStation(sectionDto.getArrival());

		validateSameStation(departureStation, arriavalStation);

		return new Section(null, departureStation, arriavalStation, new Distance(sectionDto.getDistance()));
	}

	private void validateSameStation(final Station departure, final Station arrival) {
		if (departure.equals(arrival)) {
			throw new SectionConnectionException("같은 역은 연결될 수 없습니다.");
		}
	}

	private Station getStation(final String departure) {
		return stationRepository.findByStationNames(departure)
			.orElseGet(() -> stationRepository.addStation(departure));
	}

	private List<SectionDto> convertToDtos(final List<Section> sections) {
		return sections.stream()
			.map(SectionDto::from)
			.collect(Collectors.toList());
	}
}
