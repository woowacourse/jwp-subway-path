package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.LineMap;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    // TODO 반환타입만 빼면 RouteService와 중복 로직인데 어떻게 하면 좋을까?
    public List<LineResponse> findLineResponses() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line, createStationResponse(line)))
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stationResponses = createStationResponse(persistLine);
        return LineResponse.of(persistLine, stationResponses);
    }

    private List<StationResponse> createStationResponse(final Line persistLine) {
        List<Section> sections = sectionDao.findByLineId(persistLine.getId());
        return extractStationResponses(sections);
    }

    private List<StationResponse> extractStationResponses(final List<Section> sections) {
        LineMap lineMap = LineMap.of(sections);
        List<Station> orderedStations = lineMap.getOrderedStations();
        return orderedStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
