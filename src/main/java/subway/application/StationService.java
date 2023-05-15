package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.StationInitRequest;
import subway.dto.StationInitResponse;
import subway.exception.line.LineIsNotInitException;
import subway.repository.LineRepository;

import java.util.List;

@Service
public class StationService {

    private static final int UPBOUND_STATION = 0;
    private static final int DOWNBOUND_STATION = 1;

    private final LineRepository lineRepository;

    public StationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public StationInitResponse saveInitStations(final StationInitRequest stationInitRequest) {
        Line line = lineRepository.findByName(stationInitRequest.getLineName());
        if (!line.isEmpty()) {
            throw new LineIsNotInitException();
        }

        Section section = new Section(
                new Station(stationInitRequest.getUpBoundStationName()),
                new Station(stationInitRequest.getDownBoundStationName()),
                stationInitRequest.getDistance());
        List<Station> stations = lineRepository.saveInitStations(section, line.getId());

        return new StationInitResponse(
                stations.get(UPBOUND_STATION).getId(),
                stations.get(UPBOUND_STATION).getName(),
                stations.get(DOWNBOUND_STATION).getId(),
                stations.get(DOWNBOUND_STATION).getName());
    }
}
