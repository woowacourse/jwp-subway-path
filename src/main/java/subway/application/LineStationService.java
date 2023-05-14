package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.AddInitStationToLineRequest;
import subway.controller.dto.AddStationToBetweenLineRequest;
import subway.controller.dto.AddStationToEndLineRequest;
import subway.controller.dto.RemoveStationOnLineRequest;
import subway.domain.Line;
import subway.domain.Station;
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
        final Line line = getLineByName(request.getLineName());
        final Station upStation = getStationByName(request.getUpStationName());
        final Station downStation = getStationByName(request.getDownStationName());
        line.addInitStations(upStation, downStation, request.getDistance());
        lineRepository.update(line);
    }

    public void addStationToTopLine(final AddStationToEndLineRequest request) {
        final Line line = getLineByName(request.getLineName());
        final Station station = getStationByName(request.getStationName());
        line.addTopStation(station, request.getDistance());
        lineRepository.update(line);
    }

    public void addStationToBottomLine(final AddStationToEndLineRequest request) {
        final Line line = getLineByName(request.getLineName());
        final Station station = getStationByName(request.getStationName());
        line.addBottomStation(station, request.getDistance());
        lineRepository.update(line);
    }

    public void addStationToBetweenLine(final AddStationToBetweenLineRequest request) {
        final Line line = getLineByName(request.getLineName());
        final Station station = getStationByName(request.getStationName());
        final Station upStation = getStationByName(request.getUpStationName());
        final Station downStation = getStationByName(request.getDownStationName());
        line.addBetweenStation(station, upStation, downStation, request.getDistance());
        lineRepository.update(line);
    }

    public void removeStationOnLine(final RemoveStationOnLineRequest request) {
        final Line line = getLineByName(request.getLineName());
        final Station station = getStationByName(request.getStationName());
        line.removeStation(station);
        lineRepository.update(line);
    }

    private Line getLineByName(final String name) {
        return lineRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 호선입니다."));
    }

    private Station getStationByName(final String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다."));
    }
}
