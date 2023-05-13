package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineServiceImpl implements LineService {

    private final Subway subway;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineServiceImpl(
            final Subway subway,
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final SectionRepository sectionRepository) {
        this.subway = subway;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public LineResponse createLineWithoutStation(final LineCreateRequest request) {
        final Line line = lineRepository.save(Line.of(request.getName(), request.getColor()));
        return LineResponse.of(line);
    }

    @Override
    public LineResponse addStation(final Long id, final StationCreateRequest request) {
        final Line line = lineRepository.findById(id);

        final Station upStation = stationRepository.findByName(request.getUpStationId());
        final Station downStation = stationRepository.findByName(request.getDownStationId());

        final int currentDistance = subway.findDistanceBetween(line, upStation, downStation); // upStation ~ downStation
        final int distance = request.getDistance(); // upStation ~ newStation
        final int updatedDistance = currentDistance - distance; // newStation ~ downStation

        final Station newStation = subway.addStation(line, upStation, downStation, distance);

        // add new station
        stationRepository.save(newStation);

        // add new sections created
        sectionRepository.save(line.getId(), new Section(upStation, newStation, distance));
        sectionRepository.save(line.getId(), new Section(newStation, downStation, updatedDistance));

        // delete old section
        sectionRepository.delete(new Section(upStation, downStation, currentDistance));

        // query stations in order to create line response
        final List<Station> stationsInOrder = subway.findStationsInOrderOf(line);

        final List<StationResponse> stationResponse = stationsInOrder.stream()
                .map(stationRepository::findByName)
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return LineResponse.of(line, stationResponse);
    }
}
