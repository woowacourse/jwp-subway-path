package subway.application;

import java.util.Optional;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;

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

    public void saveSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        String startStationName = sectionCreateRequest.getStartStation();
        String endStationName = sectionCreateRequest.getEndStation();
        validateStation(startStationName, endStationName);
        addSection(lineId, new Section(new Station(startStationName), new Station(endStationName),
                sectionCreateRequest.getDistance()));
    }

    private void validateStation(String startStationName, String endStationName) {
        if (!stationDao.existsBy(startStationName) || !stationDao.existsBy(endStationName)) {
            throw new IllegalArgumentException();
        }
    }

    private void addSection(Long lineId, Section section) {
        int distance = section.getDistance();
        if (sectionDao.isEmptyByLineId(lineId)) {
            sectionDao.insert(lineId, section);
            lineDao.updateStartStationById(lineId, section.getStartStation());
            return;
        }
        Station startStation = section.getStartStation();
        Station endStation = section.getEndStation();

        boolean hasStartStation = sectionDao.isStationInLine(lineId, startStation.getName());
        boolean hasEndStation = sectionDao.isStationInLine(lineId, endStation.getName());
        if ((hasStartStation && hasEndStation) || (!hasStartStation && !hasEndStation)) {
            throw new IllegalArgumentException();
        }
        if (hasStartStation) {
            Optional<SectionEntity> targetSectionOptional = sectionDao.findByStartStationNameAndLineId(
                    startStation.getName(), lineId);
            if (targetSectionOptional.isPresent()) {
                SectionEntity sectionEntity = targetSectionOptional.get();
                if (distance >= sectionEntity.getDistance()) {
                    throw new IllegalArgumentException();
                }
                String tempEndStationName = sectionEntity.getEndStationName();
                sectionDao.insert(lineId, new Section(endStation, new Station(tempEndStationName),
                        sectionEntity.getDistance() - distance));
                sectionEntity.setEndStationName(endStation.getName());
                sectionEntity.setDistance(distance);
                sectionDao.update(sectionEntity);
                return;
            }
            sectionDao.insert(lineId, section);
        }
        if (hasEndStation) {
            Optional<SectionEntity> targetSectionOptional = sectionDao.findByEndStationNameAndLineId(
                    endStation.getName(), lineId);
            if (targetSectionOptional.isPresent()) {
                SectionEntity sectionEntity = targetSectionOptional.get();
                if (distance >= sectionEntity.getDistance()) {
                    throw new IllegalArgumentException();
                }
                String tempStartStationName = sectionEntity.getStartStationName();
                sectionDao.insert(lineId, new Section(new Station(tempStartStationName), startStation,
                        sectionEntity.getDistance() - distance));
                sectionEntity.setStartStationName(startStation.getName());
                sectionEntity.setDistance(distance);
                sectionDao.update(sectionEntity);
                return;
            }
            sectionDao.insert(lineId, section);
            lineDao.updateStartStationById(lineId, section.getStartStation());
        }
    }

    public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {

    }
}
