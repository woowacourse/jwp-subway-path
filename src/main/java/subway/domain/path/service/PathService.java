package subway.domain.path.service;

import org.springframework.stereotype.Service;
import subway.domain.path.domain.LinePath;
import subway.domain.path.domain.ShortestPath;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.service.LineService;
import subway.domain.section.domain.SectionLocator;
import subway.domain.section.domain.SectionRouter;
import subway.domain.section.entity.SectionEntity;
import subway.domain.section.service.SectionService;
import subway.domain.station.entity.StationEntity;
import subway.domain.station.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final LineService lineService;
    private final SectionService sectionService;
    private final StationService stationService;


    public PathService(final LineService lineService, final SectionService sectionService, final StationService stationService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    public LinePath findById(Long id) {
        List<SectionEntity> sectionDetails = sectionService.findByLineId(id);

        SectionLocator sectionLocator = SectionLocator.of(sectionDetails);
        SectionRouter sectionRouter = SectionRouter.of(sectionDetails);

        return createLine(id, sectionLocator, sectionRouter);
    }

    private LinePath createLine(Long lineId, SectionLocator sectionLocator, SectionRouter sectionRouter) {
        Long startStation = sectionLocator.findStartStation();
        Long endStation = sectionLocator.findEndStation();

        List<Long> shortestPath = sectionRouter.findShortestPath(startStation, endStation);
        LineEntity lineDetail = lineService.findLineById(lineId);

        List<StationEntity> stations = stationService.findStationsByIds(shortestPath);
        return new LinePath(lineDetail, stations);
    }

    public List<LinePath> findAll() {
        List<LineEntity> lineDetails = lineService.findAllLine();
        return lineDetails.stream()
                .map(LineEntity::getId)
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public ShortestPath findShortestPath(Long startStationId, Long endStationId) {
        SectionRouter sectionRouter = SectionRouter.of(sectionService.findAll());
        double shortestDistance = sectionRouter.findShortestDistance(startStationId, endStationId);
        List<Long> shortestPath = sectionRouter.findShortestPath(startStationId, endStationId);

        List<StationEntity> stations = stationService.findStationsByIds(shortestPath);

        return new ShortestPath(stations, shortestDistance);
    }
}
