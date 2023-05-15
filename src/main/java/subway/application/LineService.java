package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.*;
import subway.domain.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final Lines lines;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lines = new Lines();
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Lines lines = new Lines();
        lineDao.findAll()
                .forEach(it -> lines.addNewLine(it.getName(), new Sections(toSections(sectionDao.findAll()))));

        Line line = lines.addNewLine(request.getLineName(), new Sections(
                List.of(new Section(new Station(request.getStartStation()), new Station(request.getEndStation()), new Distance(request.getDistance())))
        ));

        addStationOfSection(request.getStartStation(), request.getEndStation());

        Long savedId = lineDao.insert(new LineEntity(line.getName()));
        sectionDao.insertAll(toSectionEntities(savedId, line.getSections().getSections()));

        List<StationResponse> stationsResponses = makeStationResponses(line.getSections().getSortedStations());
        return new LineResponse(savedId, request.getLineName(), stationsResponses);
    }

    public List<LineResponse> findAllLines() {
        List<LineEntity> existsLines = lineDao.findAll();

        return existsLines.stream()
                .map(entities -> {
                    Long lineId = entities.getId();
                    return makeLineResponse(entities, lineId);
                })
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long lineId) {
        LineEntity findEntity = lineDao.findById(lineId);

        return makeLineResponse(findEntity, lineId);
    }

    private List<StationResponse> makeStationResponses(List<Station> sortedStations) {

        Map<String, Long> stations = stationDao.findAll().stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        List<StationResponse> stationResponses = sortedStations.stream()
                .map(it -> new StationResponse(stations.get(it.getName()), it.getName()))
        .collect(Collectors.toList());

        return stationResponses;
    }

    private LineResponse makeLineResponse(LineEntity entities, Long lineId) {
        List<Section> existSections = toSections(sectionDao.findByLineId(lineId));

        Line line = new Line(entities.getName(), new Sections(existSections));

        if (line.getSections().getSections().isEmpty()) {
            return new LineResponse(entities.getId(), entities.getName(), Collections.emptyList());
        }

        return getSortedLineResponse(entities, line.getSections());
    }

    private LineResponse getSortedLineResponse(LineEntity entities, Sections sections) {
        List<Station> sortedStations = sections.getSortedStations();

        List<StationResponse> stationsResponses = makeStationResponses(sortedStations);

        return new LineResponse(entities.getId(), entities.getName(), stationsResponses);
    }

    private List<SectionEntity> toSectionEntities(Long lineId, List<Section> sections) {
        Map<String, Long> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        return sections.stream()
                .map(it -> new SectionEntity(lineId,
                        stations.get(it.getStartStation().getName()),
                        stations.get(it.getEndStation().getName()),
                        it.getDistance().getDistance()))
                .collect(Collectors.toList());
    }

    private void addStationOfSection(String startStation, String endStation) {
        Map<String, Long> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        if (!stations.containsKey(startStation)) {
            stationDao.insert(new StationEntity(startStation));
        }

        if (!stations.containsKey(endStation)) {
            stationDao.insert(new StationEntity(endStation));
        }
    }

    private List<Section> toSections(List<SectionEntity> findSections) {
        Map<Long, String> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::getName));

        List<Section> existSections = findSections.stream()
                .map(it -> new Section(
                        new Station(stations.get(it.getStartStationId())),
                        new Station(stations.get(it.getEndStationId())),
                        new Distance(it.getDistance()))
                )
                .collect(Collectors.toList());
        return existSections;
    }
}
