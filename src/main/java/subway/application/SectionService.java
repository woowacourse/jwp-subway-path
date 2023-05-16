package subway.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import subway.application.dto.SectionRequest;
import subway.application.exception.SubwayServiceException;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;

import java.util.List;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void addSection(final SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        List<Section> sections = sectionDao.findByLineId(lineId);

        saveSection(sectionRequest, lineId, sections);
    }

    private void saveSection(final SectionRequest sectionRequest, final Long lineId, final List<Section> sections) {
        Line line = getLineById(lineId);
        Subway subway = Subway.of(line, sections);
        Station baseStation = getStationById(sectionRequest.leftStationId());
        Station nextStation = getStationById(sectionRequest.rightStationId());
        Integer distance = sectionRequest.distance();
        Sections addedSections = subway.findAddSections(new Section(baseStation, nextStation, new Distance(distance)));

        sectionDao.deleteByLeftStationIdAndRightStationId(lineId, addedSections.getLeftStationId(), addedSections.getRightStationId());
        for (Section section : addedSections.getSections()) {
            sectionDao.insert(lineId, section);
        }
    }

    private Line getLineById(final Long lineId) {
        try {
            return lineDao.findById(lineId);
        } catch (DataAccessException exception) {
            throw new SubwayServiceException("기존에 저장된 호선 번호를 입력해주세요.");
        }
    }

    private Station getStationById(final Long stationId) {
        try {
            return stationDao.findById(stationId);
        } catch (DataAccessException exception) {
            throw new SubwayServiceException("기존에 저장된 역 번호를 입력해주세요.");
        }
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        Line line = getLineById(lineId);
        Station station = getStationById(stationId);
        List<Section> sections = sectionDao.findByLineId(lineId);
        Subway subway = Subway.of(line, sections);
        Sections deleteSections = subway.findDeleteSections(station);

        if (deleteSections.isPresent()) {
            Section section = deleteSections.getFirstSection();
            sectionDao.insert(lineId, section);
        }
        sectionDao.deleteByStationId(lineId, stationId);
    }
}
