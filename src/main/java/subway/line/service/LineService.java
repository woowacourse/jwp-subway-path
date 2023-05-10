package subway.line.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.dao.LineDao;
import subway.line.domain.Line;
import subway.line.domain.SubwayMap;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineSearchResponse;
import subway.section.dao.SectionDao;
import subway.section.domain.Section;
import subway.station.domain.Station;

@AllArgsConstructor
@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineSearchResponse findLineResponseById(final Long id) {
        final List<Section> sections = sectionDao.findSectionsByLineId(id);
        final SubwayMap subwayMap = SubwayMap.of(sections);
        final List<Station> stations = subwayMap.getStations();
        final Line line = lineDao.findById(id);
        return new LineSearchResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}
