package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.domain.graph.Graph;
import subway.domain.graph.SubwayGraph;
import subway.dto.*;
import subway.exeption.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final SubwayService subwayService;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(
            final SubwayService subwayService,
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final SectionRepository sectionRepository) {
        this.subwayService = subwayService;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        final Subway subway = subwayService.findSubway();
        List<Line> lines = subway.findAllLines();
        return lines.stream()
                .map(line -> LineResponse.of(
                        line,
                        mapToStationResponse(
                                subway.findStationsInOrder(line)
                        )))
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse createNewLine(final LineCreateRequest request) {
        final Subway subway = subwayService.findSubway();
        final Line line = lineRepository.save(Line.of(request.getName(), request.getColor()));
        subway.createSectionsOf(line, new SubwayGraph());
        return LineResponse.of(line);
    }

    @Transactional
    public LineResponse createInitialSection(final Long lineId, final InitialSectionCreateRequest request) {
        final Subway subway = subwayService.findSubway();
        final Line line = lineRepository.findById(lineId);

        final Station upStation = stationRepository.findById(request.getUpStationId());
        final Station downStation = stationRepository.findById(request.getDownStationId());
        validateStationsExists(upStation, downStation);

        final int distance = request.getDistance();

        final Section section = new Section(lineId, upStation, downStation, distance);
        final Section savedSection = sectionRepository.save(lineId, section);

        subway.addSection(line, savedSection);

        final List<Station> stationsInOrder = subway.findStationsInOrder(line);
        return LineResponse.of(line, mapToStationResponse(stationsInOrder));
    }

    @Transactional
    public LineResponse addStation(final Long lineId, final SectionCreateRequest request) {
        final Subway subway = subwayService.findSubway();
        final Line line = lineRepository.findById(lineId);

        final Station upStation = stationRepository.findById(request.getUpStationId());
        final Station downStation = stationRepository.findById(request.getDownStationId());
        validateStationsExists(upStation, downStation);

        final int distance = request.getDistance();

        subway.addStation(line, upStation, downStation, distance);

        final Sections sections = subway.findSectionsOf(line);

        sectionRepository.deleteSectionsOf(line);
        final Graph graph = sections.getGraph();
        final List<Section> sections1 = graph.getSections();
        sectionRepository.saveAll(line, sections1);

        final List<Station> stationsInOrder = subway.findStationsInOrder(line);
        final List<StationResponse> stationResponse = mapToStationResponse(stationsInOrder);

        return LineResponse.of(line, stationResponse);
    }

    private static void validateStationsExists(final Station upStation, final Station downStation) {
        if (upStation == null || downStation == null) {
            throw new StationNotFoundException("존재하는 역의 id를 입력해 주세요.");
        }
    }

    @Transactional
    public void deleteStation(final Long lineId, final Long stationId) {
        final Subway subway = subwayService.findSubway();
        final Line line = lineRepository.findById(lineId);
        final Station station = stationRepository.findById(stationId);

        final Sections sections = subway.findSectionsOf(line);

        final Station previousStation = subway.findStationBefore(line, station);
        final Station nextStation = subway.findStationAfter(line, station);

        final Long previousStationId = sectionRepository.findStationIdBefore(lineId, stationId);
        final Long nextStationId = sectionRepository.findStationIdAfter(lineId, stationId);

        if (previousStation != null) {
            sectionRepository.deleteSection(lineId, previousStationId, stationId);
        }
        if (nextStation != null) {
            sectionRepository.deleteSection(lineId, stationId, nextStationId);
        }

        sections.deleteStation(station);
    }

    private List<StationResponse> mapToStationResponse(final List<Station> stations) {
        return stations.stream()
                .map(Station::getId)
                .map(stationRepository::findById)
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
