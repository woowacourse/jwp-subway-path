package subway.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionRepository;
import subway.domain.FareCalculator;
import subway.domain.Line;
import subway.domain.LineStation;
import subway.domain.Sections;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.LineStationResponse;
import subway.dto.ShortestPathResponse;
import subway.exception.custom.LineNotExistException;

@Service
public class ShortestPathService {

    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public ShortestPathService(final LineDao lineDao, final SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public ShortestPathResponse getShortestPath(final String startStation, final String endStation, final int age) {
        final Station start = sectionRepository.findStationByName(startStation);
        final Station end = sectionRepository.findStationByName(endStation);

        final SubwayMap subwayMap = generateSubwayMap();
        final ShortestPath shortestPath = subwayMap.getShortestPath(start, end);
        final List<LineStation> path = new ArrayList<>(shortestPath.getPath());

        if (path.size() == 1) {
            System.out.println(path);
            final Station firstStation = path.get(0).getStation();
            final Line line = subwayMap.findLineContains(firstStation);
            path.set(0, LineStation.of(line, firstStation));
        }

        if (path.size() > 1) {
            path.set(0, LineStation.of(path.get(1).getLine(), path.get(0).getStation()));
        }

        final List<LineStationResponse> lineStationResponses = mapToLineStationResponse(path);
        final int totalDistance = shortestPath.getDistance();
        final int totalFare = FareCalculator.calculate(findMostExpensiveLine(path), totalDistance, age);

        return new ShortestPathResponse(lineStationResponses, totalDistance, totalFare);
    }

    private List<LineStationResponse> mapToLineStationResponse(final List<LineStation> path) {
        return path
            .stream()
            .map(LineStationResponse::of)
            .collect(Collectors.toList());
    }

    private Line findMostExpensiveLine(final List<LineStation> path) {
        return path.stream()
            .map(LineStation::getLine).min(Comparator.comparingInt(Line::getAdditionalFee))
            .orElseThrow(() -> new LineNotExistException("경로가 비어있습니다."));
    }


    private SubwayMap generateSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Sections> lineSections = lines.stream()
            .map((line) -> new Sections(sectionRepository.findAllByLineId(line.getId())))
            .collect(Collectors.toList());

        return new SubwayMap(lines, lineSections);
    }
}
