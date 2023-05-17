package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineResponse> findAllLines() {
        final List<Line> lines = lineRepository.findAll();

        List<LineResponse> lineResponses = new ArrayList<>();
        for (Line line : lines) {
            List<Station> stations = line.findAllStation();
            lineResponses.add(LineResponse.of(line, stations));
        }
        return lineResponses;
    }

    public LineResponse findStations(final Long lineId) {
        final Line line = lineRepository.findById(lineId);
        List<Station> stations = line.findAllStation();
        return LineResponse.of(line, stations);
    }
}
