package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.AddStationToBetweenLineRequest;
import subway.application.dto.AddStationToEndLineRequest;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.exception.BusinessException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new BusinessException("존재하지 않는 호선입니다."));
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineRepository.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addStationToTopLine(final AddStationToEndLineRequest request) {
        final Line line = getLine(request.getLineName());
        final Station station = getStation(request.getStationName());
        line.addTopStation(station, request.getDistance());
        lineRepository.update(line);
    }

    @Transactional
    public void addStationToBottomLine(final AddStationToEndLineRequest request) {
        final Line line = getLine(request.getLineName());
        final Station station = getStation(request.getStationName());
        line.addBottomStation(station, request.getDistance());
        lineRepository.update(line);
    }

    @Transactional
    public void addStationToBetweenLine(final AddStationToBetweenLineRequest request) {
        final Line line = getLine(request.getLineName());
        final Station station = getStation(request.getStationName());
        final Station upStation = getStation(request.getUpStationName());
        final Station downStation = getStation(request.getDownStationName());
        line.addBetweenStation(station, upStation, downStation, request.getDistance());
        lineRepository.update(line);
    }

    private Line getLine(final String name) {
        return lineRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 호선입니다."));
    }

    private Station getStation(final String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다."));
    }
}
