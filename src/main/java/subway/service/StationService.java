package subway.service;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.service.dto.SectionDto;
import subway.service.dto.StationDto;
import subway.dto.response.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public StationService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse register(final SectionDto sectionDto) {
        final Line line = lineRepository.findLineById(sectionDto.getLineId());

        final Station source = new Station(sectionDto.getSourceStation());
        final Station target = new Station(sectionDto.getTargetStation());

        line.registerSection(source, target, sectionDto.getDistance());

        updateLine(source, target);
        final Line registeredLine = lineRepository.updateLine(line);
        return StationResponse.of(registeredLine, registeredLine.stations());
    }

    private void updateLine(final Station source, final Station target) {
        final List<Station> stations = stationRepository.findStations();

        Stream.of(source, target)
                .filter(station -> !stations.contains(station))
                .forEach(stationRepository::registerStation);
    }

    @Transactional
    public void delete(final StationDto stationDto) {
        final Line line = lineRepository.findLineById(stationDto.getLineId());

        line.deleteStation(new Station(stationDto.getName()));
        lineRepository.updateLine(line);
    }
}
