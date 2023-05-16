package subway.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import subway.domain.*;
import subway.dto.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineServiceImpl implements LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineServiceImpl(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    private Subway findSubway() {
        final List<Line> lines = lineRepository.findAllLines();

        List<Sections> subwaySections = new ArrayList<>();

        for (final Line line : lines) {
            final List<Section> sectionsOfLine = sectionRepository.findAllSectionOf(line);
            final Graph newGraph = new SubwayGraph();

            final List<Long> allStationIds = sectionRepository.findAllStationIdsOf(line);

            for (final Long stationId : allStationIds) {
                final Station station = stationRepository.findById(stationId);
                newGraph.addStation(station);
            }

            for (final Section section : sectionsOfLine) {
                final Station upStation = section.getUpStation();
                final Station downStation = section.getDownStation();
                final int distance = section.getDistance();

                final DefaultWeightedEdge edge = newGraph.addSection(upStation, downStation);
                newGraph.setSectionDistance(edge, distance);
            }

            Sections sections = new Sections(line, newGraph);
            subwaySections.add(sections);
            System.out.println("graph = " + newGraph);
        }

        return new Subway(subwaySections);
    }

    @Override
    public List<LineResponse> findAll() {
        final Subway subway = findSubway();

        List<Line> lines = subway.findAllLines();
        return lines.stream()
                .map(line -> LineResponse.of(
                        line,
                        mapToStationResponse(line)))
                .collect(Collectors.toList());
    }

    @Override
    public LineResponse createNewLine(final LineCreateRequest request) {
        final Subway subway = findSubway();
        // TODO: 이미 존재하는 노선을 추가로 생성하는 경우 예외 처리
        final Line line = lineRepository.save(Line.of(request.getName(), request.getColor()));
        subway.createSectionsOf(line, new SubwayGraph());
        return LineResponse.of(line);
    }

    @Override
    public LineResponse createInitialSection(final Long lineId, final InitialSectionCreateRequest request) {
        final Subway subway = findSubway();

        final Line line = lineRepository.findById(lineId);

        final Station upStation = stationRepository.findById(request.getUpStationId());
        final Station downStation = stationRepository.findById(request.getDownStationId());

        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException("존재하는 역의 id를 입력해 주세요.");
        }

        final int distance = request.getDistance();

        final Section section = new Section(lineId, upStation, downStation, distance);
        final Section savedSection = sectionRepository.save(lineId, section);

        subway.createNewSection(line, savedSection);

        return LineResponse.of(line, mapToStationResponse(line));
    }

    @Override
    public LineResponse addStation(final Long id, final SectionCreateRequest request) {
        final Subway subway = findSubway();
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
        sectionRepository.save(line.getId(), new Section(id, upStation, newStation, distance));
        sectionRepository.save(line.getId(), new Section(id, newStation, downStation, updatedDistance));

        // delete old section
        sectionRepository.delete(line.getId(), new Section(id, upStation, downStation, currentDistance));

        // query stations in order to create line response
        final List<StationResponse> stationResponse = mapToStationResponse(line);

        return LineResponse.of(line, stationResponse);
    }

    @Override
    public void deleteStation(final Long lineId, final Long stationId) {
        final Subway subway = findSubway();

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

    private List<StationResponse> mapToStationResponse(final Line line) {
        final Subway subway = findSubway();

        final List<Station> stationsInOrder = subway.findStationsInOrderOf(line);

        return stationsInOrder.stream()
                .map(Station::getId)
                .map(stationRepository::findById)
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
