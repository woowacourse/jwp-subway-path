package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.SectionRequest;

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
        if (sectionRequest.leftStationId() == null || sectionRequest.rightStationId() == null) {
            throw new IllegalArgumentException("올바른 역 번호를 입력해주세요.");
        }

        saveSection(sectionRequest, lineId, sections);
    }

    private void saveSection(SectionRequest sectionRequest, Long lineId, List<Section> sections) {
        Subway subway = Subway.of(new Line(lineId), sections);
        Station baseStation = stationDao.findById(sectionRequest.leftStationId());
        Station nextStation = stationDao.findById(sectionRequest.rightStationId());
        Integer distance = sectionRequest.distance();
        Sections addedSections = subway.addSection(new Section(baseStation, nextStation, new Distance(distance)));

        sectionDao.deleteByLeftStationIdAndRightStationId(lineId, addedSections.getLeftStationId(), addedSections.getRightStationId());
        for (Section section : addedSections.getSections()) {
            sectionDao.insert(lineId, section);
        }
    }

    public void deleteStation(Long lineId, Long stationId) {
        Station station = stationDao.findById(stationId);

        List<Section> sections = sectionDao.findByLineId(lineId);
        Subway subway = Subway.of(new Line(lineId), sections);

        if (!subway.hasStation(station)) {
            throw new IllegalArgumentException("역이 존재하지 않습니다.");
        }

        if (subway.hasLeftSection(station) && subway.hasRightSection(station)) {
            Section rightSection = subway.findRightSection(station);
            Section leftSection = subway.findLeftSection(station);
            Station right = rightSection.getRight();
            Station left = leftSection.getLeft();
            int leftDistance = leftSection.getDistance();
            int rightDistance = rightSection.getDistance();
            sectionDao.insert(lineId, new Section(left, right, new Distance(leftDistance + rightDistance)));
        }
        sectionDao.deleteByStationId(lineId, stationId);
    }
}
