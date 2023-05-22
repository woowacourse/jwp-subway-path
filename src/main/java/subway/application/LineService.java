package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.DuplicateLineNameException;
import subway.exception.InvalidLineException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(lineDao.findByName(request.getLineName())){
            throw new DuplicateLineNameException();
        }

        addStationOfSection(request.getStartStation(), request.getEndStation());

        Line line = new Line(request.getLineName(), new Sections(
                List.of(new Section(stationDao.findByName(request.getStartStation()),
                        stationDao.findByName(request.getEndStation()),
                        new Distance(request.getDistance())))
        ));

        Long savedId = lineDao.insert(line);
        sectionDao.insertAll(savedId, line.getSections().getSections());
        return new LineResponse(savedId, request.getLineName(), makeStationResponses(line.getSections().getSortedStations()));
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineDao.findAll().stream()
                .map(it -> new Line(it.getId(), it.getName(), new Sections(sectionDao.findByLineId(it.getId()))))
                .collect(Collectors.toList());

        return lines.stream()
                .map(it -> new LineResponse(it.getId(), it.getName(), makeStationResponses(it.getSections().getSortedStations())))
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long lineId) {
        Line line = lineDao.findById(lineId).map(it -> new Line(it.getId(),it.getName(), new Sections(sectionDao.findByLineId(it.getId()))))
                .orElseThrow(InvalidLineException::new);

        return new LineResponse(line.getId(), line.getName(), makeStationResponses(line.getSections().getSortedStations()));
    }

    private List<StationResponse> makeStationResponses(List<Station> sortedStations) {
        Map<String, Long> stations = stationDao.findAll().stream()
                .collect(Collectors.toMap(Station::getName, Station::getId));

        return sortedStations.stream()
                .map(it -> new StationResponse(stations.get(it.getName()), it.getName()))
                .collect(Collectors.toList());
    }

    private void addStationOfSection(String startStation, String endStation) {
        Map<String, Long> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(Station::getName, Station::getId));

        if (!stations.containsKey(startStation)) {
            stationDao.insert(new Station(startStation));
        }

        if (!stations.containsKey(endStation)) {
            stationDao.insert(new Station(endStation));
        }
    }
}
