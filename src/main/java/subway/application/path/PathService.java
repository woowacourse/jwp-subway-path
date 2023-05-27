package subway.application.path;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.path.dto.PathDto;
import subway.application.station.dto.StationDto;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.domain.line.StationRepository;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.error.exception.SectionConnectionException;
import subway.error.exception.StationNotFoundException;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final PathFinder pathFinder;

	public PathService(final LineRepository lineRepository, final StationRepository stationRepository,
		final PathFinder pathFinder) {
		this.lineRepository = lineRepository;
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
		return stationRepository.findByNames(stationName)
			.orElseThrow(() -> StationNotFoundException.EXCEPTION);
	}

	private List<Section> findAllSections() {
		return lineRepository.findLines().stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	private PathDto convertToPathDto(final Path shortestPath) {
		final List<StationDto> stationDtos = shortestPath.getPath().stream()
			.map(StationDto::new)
			.collect(Collectors.toList());
		final Fare fare = new Fare(shortestPath.getDistance());

		final Distance distance = shortestPath.getDistance();

		return new PathDto(stationDtos, fare.calculate(), distance.getValue());
	}
}
