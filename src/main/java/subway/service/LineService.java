package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.service.dto.LineDto;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;
import subway.repository.SubwayRepository;

@Service
public class LineService {

    private final SubwayRepository subwayRepository;
    private final LineRepository lineRepository;

    @Autowired
    public LineService(final SubwayRepository subwayRepository, final LineRepository lineRepository) {
        this.subwayRepository = subwayRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long register(final LineDto lineDto) {
        final Line line = new Line(lineDto.getName(), lineDto.getColor());
        return lineRepository.registerLine(line);
    }

    @Transactional(readOnly = true)
    public LineResponse read(final Long id) {
        final Line line = lineRepository.findLineById(id);
        final List<Station> stations = line.stations();
        final List<String> stationResponses = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
        return LineResponse.of(line, stationResponses);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> readAll() {
        final Subway subway = subwayRepository.findSubway();
        final List<Line> lines = subway.getLines();

        return lines.stream()
                .map(line -> {
                    List<String> stationResponses = line.stations().stream()
                            .map(Station::getName)
                            .collect(Collectors.toList());
                    return LineResponse.of(line, stationResponses);
                })
                .collect(Collectors.toList());
    }

}
