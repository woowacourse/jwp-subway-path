package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationPositionRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public StationService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long saveStation(final StationRequest request) {
        Station savedStation = stationRepository.save(new Station(request.getName()));
        List<StationPositionRequest> positions = request.getPositions();

        for (StationPositionRequest position : positions) {
            Line findLine = lineRepository.findById(position.getLineId());
            Station upStation = stationRepository.findById(position.getUpStationId());
            Station downStation = stationRepository.findById(position.getDownStationId());

            Distance upDistance = new Distance(position.getUpStationDistance());
            Distance downDistance = new Distance(position.getDownStationDistance());
            Section upSection = Section.of(upStation, savedStation, upDistance);
            Section downSection = Section.of(savedStation, downStation, downDistance);

            Line updateLine = findLine.addSection(upSection, downSection);

            sectionRepository.updateByLine(findLine, updateLine);
        }
        return savedStation.getId();
    }

    @Transactional
    public void deleteStation(final Long stationId) {
        Station station = stationRepository.findById(stationId);
        List<Line> findLines = lineRepository.findAll();
        List<Line> updateLines = new ArrayList<>();
        for (Line findLine : findLines) {
            Line updateLine = findLine.removeStation(station);
            updateLines.add(updateLine);
            sectionRepository.updateByLine(findLine, updateLine);
        }
        for (int index = 0; index < findLines.size(); index++) {
            stationRepository.updateByLine(findLines.get(index), updateLines.get(index));
        }
    }
}
