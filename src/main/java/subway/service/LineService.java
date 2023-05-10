package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Edge;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.dto.LineCreateRequest;

@Service
public class LineService {

    private final Lines lines;
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final EdgeDao edgeDao;


    public LineService(StationDao stationDao, LineDao lineDao, EdgeDao edgeDao) {
        this.lines = new Lines();
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
    }

    @Transactional
    public Line createNewLine(LineCreateRequest createRequest) {
        Station upStation = stationDao.findById(createRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station downStation = stationDao.findById(createRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        Line line = lines.addNewLine(createRequest.getLineName(), upStation, downStation, createRequest.getDistance());

        Line createdLine = lineDao.insert(line);

        edgeDao.insert(createdLine.getId(), new Edge(upStation, downStation, createRequest.getDistance()));

        return createdLine;
    }

    public void addStationToExistLine(String lineName, String stationName1, String stationName2, int distance) {
        Station station1 = new Station(stationName1);
        Station station2 = new Station(stationName2);
        lines.addStationToLine(lineName, station1, station2, distance);
    }

    public List<Station> findAllStation(String lineName) {
        return lines.findAllStation(lineName);
    }
}
