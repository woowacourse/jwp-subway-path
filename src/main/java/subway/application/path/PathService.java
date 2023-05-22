package subway.application.path;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.path.dto.PathDto;
import subway.application.station.dto.StationDto;
import subway.domain.Distance;
import subway.domain.Fee;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.error.exception.SectionConnectionException;
import subway.error.exception.StationNotFoundException;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final SectionRepository sectionRepository;
	private final StationRepository stationRepository;
	private final PathFinder pathFinder;

	public PathService(final SectionRepository sectionRepository, final StationRepository stationRepository,
		final PathFinder pathFinder) {
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
		this.pathFinder = pathFinder;
	}

	public PathDto findShortestPath(final String departureName, final String arrivalName) {
		final Station departure = findDepartureStation(departureName);
		final Station arrival = findDepartureStation(arrivalName);

		validateSameStation(departure, arrival);

		final List<Section> allSections = findAllSections();

		pathFinder.initialize(allSections);
		final Path shortestPath = pathFinder.findShortestPath(departure, arrival);

		return convertToPathDto(shortestPath);
	}

	private void validateSameStation(final Station departure, final Station arrival) {
		if (departure.equals(arrival)) {
			throw new SectionConnectionException("같은 역은 연결될 수 없습니다.");
		}
	}

	private Station findDepartureStation(final String stationName) {
		return stationRepository.findByStationNames(stationName)
			.orElseThrow(() -> StationNotFoundException.EXCEPTION);
	}

	private List<Section> findAllSections() {
		return sectionRepository.findAllSections().values()
			.stream()
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	private PathDto convertToPathDto(final Path shortestPath) {
		final List<StationDto> stationDtos = shortestPath.getPath().stream()
			.map(StationDto::new)
			.collect(Collectors.toList());
		final Fee fee = Fee.Calculate(shortestPath.getDistance());
		final Distance distance = shortestPath.getDistance();

		return new PathDto(stationDtos, fee.getFee(), distance.getValue());
	}
}
