package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import subway.domain.FareCalculator;
import subway.domain.Line;
import subway.domain.Navigation;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.SubwayFareCalculator;
import subway.dto.PathDto;
import subway.dto.response.PathResponse;
import subway.repository.SubwayRepository;

@Service
public class PathService {

    private final SubwayRepository subwayRepository;

    @Autowired
    public PathService(final SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public PathResponse findPath(final PathDto pathDto) {
        final Station source = subwayRepository.findStationByName(pathDto.getSourceStation());
        final Station target = subwayRepository.findStationByName(pathDto.getTargetStation());

        final List<Line> lines = subwayRepository.findSubway().getLines();
        final List<Sections> sections = lines.stream()
                .map(Line::getSections)
                .collect(Collectors.toUnmodifiableList());

        final Navigation navigation = Navigation.from(sections);
        final List<Station> stations = navigation.getShortestPath(source, target);

        final List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());

        final int distance = navigation.getDistance(source, target);
        final FareCalculator fareCalculator = new SubwayFareCalculator();

        return new PathResponse(stationNames, fareCalculator.calculate(distance), distance );
    }
}
