package subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationsResponse;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

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
        List<Line> persistLines = lineDao.findAll();
        List<Station> persistStations = stationDao.findAll();
        List<Section> persistSections = sectionDao.findAll();

        Subway subway = new Subway(persistLines, persistStations, persistSections);
        Map<Line, List<Station>> lineMap = subway.getLineMap();

        return getResponses(lineMap);
    }

    private List<LineStationsResponse> getResponses(Map<Line, List<Station>> lineMap) {
        return lineMap.entrySet()
                .stream()
                .map(entry -> LineStationsResponse.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toUnmodifiableList());
    }

    public LineStationsResponse findLineStationsResponseById(Long id) {
        Stations stations = new Stations(stationDao.findAll());
        List<Section> allSections = sectionDao.findAllSectionByLineId(id);
        Line persistLine = lineDao.findById(id)
                .orElseThrow(() -> new DomainException(ExceptionType.UN_EXISTED_LINE));
        Sections sections = new Sections(allSections);

        List<Station> orderedStations = stations.generateSortedStations(sections.findOrderedStationIds());
        return LineStationsResponse.of(persistLine, orderedStations);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        final int updatedRow = lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
        if (updatedRow == 0) {
            throw new DomainException(ExceptionType.UN_EXISTED_LINE);
        }
    }

    public void deleteLineById(Long id) {
        final int deletedRow = lineDao.deleteById(id);
        if (deletedRow == 0) {
            throw new DomainException(ExceptionType.UN_EXISTED_LINE);
        }
    }

}
