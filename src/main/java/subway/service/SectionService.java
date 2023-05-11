package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.CreateSectionRequest;
import subway.dto.response.LineResponse;
import subway.exception.NoSuchLineException;
import subway.exception.NoSuchStationException;
import subway.mapper.LineMapper;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(final LineRepository lineRepository,
                          final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse createSection(final long lineId, final CreateSectionRequest request) {
        Line line = lineRepository.findById(lineId);
        if (line == null) {
            throw new NoSuchLineException();
        }

        Station upStation = stationRepository.findById(request.getUpStation());
        Station downStation = stationRepository.findById(request.getDownStation());
        if (upStation == null || downStation == null) {
            throw new NoSuchStationException();
        }

        line.addSection(new Section(upStation, downStation, request.getDistance()));

        lineRepository.updateSections(line);

        return LineMapper.toResponse(line);
    }

    public void deleteSection(final long lineId, final long stationId) {
        Line line = lineRepository.findById(lineId);
        if (line == null) {
            throw new NoSuchLineException();
        }

        Station station = stationRepository.findById(stationId);
        if (station == null) {
            throw new NoSuchStationException();
        }

        line.removeStation(station);

        lineRepository.updateSections(line);
    }
}
