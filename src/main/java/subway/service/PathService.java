package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Price;
import subway.domain.Station;
import subway.domain.Subway;
import subway.domain.path.DijkstraShortestPathFinder;
import subway.domain.path.ShortestPath;
import subway.dto.PathDto;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final SubwayService subwayService;
    private final StationRepository stationRepository;

    public PathService(final SubwayService subwayService,
                       final StationRepository stationRepository) {
        this.subwayService = subwayService;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId, final int age) {
        final Subway subway = subwayService.findSubway();

        final Station source = stationRepository.findById(sourceId);
        final Station target = stationRepository.findById(targetId);

        // TODO: Service에서 new ShortestPath()를 생성해서 주입하고 있는데, 이 작업은 어디에서 하는 것이 좋을까요?
        final PathDto path = subway.findShortestPath(
                source,
                target,
                new ShortestPath(new DijkstraShortestPathFinder()));

        final double distance = path.distance();
        final List<StationResponse> stationResponses = path.stations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        final int price = Price
                .from(distance)
                .applyAge(age)
                .getPrice();

        return new PathResponse(stationResponses, price);
    }
}
