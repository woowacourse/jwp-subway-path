package subway.application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.application.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.LineDto;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;
import subway.ui.dto.LineStationResponse;
import subway.ui.dto.StationResponse;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Long lineId = lineDao.insert(new LineDto(null, request.getName()));
        return new LineResponse(lineId, request.getName());
    }

    public List<LineResponse> findLineResponses() {
        List<LineDto> foundLines = lineDao.findAll();
        return foundLines.stream()
                .map(foundLine -> new LineResponse(foundLine.getId(), foundLine.getName()))
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        LineDto foundLine = findByLineId(id);
        return new LineResponse(foundLine.getId(), foundLine.getName());
    }

    public List<LineStationResponse> findAllLinesAndStations() {
        List<LineDto> lines = lineDao.findAll();

        List<LineStationResponse> responses = new ArrayList<>();
        for (LineDto line : lines) {
            LineStationResponse response = findStationsById(line.getId());
            responses.add(response);
        }
        return responses;
    }

    public LineStationResponse findStationsById(Long lineId) {
        LineDto foundLine = findByLineId(lineId);

        List<SectionDto> sectionDtos = sectionDao.findByLineId(lineId);

        LinkedList<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(
                                stationDao.findById(sectionDto.getLeftStationId()),
                                stationDao.findById(sectionDto.getRightStationId()),
                                sectionDto.getDistance()
                        )
                ).collect(Collectors.toCollection(LinkedList::new));

        Line line = new Line(lineId, foundLine.getName(), sections);
        List<Station> stations = line.findLeftToRightRoute();
        List<StationResponse> stationResponses = stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineStationResponse(line.getId(), line.getName(), stationResponses);
    }

    private LineDto findByLineId(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 노선이 없습니다."));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}
