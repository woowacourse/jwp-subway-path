package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.AddInitStationToLineRequest;
import subway.controller.dto.request.AddStationToBetweenLineRequest;
import subway.controller.dto.request.AddStationToEndLineRequest;
import subway.controller.dto.request.RemoveStationOnLineRequest;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.vo.Distance;
import subway.exception.BusinessException;
import subway.persistence.LineRepository;
import subway.persistence.StationRepository;

@Service
@Transactional
public class LineStationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void addInitStationToLine(final AddInitStationToLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station upStation = findStationByName(request.getUpStationName());
        final Station downStation = findStationByName(request.getDownStationName());
        final Distance distance = new Distance(request.getDistance());
        line.addInitStations(upStation, downStation, distance);
        lineRepository.update(line);
    }

    public void addStationToTopLine(final AddStationToEndLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station station = findStationByName(request.getStationName());
        final Distance distance = new Distance(request.getDistance());
        line.addTopStation(station, distance);
        lineRepository.update(line);
    }

    public void addStationToBottomLine(final AddStationToEndLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station station = findStationByName(request.getStationName());
        final Distance distance = new Distance(request.getDistance());
        line.addBottomStation(station, distance);
        lineRepository.update(line);
    }

    public void addStationToBetweenLine(final AddStationToBetweenLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station station = findStationByName(request.getStationName());
        final Station upStation = findStationByName(request.getUpStationName());
        final Station downStation = findStationByName(request.getDownStationName());
        final Distance distance = new Distance(request.getDistance());
        line.addBetweenStation(station, upStation, downStation, distance);
        lineRepository.update(line);
    }

    public void removeStationOnLine(final RemoveStationOnLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station station = findStationByName(request.getStationName());
        line.removeStation(station);
        lineRepository.update(line);
    }

    private Line findLineByName(final String name) {
        return lineRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 호선입니다."));
    }

    private Station findStationByName(final String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다."));
    }
}
