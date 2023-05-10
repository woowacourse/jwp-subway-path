package subway.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Edge;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.dto.AddStationToLineRequest;
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

    public Line addStationToExistLine(Long lineId, AddStationToLineRequest addStationToLineRequest) {
        Station upStation = stationDao.findById(addStationToLineRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station downStation = stationDao.findById(addStationToLineRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));

        Line updatedLine = lines.addStationToLine(line.getName(), upStation, downStation, addStationToLineRequest.getDistance());
        // TODO : edge 테이블 업데이트 .. 다 덮어씌워야할까? 수정된 부분만?

        return updatedLine;
    }

    public List<Station> findAllStation(String lineName) {
        return lines.findAllStation(lineName);
    }
}
