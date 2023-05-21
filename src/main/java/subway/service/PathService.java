package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.FareCalculator;
import subway.domain.Line;
import subway.domain.Navigation;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.SubwayFareCalculator;
import subway.service.dto.PathDto;
import subway.dto.response.PathResponse;
import subway.repository.SubwayRepository;

@Service
public class PathService {

    private final SubwayRepository subwayRepository;

    public PathService(final SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(final PathDto pathDto) {
        final Station source = new Station(pathDto.getSourceStation());
        final Station target = new Station(pathDto.getTargetStation());

        final List<Line> lines = subwayRepository.findSubway().getLines();
        final List<Sections> sections = lines.stream()
                .map(Line::getSections)
                .collect(Collectors.toList());

        final Navigation navigation = Navigation.from(sections);
        final List<Station> stations = navigation.getShortestPath(source, target);

        final int distance = navigation.getDistance(source, target);
        final FareCalculator fareCalculator = new SubwayFareCalculator();

        return PathResponse.from(fareCalculator.calculate(distance), distance, stations);
    }
}
