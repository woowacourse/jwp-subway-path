package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.dto.SubwayMapResponse;
import subway.repository.LineRepository;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SubwayMapService {
    private final LineRepository lineRepository;

    public SubwayMapService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public SubwayMapResponse findAllStationsInLine(final long lineId) {
        Line line = lineRepository.findById(lineId);
        List<Station> alignedStations = line.getAligned();
        LineResponse lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor());

        if(alignedStations.size()==0){
            return new SubwayMapResponse(lineResponse, Collections.emptyList());
        }

        List<StationResponse> stationResponses = alignedStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(toList());

        return new SubwayMapResponse(lineResponse, stationResponses);
    }

    public List<SubwayMapResponse> findAllStations() {
        List<Line> allLines = lineRepository.findAll();

        return allLines.stream()
                .map(line -> findAllStationsInLine(line.getId()))
                .collect(toList());
    }
}

