package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.SectionInformationDto;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.StationRequest;

@Service
@Transactional
public class StationService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void save(final StationRequest stationRequest) {
        long lineId = stationRequest.getLineId();

        LineEntity lineEntity = lineDao.findById(lineId);
        Line line = new Line(lineEntity.getName());

        Stations stations = makeStations(lineId, line);
        Station startStation = makeStation(stationRequest.getStartStation(), line);
        Station endStation = makeStation(stationRequest.getEndStation(), line);

        if (stations.isEmpty()) {
            saveInitialStations(stationRequest, lineId, startStation, endStation);
            return;
        }

        stations.validateStations(startStation, endStation);

        if (stations.contains(startStation)) {
            saveEndStation(stationRequest, line, startStation, endStation);
            return;
        }

        saveStartStation(stationRequest, line, startStation, endStation);
    }

    private void saveInitialStations(final StationRequest stationRequest, final long lineId, final Station startStation,
                                     final Station endStation) {
        Long startStationId = stationDao.save(new StationEntity(startStation.getName(), lineId));
        Long endStationId = stationDao.save(new StationEntity(endStation.getName(), lineId));
        sectionDao.save(new SectionEntity(startStationId, endStationId, lineId, stationRequest.getDistance()));
    }

    private Station makeStation(final String name, final Line line) {
        return new Station(name, List.of(line));
    }

    private Stations makeStations(final long lineId, final Line line) {
        List<StationEntity> stationEntities = stationDao.findByLineId(lineId);
        List<Station> stations = stationEntities.stream()
                .map(entity -> new Station(entity.getName(), List.of(line)))
                .collect(Collectors.toList());
        return new Stations(stations);
    }

    public Sections makeSections(Long lineId, Line line) {
        List<SectionInformationDto> sectionInformationDtos = sectionDao.findDetailsByLineId(lineId);
        List<Section> sections = sectionInformationDtos.stream()
                .map(dto -> new Section(
                        new Station(dto.getStartStationName(), List.of(line)),
                        new Station(dto.getEndStationName(), List.of(line)),
                        dto.getDistance(),
                        line)
                ).collect(Collectors.toList());
        return new Sections(sections);
    }

    private void saveEndStation(final StationRequest stationRequest, final Line line,
                                final Station startStation, final Station endStation) {
        int distance = stationRequest.getDistance();
        Long lineId = stationRequest.getLineId();

        Sections sections = makeSections(lineId, line);
        Long startStationId = stationDao.findByName(startStation.getName()).getId();
        Long endStationId = stationDao.save(new StationEntity(endStation.getName(), lineId));
        if (sections.isTerminalStation(startStation)) {
            sectionDao.save(new SectionEntity(startStationId, endStationId, lineId, distance));
            return;
        }

        Section sectionStartedByStartStation = sections.getSectionByStartStation(startStation);
        sectionStartedByStartStation.validateDistance(distance);
        Long existStationId = stationDao.findByName(sectionStartedByStartStation.getEndStation().getName()).getId();

        sectionDao.save(new SectionEntity(startStationId, endStationId, lineId, distance));
        sectionDao.save(
                new SectionEntity(
                        endStationId,
                        existStationId,
                        lineId,
                        sectionStartedByStartStation.getDistance() - distance
                ));
        sectionDao.deleteByStationsId(startStationId, existStationId);
    }

    private void saveStartStation(final StationRequest stationRequest, final Line line,
                                  final Station startStation, final Station endStation) {
        int distance = stationRequest.getDistance();
        Long lineId = stationRequest.getLineId();

        Sections sections = makeSections(lineId, line);
        Long endStationId = stationDao.findByName(endStation.getName()).getId();
        Long startStationId = stationDao.save(new StationEntity(startStation.getName(), lineId));
        if (sections.isTerminalStation(endStation)) {
            sectionDao.save(new SectionEntity(startStationId, endStationId, lineId, distance));
            return;
        }

        Section sectionEndByEndStation = sections.getSectionByEndStation(endStation);
        sectionEndByEndStation.validateDistance(distance);
        Long existStationId = stationDao.findByName(sectionEndByEndStation.getStartStation().getName()).getId();

        sectionDao.save(new SectionEntity(existStationId, startStationId, lineId, distance));
        sectionDao.save(
                new SectionEntity(
                        startStationId,
                        endStationId,
                        lineId,
                        sectionEndByEndStation.getDistance() - distance
                ));
        sectionDao.deleteByStationsId(existStationId, endStationId);
    }
}
