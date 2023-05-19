package subway.application.service.Route;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.route.dto.FindShortCutRequest;
import subway.application.dto.RouteResponse;
import subway.application.port.in.route.FindRouteResultUseCase;
import subway.application.port.out.graph.ShortPathPort;
import subway.application.port.out.section.SectionQueryPort;
import subway.application.port.out.station.StationQueryPort;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RouteQueryService implements FindRouteResultUseCase {

    private static final int FROM_STATION_INDEX = 0;
    private static final int TO_STATION_INDEX = 1;

    private final ShortPathPort shortPathPort;
    private final SectionQueryPort sectionQueryPort;
    private final StationQueryPort stationQueryPort;

    public RouteQueryService(final ShortPathPort shortPathPort, final SectionQueryPort sectionQueryPort, final StationQueryPort stationQueryPort) {
        this.shortPathPort = shortPathPort;
        this.sectionQueryPort = sectionQueryPort;
        this.stationQueryPort = stationQueryPort;
    }

    @Override
    public RouteResponse findResultShotCut(final FindShortCutRequest findShortCutRequest) {
        List<Station> routeRequest = createBy(findShortCutRequest);
        final List<Sections> allSections = sectionQueryPort.findAll().stream()
                .collect(Collectors.groupingBy(Section::getLineId)).values().stream()
                .map(Sections::new)
                .collect(Collectors.toList());
        if (allSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 구간을 추가해주세요");
        }

        return RouteResponse.from(
                shortPathPort.findSortPath(
                        routeRequest.get(FROM_STATION_INDEX),
                        routeRequest.get(TO_STATION_INDEX),
                        allSections)
        );
    }

    private List<Station> createBy(final FindShortCutRequest findShortCutRequest) {
        final List<Station> route = new ArrayList<>();

        route.add(stationQueryPort.findByName(new Station(findShortCutRequest.getFromStation()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다.")));
        route.add(stationQueryPort.findByName(new Station(findShortCutRequest.getToStation()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다.")));

        return route;
    }
}
