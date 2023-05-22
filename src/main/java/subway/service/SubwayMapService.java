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
import subway.domain.subwaymap.SubwaySectionMap;
import subway.domain.subwaymap.SubwayStationMap;
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
        final SubwayStationMap subwayStationMap = getSubwayMap();
        final Set<Long> lineIds = subwayStationMap.getAllLineIds();
        return lineIds.stream()
                .map(lineId -> getLineResponseWithStations(lineId, subwayStationMap))
                .collect(Collectors.toList());
    }

    public LineResponseWithStations getLineResponseWithStations(final Long lineId) {
        final SubwayStationMap subwayStationMap = getSubwayMap();
        return getLineResponseWithStations(lineId, subwayStationMap);
    }

    private LineResponseWithStations getLineResponseWithStations(final Long lineId, final SubwayStationMap subwayStationMap) {
        final Line line = subwayStationMap.getLine(lineId);
        final List<Station> stations = subwayStationMap.getStations(lineId).getStations();
        return new LineResponseWithStations(line.getId(), line.getName(), line.getColor(), stations);
    }

    public List<LineResponseWithSections> getLineResponsesWithSections() {
        final SubwaySectionMap subwaySectionMap = getSectionMap();
        final Set<Long> lineIds = subwaySectionMap.getAllLineIds();
        return lineIds.stream()
                .map(lineId -> getLineResponseWithSections(lineId, subwaySectionMap))
                .collect(Collectors.toList());
    }

    public LineResponseWithSections getLineResponseWithSections(final Long lineId) {
        final SubwaySectionMap subwaySectionMap = getSectionMap();
        return getLineResponseWithSections(lineId, subwaySectionMap);
    }

    private LineResponseWithSections getLineResponseWithSections(final Long lineId, final SubwaySectionMap subwaySectionMap) {
        final Line line = subwaySectionMap.getLine(lineId);
        final List<Section> sections = subwaySectionMap.getSections(lineId).getSections();
        return new LineResponseWithSections(line.getId(), line.getName(), line.getColor(), sections);
    }

    private SubwayStationMap getSubwayMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SubwayStationMap.of(lines, sections);
    }

    private SubwaySectionMap getSectionMap() {
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();
        return SubwaySectionMap.of(lines, sections);
    }
}
