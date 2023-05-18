package subway.domain.line.service;

import org.springframework.stereotype.Service;
import subway.domain.line.domain.Line;
import subway.domain.line.domain.ShortestPath;
import subway.domain.lineDetail.entity.LineDetailEntity;
import subway.domain.lineDetail.service.LineDetailService;
import subway.domain.section.domain.SectionLocator;
import subway.domain.section.domain.SectionRouter;
import subway.domain.section.entity.SectionEntity;
import subway.domain.section.service.SectionService;
import subway.domain.station.entity.StationEntity;
import subway.domain.station.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDetailService lineDetailService;
    private final SectionService sectionService;
    private final StationService stationService;


    public LineService(final LineDetailService lineDetailService, final SectionService sectionService, final StationService stationService) {
        this.lineDetailService = lineDetailService;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    public Line findById(Long id) {
        List<SectionEntity> sectionDetails = sectionService.findByLineId(id);

        SectionLocator sectionLocator = SectionLocator.of(sectionDetails);
        SectionRouter sectionRouter = SectionRouter.of(sectionDetails);

        return createLine(id, sectionLocator, sectionRouter);
    }

    private Line createLine(Long lineId, SectionLocator sectionLocator, SectionRouter sectionRouter) {
        Long startStation = sectionLocator.findStartStation();
        Long endStation = sectionLocator.findEndStation();

        List<Long> shortestPath = sectionRouter.findShortestPath(startStation, endStation);
        LineDetailEntity lineDetail = lineDetailService.findLineById(lineId);

        List<StationEntity> stations = stationService.findStationsByIds(shortestPath);
        return new Line(lineDetail, stations);
    }

    public List<Line> findAll() {
        List<LineDetailEntity> lineDetails = lineDetailService.findAllLine();
        return lineDetails.stream()
                .map(LineDetailEntity::getId)
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
