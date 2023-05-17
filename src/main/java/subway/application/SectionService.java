package subway.application;

import static subway.application.StationFactory.toStation;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.Distance;
import subway.domain.Fee;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.RouteDto;
import subway.dto.SectionSaveDto;
import subway.exception.GlobalException;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void saveSection(Long lineId, SectionSaveDto request) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        Sections sections = getSections(sectionEntities);

        Station requestStartStation = new Station(request.getStartStation());
        Station requestEndStation = new Station(request.getEndStation());
        saveNewStationIfNotExists(requestStartStation);
        saveNewStationIfNotExists(requestEndStation);
        Distance requestDistance = new Distance(request.getDistance());
        Section requestSection = new Section(requestStartStation, requestEndStation, requestDistance);

        sections.add(requestSection);

        updateSections(lineId, sections);
    }

    private void updateSections(Long lineId, Sections sections) {
        sectionDao.deleteAllByLineId(lineId);

        List<SectionEntity> makeSectionEntitiesByAddedSections = makeSectionEntities(lineId, sections.getSections());

        sectionDao.insertAll(makeSectionEntitiesByAddedSections);
    }

    private void saveNewStationIfNotExists(Station station) {
        if (!stationDao.isExistStationByName(station.getName())) {
            stationDao.insert(new StationEntity(station.getName()));
        }
    }

    private List<SectionEntity> makeSectionEntities(Long lineId, List<Section> sections) {
        return sections.stream()
                .map(it -> {
                    Station startStation = it.getStartStation();
                    Station endStation = it.getEndStation();

                    Long startStationId = stationDao.findIdByName(startStation.getName());
                    Long endStationId = stationDao.findIdByName(endStation.getName());

                    return new SectionEntity(lineId, startStationId, endStationId, it.getDistance().getDistance());
                })
                .collect(Collectors.toList());
    }


    private Section toSection(SectionEntity sectionEntity) {
        Station startStation = toStation(stationDao.findById(sectionEntity.getStartStationId()));
        Station endStation = toStation(stationDao.findById(sectionEntity.getEndStationId()));
        Distance distance = new Distance(sectionEntity.getDistance());

        return new Section(startStation, endStation, distance);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        List<SectionEntity> sectionEntitiesOfLine = sectionDao.findByLineId(lineId);
        Sections sections = getSections(sectionEntitiesOfLine);

        Station removedStation = toStation(stationDao.findById(stationId));
        sections.remove(removedStation);

        updateSections(lineId, sections);
    }

    private Sections getSections(final List<SectionEntity> sectionEntitiesOfLine) {
        List<Section> sectionsOfLine = sectionEntitiesOfLine.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        return new Sections(sectionsOfLine);
    }

    public RouteDto getFeeByStations(final Long startStationId, final Long endStationId) {
        validateStations(startStationId, endStationId);

        StationEntity startStationEntity = stationDao.findById(startStationId);
        StationEntity endStationEntity = stationDao.findById(endStationId);

        Station startStation = new Station(startStationEntity.getName());
        Station endStation = new Station(endStationEntity.getName());

        List<SectionEntity> sectionEntities = sectionDao.findAll();
        List<Section> sections = sectionEntities.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = getDijkstraShortestPath(sections);

        List<Station> shortestPath = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();
        int distance = (int) dijkstraShortestPath.getPathWeight(startStation, endStation);

        Fee fee = Fee.toDistance(distance);

        List<String> shortestPathStationNames = shortestPath.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        return new RouteDto(distance, fee.getFee(), shortestPathStationNames);
    }

    private void validateStations(final Long startStationId, final Long endStationId) {
        if (startStationId.equals(endStationId)) {
            throw new GlobalException("출발역과 도착역이 같을 수는 없습니다.");
        }
        if (!stationDao.isExistStationById(startStationId) || !stationDao.isExistStationById(endStationId)) {
            throw new GlobalException("존재하지 않는 역입니다. 역을 다시 한번 확인해주세요.");
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath(
            final List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (int i = 0; i < sections.size(); i++) {
            graph.addVertex(sections.get(i).getStartStation());
            if (i == sections.size() - 1) {
                graph.addVertex(sections.get(i).getEndStation());
            }
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(
                            section.getStartStation(),
                            section.getEndStation()),
                    section.getDistance().getDistance()
            );
        }

        return new DijkstraShortestPath<>(graph);
    }

}
