package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.dto.AddStationToExistLineDto;
import subway.dto.CreateNewLineDto;
import subway.repository.SubwayRepository;

@Service
public class LineService {

    public final SubwayRepository subwayRepository;

    public LineService(SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    @Transactional
    public Line createNewLine(CreateNewLineDto dto) {
        subwayRepository.checkLineIsExist(dto.getLineName());
        Station upStation = subwayRepository.getStation(dto.getUpStationId());
        Station downStation = subwayRepository.getStation(dto.getDownStationId());

        Lines lines = new Lines(subwayRepository.getAllLines());
        Line createdLine = lines.addNewLine(dto.getLineName(), upStation, downStation, dto.getDistance());

        return subwayRepository.insertNewLine(createdLine);
    }

    @Transactional
    public Line addStationToExistLine(AddStationToExistLineDto dto) {
        Line line = subwayRepository.getLine(dto.getLineId());
        Station upStation = subwayRepository.getStation(dto.getUpStationId());
        Station downStation = subwayRepository.getStation(dto.getDownStationId());

        Lines lines = new Lines(subwayRepository.getAllLines());
        Line updatedLine = lines.addStationToLine(line, upStation, downStation, dto.getDistance());

        return subwayRepository.updateLine(updatedLine);
    }

    @Transactional
    public Line deleteStationFromLine(Long lineId, Long stationId) {
        Line line = subwayRepository.getLine(lineId);
        Station station = subwayRepository.getStation(stationId);

        Lines lines = new Lines(subwayRepository.getAllLines());
        Line updatedLine = lines.deleteStationFromLine(line, station);

        return subwayRepository.updateLine(updatedLine);
    }

    public Line findOneLine(Long lineId) {
        return subwayRepository.getLine(lineId);
    }

    public List<Line> findAllLine() {
        return subwayRepository.getAllLines();
    }
}
