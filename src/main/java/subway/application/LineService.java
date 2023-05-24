package subway.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.domain.subway.Subway;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationsResponse;

@Transactional(readOnly = true)
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

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        String name = request.getName();
        String color = request.getColor();


        if (lineDao.checkExistenceByNameAndColor(name, color)) {
            throw new DomainException(ExceptionType.LINE_ALREADY_EXIST);
        }

        Line persistLine = lineDao.insert(new Line(name, color, 0));
        return LineResponse.of(persistLine);
    }

    public List<LineStationsResponse> findLineStationsResponses() {
        List<Line> persistLines = findLines();
        List<Station> persistStations = stationDao.findAll();
        List<Section> persistSections = sectionDao.findAll();

        Subway subway = new Subway(persistLines, persistStations, persistSections);
        Map<Line, List<Station>> lineMap = subway.getLineMap();

        return getLineStationsResponses(lineMap);
    }

    private List<LineStationsResponse> getLineStationsResponses(Map<Line, List<Station>> lineMap) {
        return lineMap.entrySet()
            .stream()
            .map(entry -> LineStationsResponse.of(entry.getKey(), entry.getValue()))
            .collect(Collectors.toUnmodifiableList());
    }

    private List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineStationsResponse findLineStationsResponseById(Long id) {
        List<Station> stations = stationDao.findAll();
        List<Section> allSections = sectionDao.findAllSectionByLineId(id);

        Line persistLine = findLineById(id);

        Sections sections = new Sections(allSections);
        List<Station> orderedStations = getOrderedStations(stations, sections);

        return LineStationsResponse.of(persistLine, orderedStations);
    }

    private List<Station> getOrderedStations(List<Station> stations, Sections sections) {
        Map<Long, Station> idToStations = stations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));

        return sections.findOrderedStationIds()
            .stream()
            .map(idToStations::get)
            .collect(Collectors.toUnmodifiableList());
    }

    private Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        if (!lineDao.checkExistenceById(id)) {
            throw new DomainException(ExceptionType.LINE_DOES_NOT_EXIST);
        }
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), 0));
    }

    @Transactional
    public void deleteLineById(Long id) {
        if (!lineDao.checkExistenceById(id)) {
            throw new DomainException(ExceptionType.LINE_DOES_NOT_EXIST);
        }
        lineDao.deleteById(id);
    }
}
