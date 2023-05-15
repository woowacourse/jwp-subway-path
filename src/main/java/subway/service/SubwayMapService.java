package subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.LineSearchResponse;

@Service
public class SubwayMapService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private SubwayMap subwayMap;

    public SubwayMapService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        update();
    }

    public synchronized void update() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        subwayMap = SubwayMap.of(lines, sections);
    }

    public List<LineSearchResponse> getLineSearchResponses() {
        final Set<Long> lineIds = subwayMap.getAllLineIds();
        return lineIds.stream()
                .map(this::getLineSearchResponse)
                .collect(Collectors.toList());
    }

    public LineSearchResponse getLineSearchResponse(final Long lineId) {
        final Line line = subwayMap.getLine(lineId);
        final List<Station> stations = subwayMap.getStations(lineId);
        return new LineSearchResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
