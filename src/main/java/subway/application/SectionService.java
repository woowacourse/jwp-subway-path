package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.application.dto.SectionRequest;

import java.util.List;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void addSection(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        List<Section> sections = sectionDao.findByLineId(lineId);
        if (sectionRequest.leftStationId() == null || sectionRequest.rightStationId() == null) { // TODO: sectionRequest에서 null 체크
            throw new IllegalArgumentException("올바른 역 번호를 입력해주세요.");
        }

        saveSection(sectionRequest, lineId, sections);
    }

    private void saveSection(SectionRequest sectionRequest, Long lineId, List<Section> sections) {
        Subway subway = Subway.of(new Line(lineId), sections);
        Station baseStation = stationDao.findById(sectionRequest.leftStationId()); // TODO: findById 메서드 try catch로 묶기 -> 예외 전파 방지
        Station nextStation = stationDao.findById(sectionRequest.rightStationId());
        Integer distance = sectionRequest.distance();
        Sections addedSections = subway.findAddSections(new Section(baseStation, nextStation, new Distance(distance)));

        sectionDao.deleteByLeftStationIdAndRightStationId(lineId, addedSections.getLeftStationId(), addedSections.getRightStationId());
        for (Section section : addedSections.getSections()) {
            sectionDao.insert(lineId, section);
        }
    }

    public void deleteStation(Long lineId, Long stationId) {
        Station station = stationDao.findById(stationId);
        List<Section> sections = sectionDao.findByLineId(lineId);

        Subway subway = Subway.of(new Line(lineId), sections);
        Sections deleteSections = subway.findDeleteSections(station);

        if (deleteSections.isPresent()) {
            Section section = deleteSections.getFirstSection();
            sectionDao.insert(lineId, section);
        }
        sectionDao.deleteByStationId(lineId, stationId);
    }
}
