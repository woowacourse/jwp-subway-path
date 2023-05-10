package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.SectionStation;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;
import subway.dto.StationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

//    public LineResponse findLineResponseById(Long id) {
//        Line persistLine = findLineById(id);
//        return LineResponse.of(persistLine);
//    }

    public LineStationResponse findLineById(Long id) {
        Line line = lineDao.findById(id);
        List<SectionStation> sections = sectionDao.findByLineId(id);
        Map<Long, Station> mapSections = new HashMap<>();
        sections.forEach(section -> mapSections.put(section.getUpStation().getId(), section.getDownStation()));

        mapSections.forEach((stationId, station) -> System.out.println(stationId));
        mapSections.forEach((stationId, station) -> {
            System.out.println(station.getId());
            System.out.println(station.getName());
        });
        List<Station> result = new ArrayList<>();

        Station nextStation = mapSections.get(null);
        System.out.println(nextStation.getId());
        while(nextStation.getId() != null) {
            result.add(nextStation);
            nextStation = mapSections.get(nextStation.getId());
        }
        List<StationResponse> stationResponses = result.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return LineStationResponse.from(LineResponse.of(line), stationResponses);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
