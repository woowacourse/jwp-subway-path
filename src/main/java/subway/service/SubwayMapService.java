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

    public SubwayMapService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public List<LineSearchResponse> getLineSearchResponses() {
        final SubwayMap subwayMap = getSubwayMap();
        final Set<Long> lineIds = subwayMap.getAllLineIds();
        return lineIds.stream()
                .map(this::getLineSearchResponse)
                .collect(Collectors.toList());
    }

    public LineSearchResponse getLineSearchResponse(final Long lineId) {
        final SubwayMap subwayMap = getSubwayMap();
        final Line line = subwayMap.getLine(lineId);
        final List<Station> stations = subwayMap.getStations(lineId).getStations();
        return new LineSearchResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private SubwayMap getSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SubwayMap.of(lines, sections);
    }
}
