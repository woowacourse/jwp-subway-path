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
                .stream()
                .forEach(it -> lines.addNewLine(it.getName(), new Sections(toSections(sectionDao.findAll()))));

        Line line = lines.addNewLine(request.getLineName(), new Sections(
                List.of(new Section(new Station(request.getStartStation()), new Station(request.getEndStation()), new Distance(request.getDistance())))
        ));

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
        List<StationResponse> stationsResponses = sortedStations.stream()
                .map(it -> {
                    //TODO : 여러번 db조회보다 한번에 조회
                    Long findStationId = stationDao.findIdByName(it.getName());
                    return new StationResponse(findStationId, it.getName());
                })
                .collect(Collectors.toList());
        return stationsResponses;
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
        return sections.stream()
                .map(it -> {
                    //TODO : 여러번 db조회보다 한번에 조회
                    addStationOfSection(new Station(it.getStartStation().getName()), new Station(it.getEndStation().getName()));
                    Long startStationId = stationDao.findIdByName(it.getStartStation().getName());
                    Long endStationId = stationDao.findIdByName(it.getEndStation().getName());
                    return new SectionEntity(lineId, startStationId, endStationId, it.getDistance().getDistance());
                })
                .collect(Collectors.toList());
    }

    private void addStationOfSection(Station requestStartStation, Station requestEndStation) {
        if (!stationDao.isExistStationByName(requestStartStation.getName())) {
            stationDao.insert(new StationEntity(requestStartStation.getName()));
        }

        if (!stationDao.isExistStationByName(requestEndStation.getName())) {
            stationDao.insert(new StationEntity(requestEndStation.getName()));
        }
    }

    private List<Section> toSections(List<SectionEntity> findSections) {
        List<Section> existSections = findSections.stream()
                .map(this::toSection)
                .collect(Collectors.toList());
        return existSections;
    }

    private Section toSection(SectionEntity sectionEntity) {
        //TODO : 여러번 db조회보다 한번에 조회
        Station startStation = toStation(stationDao.findById(sectionEntity.getStartStationId()));
        Station endStation = toStation(stationDao.findById(sectionEntity.getEndStationId()));
        Distance distance = new Distance(sectionEntity.getDistance());

        return new Section(startStation, endStation, distance);
    }

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }
}
