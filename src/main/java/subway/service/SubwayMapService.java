package subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionMap;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.LineResponseWithSections;
import subway.dto.LineResponseWithStations;

@Service
public class SubwayMapService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public SubwayMapService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public List<LineResponseWithStations> getLineResponsesWithStations() {
        final SubwayMap subwayMap = getSubwayMap();
        final Set<Long> lineIds = subwayMap.getAllLineIds();
        return lineIds.stream()
                .map(lineId -> getLineResponseWithStations(lineId, subwayMap))
                .collect(Collectors.toList());
    }

    public LineResponseWithStations getLineResponseWithStations(final Long lineId) {
        final SubwayMap subwayMap = getSubwayMap();
        return getLineResponseWithStations(lineId, subwayMap);
    }

    private LineResponseWithStations getLineResponseWithStations(final Long lineId, final SubwayMap subwayMap) {
        final Line line = subwayMap.getLine(lineId);
        final List<Station> stations = subwayMap.getStations(lineId).getStations();
        return new LineResponseWithStations(line.getId(), line.getName(), line.getColor(), stations);
    }

    public List<LineResponseWithSections> getLineResponsesWithSections() {
        final SectionMap sectionMap = getSectionMap();
        final Set<Long> lineIds = sectionMap.getAllLineIds();
        return lineIds.stream()
                .map(lineId -> getLineResponseWithSections(lineId, sectionMap))
                .collect(Collectors.toList());
    }

    public LineResponseWithSections getLineResponseWithSections(final Long lineId) {
        final SectionMap sectionMap = getSectionMap();
        return getLineResponseWithSections(lineId, sectionMap);
    }

    private LineResponseWithSections getLineResponseWithSections(final Long lineId, final SectionMap sectionMap) {
        final Line line = sectionMap.getLine(lineId);
        final List<Section> sections = sectionMap.getSections(lineId).getSections();
        return new LineResponseWithSections(line.getId(), line.getName(), line.getColor(), sections);
    }

    private SubwayMap getSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SubwayMap.of(lines, sections);
    }

    private SectionMap getSectionMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SectionMap.of(lines, sections);
    }
}
