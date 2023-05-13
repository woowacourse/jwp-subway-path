package subway.line.service;

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

import java.util.List;
import java.util.Optional;
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
        Optional<Line> line = lineDao.findByName(request.getName());
        if(line.isPresent()){
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        final Line findLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(findLine);
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
