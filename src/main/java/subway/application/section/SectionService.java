package subway.application.section;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.section.dto.SectionDto;
import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.SectionRepository;
import subway.domain.line.Sections;
import subway.domain.line.Station;
import subway.domain.line.StationRepository;
import subway.domain.line.command.Result;
import subway.error.exception.SectionConnectionException;
import subway.error.exception.StationNotFoundException;

@Service
@Transactional
public class SectionService {

	private final SectionRepository sectionRepository;
	private final StationRepository stationRepository;

	public SectionService(final SectionRepository sectionRepository, final StationRepository stationRepository) {
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
	}

	public SectionDto addByLineId(final Long lineId, final SectionDto sectionDto) {
		final List<Section> lineSections = sectionRepository.findSectionsByLineId(lineId);
		final Section addSection = createNewSection(sectionDto);

		final Sections sections = new Sections(lineSections);
		final Result result = sections.addStation(addSection);

		result.execute(lineId, sectionRepository::addSection, sectionRepository::removeSection);
		return SectionDto.from(addSection);
	}

	public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
		final List<Section> lineSections = sectionRepository.findSectionsByLineId(lineId);
		final Station removeStation = stationRepository.findById(stationId)
			.orElseThrow(() -> StationNotFoundException.EXCEPTION);

		final Sections sections = new Sections(lineSections);
		final Result result = sections.removeStation(removeStation);

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
		return stationRepository.findByNames(departure)
			.orElseGet(() -> stationRepository.addStation(departure));
	}

}
