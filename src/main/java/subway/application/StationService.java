package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.DeleteStationRequest;
import subway.dto.StationCreateRequest;
import subway.dto.StationPositionRequest;
import subway.dto.StationRequest;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public StationService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveStation(final StationCreateRequest request) {
        Station station = new Station(request.getName());
        List<StationPositionRequest> positions = request.getPositions();

        Long id = stationRepository.save(station);

        for (StationPositionRequest position : positions) {
            Line line = lineRepository.findById(position.getLineId());
            Station upStation = stationRepository.findById(position.getUpStationId());
            Station downStation = stationRepository.findById(position.getDownStationId());

            Distance upDistance = new Distance(position.getUpStationDistance());
            Distance downDistance = new Distance(position.getDownStationDistance());
            Section upSection = Section.of(upStation, station, upDistance);
            Section downSection = Section.of(station, downStation, downDistance);

            Line updateLine = line.addSection(upSection, downSection);

            lineRepository.update(line, updateLine);
        }
        return id;
    }

    @Transactional
    public Long saveStation(StationRequest stationRequest) {
        return null;
    }

    @Transactional
    public void deleteStationByStationNameAndLineName(final DeleteStationRequest deleteStationRequest) {
    }
}
