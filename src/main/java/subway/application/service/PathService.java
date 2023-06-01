package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Direction;
import subway.domain.Line;
import subway.domain.Station;
import subway.persistence.repository.SubwayRepository;

import java.util.List;

@Transactional
@Service
public class PathService {
    private final SubwayRepository subwayRepository;

    public PathService(final SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public void addPath(
            final Long lineId,
            final Long targetStationId,
            final Long addStationId,
            final Integer distance,
            final String direction
    ) {
        final Line line = subwayRepository.findLine(lineId);
        final Station targetStation = subwayRepository.findStationById(targetStationId);
        final Station addStation = subwayRepository.findStationById(addStationId);

        final Line lineAdded = line.addPath(targetStation, addStation, distance, Direction.of(direction));

        subwayRepository.saveLine(lineAdded);
    }

    public void removeStationFromLine(final Long lineId, final Long stationId) {
        final Line line = subwayRepository.findLine(lineId);
        final Station station = subwayRepository.findStationById(stationId);
        line.removeStation(station);

        subwayRepository.saveLine(line);
    }

    public void removeStationFromLines(final Long stationId) {
        final List<Line> lines = subwayRepository.findLines();
        final Station station = subwayRepository.findStationById(stationId);
        for (Line line : lines) {
            line.removeStation(station);
            subwayRepository.saveLine(line);
        }
    }
}
