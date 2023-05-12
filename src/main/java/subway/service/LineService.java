package subway.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSearchResponse;

@AllArgsConstructor
@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineSearchResponse> findLineResponses() {
        final List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(Line::getId)
                .map(this::findLineResponseById)
                .collect(Collectors.toList());
    }

    public LineSearchResponse findLineResponseById(final Long id) {
        final List<Section> sections = sectionDao.findSectionsByLineId(id);
        final SubwayMap subwayMap = SubwayMap.of(sections);
        final List<Station> stations = subwayMap.getStations();
        final Line line = lineDao.findById(id);
        return new LineSearchResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
