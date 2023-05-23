package subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.subwaymap.SubwayMap;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSearchResponse;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public List<LineSearchResponse> getLineSearchResponses() {
        final SubwayMap subwayMap = getSubwayMap();
        final Set<Long> lineIds = subwayMap.getAllLineIds();
        return lineIds.stream()
                .map(lineId -> getLineSearchResponse(lineId, subwayMap))
                .collect(Collectors.toList());
    }

    public LineSearchResponse getLineSearchResponse(final Long lineId) {
        final SubwayMap subwayMap = getSubwayMap();
        return getLineSearchResponse(lineId, subwayMap);
    }

    private LineSearchResponse getLineSearchResponse(final Long lineId, final SubwayMap subwayMap) {
        final Line line = subwayMap.getLine(lineId);
        final List<Section> sections = subwayMap.getSections(lineId).getSections();
        return new LineSearchResponse(line.getId(), line.getName(), line.getColor(), sections);
    }

    private SubwayMap getSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SubwayMap.of(lines, sections);
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
