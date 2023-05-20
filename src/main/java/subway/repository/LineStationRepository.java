package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.NotFoundException;

@Repository
public class LineStationRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineStationRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    //todo 질문 :: LineRepository의 findLineById메서드와 동일한데, 어떻게 생각하시는 지 궁금합니다.
    public Line findLineById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                new Sections(sectionDao.findSectionsByLineId(lineId)));
    }

    public Station findStationById(Long lineId, Long stationId) {
        StationEntity station = stationDao.findById(stationId, lineId)
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
        return new Station(stationId, station.getName());
    }

    //todo : 역 초기화 내용 수정하기

    public List<Station> saveInitialStations(Line line) {
        Section initialSection = line.getSections().get(0);
        Station upStation = initialSection.getUpStation();
        Station downStation = initialSection.getDownStation();
        Long upStationId = stationDao.insert(new StationEntity(upStation.getId(), line.getId(),upStation.getName()));
        Long downStationId = stationDao.insert(new StationEntity(downStation.getId(), line.getId(),downStation.getName()));
        Integer distance = initialSection.getDistance().getValue();
        sectionDao.insert(new SectionEntity(line.getId(), upStationId, downStationId, distance));

        return List.of(new Station(upStationId, upStation.getName()),
                new Station(downStationId, downStation.getName()));
    }

    public Station saveStation(Line line, Station newStation) {
        Long newStationId = stationDao.insert(new StationEntity(line.getId(), newStation.getName()));
        deleteOutdatedSection(line);
        addNewSections(line, new Station(newStationId, newStation.getName()));
        return new Station(newStationId, newStation.getName());
    }

    private void deleteOutdatedSection(Line line) {
        List<Section> updatedSections = line.getSections();
        List<SectionEntity> sectionToRemove = sectionDao.findSectionsByLineId(line.getId()).stream()
                .filter(savedSection -> !updatedSections.contains(savedSection))
                .map(toRemove -> SectionEntity.of(line.getId(), toRemove))
                .collect(Collectors.toList());
        if(!sectionToRemove.isEmpty()) {
            sectionToRemove.forEach(sectionDao::delete);
        }
    }

    private void addNewSections(Line line, Station newStation) {
        List<Section> newSections = line.findSectionByStation(newStation);
        newSections.stream()
                .map(section -> new SectionEntity(
                        line.getId(),
                        findStationIdByName(line.getId(), section.getUpStation().getName()),
                        findStationIdByName(line.getId(), section.getDownStation().getName()),
                        section.getDistance().getValue()))
                .forEach(sectionDao::insert);
    }

    private Long findStationIdByName(Long lineId, String stationName) {
        StationEntity stationEntity = stationDao.findByName(stationName, lineId)
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
        System.out.println("해당 역 아이디"+stationEntity.getId());
        return stationEntity.getId();
    }

    public void removeStation(Line line, Station stationToRemove) {
        StationEntity savedStation = stationDao.findByName(stationToRemove.getName(), line.getId())
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
        stationDao.delete(savedStation);
        deleteOutdatedSection(line); // todo : 질문, section 데이터베이스는 외래키로 station을 참조함, 해당 station 삭제하면, 알아서 구간도 삭제되지 않나?
        List<Section> updatedSections = line.getSections();
        List<Section> savedSections = sectionDao.findSectionsByLineId(line.getId());
        updatedSections.stream()
                .filter(updatedSection -> !savedSections.contains(updatedSection))
                .map(updatedSection -> SectionEntity.of(line.getId(), updatedSection))
                .forEach(sectionDao::insert);
    }
}

//    public Line findLineIdByName(String lineName) {
//        LineEntity lineEntity = lineDao.findByName(lineName)
//                .orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
//        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
//                new Sections(sectionDao.findSectionsByLineId(lineEntity.getId())));
//    }
