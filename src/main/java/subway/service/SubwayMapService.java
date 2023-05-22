package subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.subwaymap.SectionMap;
import subway.domain.subwaymap.StationMap;
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
        final StationMap stationMap = getSubwayMap();
        final Set<Long> lineIds = stationMap.getAllLineIds();
        return lineIds.stream()
                .map(lineId -> getLineResponseWithStations(lineId, stationMap))
                .collect(Collectors.toList());
    }

    public LineResponseWithStations getLineResponseWithStations(final Long lineId) {
        final StationMap stationMap = getSubwayMap();
        return getLineResponseWithStations(lineId, stationMap);
    }

    private LineResponseWithStations getLineResponseWithStations(final Long lineId, final StationMap stationMap) {
        final Line line = stationMap.getLine(lineId);
        final List<Station> stations = stationMap.getStations(lineId).getStations();
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

    private StationMap getSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return StationMap.of(lines, sections);
    }

    private SectionMap getSectionMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SectionMap.of(lines, sections);
    }
}
