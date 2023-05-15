package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.AddStationToExistLineDto;
import subway.dto.CreateNewLineDto;
import subway.repository.LineRepository;

@Service
public class LineService {

    public final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line createNewLine(CreateNewLineDto dto) {
        lineRepository.checkLineIsExist(dto.getLineName());
        Station upStation = lineRepository.getStation(dto.getUpStationId());
        Station downStation = lineRepository.getStation(dto.getDownStationId());

        Line createdLine = Line.createLine(dto.getLineName(), upStation, downStation, dto.getDistance());

        return lineRepository.insertNewLine(createdLine);
    }

    @Transactional
    public Line addStationToExistLine(AddStationToExistLineDto dto) {
        Line line = lineRepository.getLine(dto.getLineId());
        Station upStation = lineRepository.getStation(dto.getUpStationId());
        Station downStation = lineRepository.getStation(dto.getDownStationId());

        line.addEdge(upStation, downStation, dto.getDistance());

        return lineRepository.updateLine(line);
    }

    @Transactional
    public Line deleteStationFromLine(Long lineId, Long stationId) {
        Line line = lineRepository.getLine(lineId);
        Station station = lineRepository.getStation(stationId);

        line.deleteStation(station);

        return lineRepository.updateLine(line);
    }

    public Line findOneLine(Long lineId) {
        return lineRepository.getLine(lineId);
    }

    public List<Line> findAllLine() {
        return lineRepository.getAllLines();
    }
}
