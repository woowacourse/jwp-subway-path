package subway.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationsResponse;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineStationsResponse> findLineStationsResponses() {
        List<Line> persistLines = findLines();
        List<Station> persistStations = stationDao.findAll();
        List<Section> persistSections = sectionDao.findAll();

        Subway subway = new Subway(persistLines, persistStations, persistSections);
        Map<Line, List<Station>> lineMap = subway.getSubway();

        return lineMap.entrySet()
            .stream()
            .map(entry -> LineStationsResponse.of(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineStationsResponse findLineStationsResponseById(Long id) {
        List<Station> stations = stationDao.findAll();
        Map<Long, Station> idToStations = stations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));

        List<Section> allSections = sectionDao.findAllSectionByLineId(id);
        Sections sections = new Sections(allSections);
        List<Station> orderedStations = sections.findOrderedStationIds()
            .stream()
            .map(idToStations::get)
            .collect(Collectors.toList());

        Line persistLine = findLineById(id);

        return LineStationsResponse.of(persistLine, orderedStations);
    }


    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
