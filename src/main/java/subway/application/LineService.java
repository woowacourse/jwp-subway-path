package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.StationResponse;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line, createStationResponse(line)))
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stationResponses = createStationResponse(persistLine);
        return LineResponse.of(persistLine, stationResponses);
    }

    private List<StationResponse> createStationResponse(final Line persistLine) {
        List<Section> sections = sectionDao.findByLineId(persistLine.getId());
        return extractStationResponses(persistLine, sections);
    }

    private List<StationResponse> extractStationResponses(final Line persistLine, final List<Section> sections) {
        Subway subway = Subway.of(persistLine, sections);
        List<Station> orderedStations = subway.getOrderedStations();
        return orderedStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(final Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}
