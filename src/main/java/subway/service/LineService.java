package subway.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.controller.dto.AddLineRequest;
import subway.controller.dto.AddLineResponse;
import subway.controller.dto.AddStationRequest;
import subway.controller.dto.AddStationResponse;
import subway.controller.dto.LineResponse;
import subway.controller.dto.RemoveStationRequest;
import subway.domain.Line;
import subway.domain.Station;
import subway.exception.BusinessException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@RequiredArgsConstructor
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public AddLineResponse addLine(final AddLineRequest request) {
        validateEmptyLine(request);
        final Station firstStation = getStation(request.getFrontStationName());
        final Station secondStation = getStation(request.getBackStationName());
        final Line line = new Line(request.getName(), request.getColor());
        line.addInitialStation(firstStation, secondStation, request.getDistance());
        final Line result = lineRepository.save(line);
        return AddLineResponse.from(result);
    }

    public Station getStation(final String name) {
        return stationRepository.findByName(name)
                .orElseGet(() -> stationRepository.save(new Station(name)));
    }

    private void validateEmptyLine(final AddLineRequest request) {
        final Optional<Line> existLine = lineRepository.findByName(request.getName());
        if (existLine.isPresent()) {
            throw new BusinessException("이미 존재하는 라인입니다");
        }
    }

    public AddStationResponse addStation(final AddStationRequest request) {
        final Line line = lineRepository.findByName(request.getLineName())
                .orElseThrow(() -> new BusinessException("이미 존재하는 라인입니다"));
        final Station firstStation = stationRepository.findByName(request.getFrontStation())
                .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다"));
        final Station station = getStation(request.getStationName());
        if (request.getIsEnd()) {
            line.addStationEnd(firstStation, station, request.getDistance());
        } else {
            final Station secondStation = stationRepository.findByName(request.getBackStation())
                    .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다"));
            line.addStationBetween(firstStation, secondStation, station, request.getDistance());
        }
        lineRepository.update(line);
        return AddStationResponse.from(station);
    }

    public void removeStation(final RemoveStationRequest request) {
        final Station existStation = stationRepository.findByName(request.getStationName())
                .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다"));
        final List<Line> lines = lineRepository.findAll();
        for (final Line line : lines) {
            line.deleteStation(existStation);
            lineRepository.update(line);
            if (line.isEmpty()) {
                lineRepository.delete(line);
            }
        }
        stationRepository.delete(existStation);
    }

    public List<LineResponse> getLines() {
        final List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
