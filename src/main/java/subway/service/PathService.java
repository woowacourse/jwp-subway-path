package subway.service;

import org.springframework.stereotype.Service;
import subway.dto.PathDto;
import subway.domain.Price;
import subway.domain.Station;
import subway.domain.Subway;
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

        final PathDto path = subway.findShortestPath(source, target);

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
