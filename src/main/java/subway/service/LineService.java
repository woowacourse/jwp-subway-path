package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import subway.controller.exception.SubwayException;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineDto;
import subway.dto.response.LineResponse;
import subway.repository.SubwayRepository;

@Service
public class LineService {

    private final SubwayRepository subwayRepository;

    @Autowired
    public LineService(final SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public Long register(final LineDto lineDto) {
        validateDuplicatedName(lineDto.getName());
        return subwayRepository.registerLine(lineDto.getName(), lineDto.getColor());
    }

    private void validateDuplicatedName(final String name) {
        if (subwayRepository.isDuplicatedName(name)) {
            throw new SubwayException("중복된 이름의 노선이 존재합니다.");
        }
    }

    public LineResponse read(final Long id) {
        final Line line = subwayRepository.findLineById(id);
        final List<Station> stations = line.stations();
        final List<String> stationResponses = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
        return LineResponse.of(line, stationResponses);
    }

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
