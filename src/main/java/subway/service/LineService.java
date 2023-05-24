package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.vo.Distance;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.domain.vo.Charge;
import subway.dto.AddStationToExistLineDto;
import subway.dto.LineCreateDto;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Line createNewLine(LineCreateDto dto) {
        lineRepository.checkLineIsExist(dto.getLineName());
        Station upStation = stationRepository.getStation(dto.getUpStationId());
        Station downStation = stationRepository.getStation(dto.getDownStationId());

        Line createdLine = Line.createLine(dto.getLineName(), new Charge(dto.getExtraCharge()), upStation, downStation, new Distance(dto.getDistance()));

        return lineRepository.insertNewLine(createdLine);
    }

    public Line addStationToExistLine(AddStationToExistLineDto dto) {
        Line line = lineRepository.getLine(dto.getLineId());
        Station upStation = stationRepository.getStation(dto.getUpStationId());
        Station downStation = stationRepository.getStation(dto.getDownStationId());

        line.addSection(upStation, downStation, new Distance(dto.getDistance()));

        return lineRepository.updateLine(line);
    }

    public Line deleteStationFromLine(Long lineId, Long stationId) {
        Line line = lineRepository.getLine(lineId);
        Station station = stationRepository.getStation(stationId);

        line.deleteStation(station);

        return lineRepository.updateLine(line);
    }

    @Transactional(readOnly = true)
    public Line findOneLine(Long lineId) {
        return lineRepository.getLine(lineId);
    }

    @Transactional(readOnly = true)
    public List<Line> findAllLine() {
        return lineRepository.getAllLines();
    }
}
