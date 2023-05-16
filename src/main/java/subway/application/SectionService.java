package subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.*;
import subway.domain.*;
import subway.dto.SectionRequest;
import subway.exception.InvalidStationException;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest request) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        Line line = new Line(lineDao.findById(lineId).get().getName(),new Sections(toSections(sectionEntities)));

        Section requestSection = new Section(
                new Station(request.getStartStation()),
                new Station(request.getEndStation()),
                new Distance(request.getDistance()));

        line.addSection(requestSection);
        addStationOfSection(requestSection.getStartStation(), requestSection.getEndStation());

        sectionDao.deleteAllById(lineId);
        sectionDao.insertAll(toSectionEntities(lineId, line.getSections().getSections()));
    }

    private void addStationOfSection(Station requestStartStation, Station requestEndStation) {
        Map<String ,Long> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getName,StationEntity::getId));

        if (!stations.containsKey(requestStartStation.getName())) {
            stationDao.insert(new StationEntity(requestStartStation.getName()));
        }

        if (!stations.containsKey(requestEndStation.getName())) {
            stationDao.insert(new StationEntity(requestEndStation.getName()));
        }
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        Line line = new Line(lineDao.findById(lineId).get().getName(),new Sections(toSections(sectionEntities)));

        line.getSections().remove(toStation(stationDao.findById(stationId).orElseThrow(InvalidStationException::new)));

        sectionDao.deleteAllById(lineId);
        sectionDao.insertAll(toSectionEntities(lineId, line.getSections().getSections()));

        deleteStation(stationId);
    }

    private void deleteStation(Long stationId) {
        if(!sectionDao.findExistStationById(stationId)){
            stationDao.deleteById(stationId);
        }
    }

    private List<SectionEntity> toSectionEntities(Long lineId, List<Section> sections) {
        Map<String, Long> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        return sections.stream()
                .map(it -> {
                    Long startStationId = stations.get(it.getStartStation().getName());
                    Long endStationId = stations.get(it.getEndStation().getName());
                    return new SectionEntity(lineId, startStationId, endStationId, it.getDistance().getDistance());
                })
                .collect(Collectors.toList());
    }

    private List<Section> toSections(List<SectionEntity> findSections) {
        Map<Long, String> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::getName));

        return findSections.stream()
                .map(it -> new Section(
                        new Station(stations.get(it.getStartStationId())),
                        new Station(stations.get(it.getEndStationId())),
                        new Distance(it.getDistance()))
                )
                .collect(Collectors.toList());
    }

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }
}
