package subway.service;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.SectionDto;
import subway.dto.StationDto;
import subway.dto.response.StationResponse;
import subway.repository.SubwayRepository;

@Service
public class StationService {

    private final SubwayRepository subwayRepository;

    @Autowired
    public StationService(final SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    @Transactional
    public StationResponse register(final SectionDto sectionDto) {
        final Line line = subwayRepository.findLineById(sectionDto.getLineId());

        final Station source = new Station(sectionDto.getSourceStation());
        final Station target = new Station(sectionDto.getTargetStation());

        line.registerSection(source, target, sectionDto.getDistance());

        updateLine(source, target);
        final Line registeredLine = subwayRepository.updateLine(line);
        return StationResponse.of(registeredLine, registeredLine.stations());
    }

    private void updateLine(final Station source, final Station target) {
        final List<Station> stations = subwayRepository.findStations();

        Stream.of(source, target)
                .filter(station -> !stations.contains(station))
                .forEach(subwayRepository::registerStation);
    }

    @Transactional
    public void delete(final StationDto stationDto) {
        final Line line = subwayRepository.findLineById(stationDto.getLineId());

        line.deleteStation(new Station(stationDto.getName()));
        subwayRepository.updateLine(line);
    }
}
