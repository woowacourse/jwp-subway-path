package subway.service;


import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Entity.SectionEntity;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.DtoMapper;
import subway.dto.EndSectionRequest;
import subway.dto.InitSectionRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionLastDeleteRequest;
import subway.dto.SectionRequest;
import subway.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public void saveSection(SectionRequest request) {
        Line line = lineDao.findById(request.getLineId());
        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());
        Station newStation = stationDao.findById(request.getStationId());

        SectionEntity sectionEntity = sectionDao.selectByStationIdsAndLineId(upward.getId(), downward.getId(), line.getId());
        Section originalSection = Section.of(
                sectionEntity.getId(),
                upward,
                downward,
                sectionEntity.getDistance(),
                line
        );

        Sections sections = Sections.from(new ArrayList<>(List.of(originalSection)));
        List<Section> newSections = sections.addSection(
                newStation,
                originalSection,
                request.getUpwardDistance(),
                request.getDownwardDistance()
        );

        sectionDao.deleteById(sectionEntity.getId());

        for (Section section : newSections) {
            sectionDao.insert(DtoMapper.convertToSectionEntity(section));
        }
    }

    public void saveInitSections(InitSectionRequest request) {
        Line line = lineDao.findById(request.getLineId());

        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());

        Sections sections = Sections.from(new ArrayList<>());
        List<Section> newSections = sections.addInitSection(upward, downward, request.getDistance(), line);

        for (Section section : newSections) {
            sectionDao.insert(DtoMapper.convertToSectionEntity(section));
        }
    }

    public void saveEndSection(EndSectionRequest request) {
        Line line = lineDao.findById(request.getLineId());
        Station originalEndStation = stationDao.findById(request.getOriginalEndStationId());
        Station newStation = stationDao.findById(request.getStationId());

        SectionEntity sectionEntity = sectionDao.selectEndSection(originalEndStation.getId(), line.getId());

        Section endSection;

        if (Objects.equals(sectionEntity.getUpwardId(), originalEndStation.getId())) {
            //하행종착
            endSection = Section.ofEmptyDownwardSection(
                    originalEndStation,
                    line
            );
            Sections sections = Sections.from(new ArrayList<>(List.of(endSection)));
            List<Section> newSections = sections.addEndSectionAtDownward(newStation, endSection, request.getDistance());
            sectionDao.deleteById(sectionEntity.getId());

            for (Section section : newSections) {
                sectionDao.insert(DtoMapper.convertToSectionEntity(section));
            }
        }

        if (Objects.equals(sectionEntity.getDownwardId(), originalEndStation.getId())) {
            //상행종착
            endSection = Section.ofEmptyUpwardSection(
                    originalEndStation,
                    line
            );
            Sections sections = Sections.from(new ArrayList<>(List.of(endSection)));
            List<Section> newSections = sections.addEndSectionAtUpward(newStation, endSection, request.getDistance());
            sectionDao.deleteById(sectionEntity.getId());

            for (Section section : newSections) {
                sectionDao.insert(DtoMapper.convertToSectionEntity(section));
            }
        }
    }

    public void removeSectionsByStationAndLine(SectionDeleteRequest request) {
        Station station = stationDao.findById(request.getStationId());
        Line line = lineDao.findById(request.getLineId());

        List<SectionEntity> sectionEntities = sectionDao.selectSectionsByStationIdAndLineId(station.getId(), line.getId());
        List<Section> sections = sectionEntities.stream().map(entity -> Section.of(
                entity.getId(),
                stationDao.findById(entity.getUpwardId()),
                stationDao.findById(entity.getDownwardId()),
                entity.getDistance(),
                lineDao.findById(entity.getLineId())
        )).collect(Collectors.toList());
        Sections allSections = Sections.from(sections);

        for (SectionEntity sectionEntity : sectionEntities) {
            sectionDao.deleteById(sectionEntity.getId());
        }

        sectionDao.insert(DtoMapper.convertToSectionEntity(allSections.removeSectionsByStation(station, line)));
    }

    public void removeEndSectionByStationAndLine(SectionDeleteRequest request) {
        Station station = stationDao.findById(request.getStationId());
        Line line = lineDao.findById(request.getLineId());

        List<SectionEntity> sectionEntities = sectionDao.selectSectionsByStationIdAndLineId(station.getId(), line.getId());

        Section section = createEndSection(station, line, sectionEntities);

        for (SectionEntity sectionEntity : sectionEntities) {
            sectionDao.deleteById(sectionEntity.getId());
        }
        sectionDao.insert(DtoMapper.convertToSectionEntity(section));
    }

    private Section createEndSection(Station station, Line line, List<SectionEntity> sectionEntities) {

        SectionEntity entity = findNonEndSectionEntity(sectionEntities);
        Station newEndStation = stationDao.findById(entity.getUpwardId());
        if (newEndStation.equals(station)) {
            newEndStation = stationDao.findById(entity.getDownwardId());
        }


        for (SectionEntity sectionEntity : sectionEntities) {
            if (sectionEntity.getDownwardId() == null) {
                //하행 종착역
                return Section.ofEmptyDownwardSection(newEndStation, line);
            }
            if (sectionEntity.getUpwardId() == null) {
                //상행 종착역
                return Section.ofEmptyUpwardSection(newEndStation, line);
            }
        }
        throw new IllegalArgumentException("[ERROR] 해당하는 종착역 구간이 없습니다.");
    }

    private SectionEntity findNonEndSectionEntity(List<SectionEntity> sectionEntities) {
        for (SectionEntity sectionEntity : sectionEntities) {
            if (sectionEntity.getDownwardId() != null && sectionEntity.getUpwardId() != null) {
                return sectionEntity;
            }
        }
        throw new IllegalArgumentException("[ERROR] 해당하는 종착역 구간이 없습니다.");
    }

    public void removeLastSectionInLine(SectionLastDeleteRequest request) {
        Line line = lineDao.findById(request.getLineId());
        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());

        List<SectionEntity> sectionEntities = sectionDao.selectSectionsByLineId(line.getId());

        List<Long> stations = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            stations.add(sectionEntity.getUpwardId());
            stations.add(sectionEntity.getDownwardId());
        }
        boolean result = stations.containsAll(List.of(upward.getId(), downward.getId()));

        if (!result && sectionEntities.size() != 3) {
            throw new IllegalArgumentException("[ERROR] 노선의 마지막 남은 구간이 아니라 삭제할 수 없습니다.");
        }
        for (SectionEntity sectionEntity : sectionEntities) {
            sectionDao.deleteById(sectionEntity.getId());
        }
    }

    public List<StationResponse> readAllStationsOfLine(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.selectSectionsByLineId(lineId);

        SectionEntity upwardEndSectionEntity = sectionEntities.stream()
                .filter(entity -> entity.getUpwardId() == null)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("[ERROR] 노선이 아직 역이 없습니다."));

        SectionEntity downwardEndSectionEntity = sectionEntities.stream()
                .filter(entity -> entity.getDownwardId() == null)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("[ERROR] 노선이 아직 역이 없습니다."));

        sectionEntities.removeAll(List.of(upwardEndSectionEntity, downwardEndSectionEntity));

        List<Section> sections = sectionEntities.stream().map(entity -> Section.of(
                entity.getId(),
                stationDao.findById(entity.getUpwardId()),
                stationDao.findById(entity.getDownwardId()),
                entity.getDistance(),
                lineDao.findById(entity.getLineId())
        )).collect(Collectors.toList());
        Sections allSections = Sections.from(sections);


        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Station> allStations = allSections.getStations();
        for (Station station : allStations) {
            graph.addVertex(station.getName());
        }

        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpward().getName(), section.getDownward().getName()), section.getDistance()
            );
        }

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        Station upwardEndStation = stationDao.findById(upwardEndSectionEntity.getDownwardId());
        Station downwardEndStation = stationDao.findById(downwardEndSectionEntity.getUpwardId());
        List<String> shortestPath = dijkstraShortestPath.getPath(upwardEndStation.getName(), downwardEndStation.getName()).getVertexList();

        List<Station> orderedStations = shortestPath.stream()
                .map(stationDao::findByName)
                .collect(Collectors.toList());
        return orderedStations.stream()
                .map(DtoMapper::convertToStationReponse)
                .collect(Collectors.toList());
    }
}
