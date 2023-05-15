package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.*;
import subway.dto.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineServiceImpl implements LineService {

    private final Subway subway;
    private final Graph graph;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineServiceImpl(
            final Subway subway,
            final Graph graph,
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final SectionRepository sectionRepository) {
        this.subway = subway;
        this.graph = graph;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<LineResponse> findAll() {
        List<Line> lines = subway.findAllLines();
        return lines.stream()
                .map(line -> LineResponse.of(
                        line,
                        mapToStationResponse(line)))
                .collect(Collectors.toList());
    }

    @Override
    public LineResponse createNewLine(final LineCreateRequest request) {
        // TODO: 이미 존재하는 노선을 추가로 생성하는 경우 예외 처리
        final Line line = lineRepository.save(Line.of(request.getName(), request.getColor()));
        subway.createSectionsOf(line, graph);
        return LineResponse.of(line);
    }

    @Override
    public LineResponse createInitialSection(final Long id, final InitialSectionCreateRequest request) {
        final Line line = lineRepository.findById(id);

        final Station upStation = stationRepository.findById(request.getUpStationId());
        final Station downStation = stationRepository.findById(request.getDownStationId());

        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException("존재하는 역의 id를 입력해 주세요.");
        }

        final int distance = request.getDistance();

        subway.createNewSection(line, upStation, downStation, distance);

        final Section section = new Section(upStation, downStation, distance);
        sectionRepository.save(line.getId(), section);

        return LineResponse.of(line, mapToStationResponse(line));
    }

    @Override
    public LineResponse addStation(final Long id, final SectionCreateRequest request) {
        final Line line = lineRepository.findById(id);

        final Station upStation = stationRepository.findById(request.getUpStationId());
        final Station downStation = stationRepository.findById(request.getDownStationId());

        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException("존재하는 역의 id를 입력해 주세요.");
        }

        final int distance = request.getDistance(); // upStation ~ newStation

        final Station newStation = subway.addStation(line, upStation, downStation, distance);

        final int currentDistance = subway.findDistanceBetween(line, upStation, downStation); // upStation ~ downStation
        final int updatedDistance = currentDistance - distance; // newStation ~ downStation

        // add new sections created
        sectionRepository.save(line.getId(), new Section(upStation, newStation, distance));
        sectionRepository.save(line.getId(), new Section(newStation, downStation, updatedDistance));

        // delete old section
        sectionRepository.delete(new Section(upStation, downStation, currentDistance));

        // query stations in order to create line response
        final List<StationResponse> stationResponse = mapToStationResponse(line);

        return LineResponse.of(line, stationResponse);
    }

    @Override
    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findById(lineId);
        final Station station = stationRepository.findById(stationId);

        final Sections sections = subway.findSectionsOf(line);

        final Station previousStation = subway.findStationBefore(line, station);
        final Station nextStation = subway.findStationAfter(line, station);

        if (previousStation != null) {
            sectionRepository.deleteSection(lineId, previousStation, stationId);
        }
        if (nextStation != null) {
            sectionRepository.deleteSection(lineId, stationId, nextStation);
        }

        sections.deleteStation(station);
    }

    private List<StationResponse> mapToStationResponse(final Line line) {
        final List<Station> stationsInOrder = subway.findStationsInOrderOf(line);

        return stationsInOrder.stream()
                .map(stationRepository::findByName)
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
