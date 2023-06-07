package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.*;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.exception.LineAlreadyExistException;
import subway.exception.LineNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final SubwayGraphs subwayGraphs;
    private final LineDao lineDao;
    private final EdgeDao edgeDao;

    public LineService(final SubwayGraphs subwayGraphs, final LineDao lineDao, final EdgeDao edgeDao) {
        this.subwayGraphs = subwayGraphs;
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public LineResponse createLine(final LineCreateRequest lineCreateRequest) {
        final Line line = new Line(lineCreateRequest.getLineName());

        if (lineDao.findByName(line.getName()).isPresent()) {
            throw new LineAlreadyExistException();
        }

        final Line savedLine = lineDao.saveLine(line.toEntity()).toDomain();
        subwayGraphs.addLine(savedLine);

        return LineResponse.of(savedLine, List.of());
    }

    @Transactional
    public String deleteLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException())
                .toDomain();
        subwayGraphs.remove(line);
        edgeDao.deleteAllEdgesOf(lineId);
        lineDao.deleteLine(lineId);
        return line.getName();
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException())
                .toDomain();

        if (subwayGraphs.findSubwayGraph(line).getStationSize() == 0) {
            return LineResponse.of(line, List.of());
        }

        List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrder(line);
        List<StationResponse> stationResponses = allStationsInOrder.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());

        return LineResponse.of(line, stationResponses);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineDao.findAll().stream()
                .map(LineEntity::toDomain)
                .collect(Collectors.toList());

        List<LineResponse> lineResponses = new ArrayList<>();

        for (Line line : lines) {
            if (subwayGraphs.findSubwayGraph(line).getStationSize() == 0) {
                lineResponses.add(LineResponse.of(line, List.of()));
                continue;
            }
            List<Station> allStationsInOrder = subwayGraphs.findAllStationsInOrder(line);

            List<StationResponse> stationResponses = allStationsInOrder.stream()
                    .map(station -> StationResponse.of(station))
                    .collect(Collectors.toList());
            lineResponses.add(LineResponse.of(line, stationResponses));
        }
        return lineResponses;
    }
}
