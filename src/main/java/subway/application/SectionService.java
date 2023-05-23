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

    private static final String INVALID_LINE_MESSAGE = "기존에 저장된 호선 번호를 입력해주세요.";
    private static final String INVALID_STATION_MESSAGE = "기존에 저장된 역 번호를 입력해주세요.";

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
        Line line = getLineById(lineId);
        List<Section> sections = sectionDao.findByLineId(lineId);

        saveSection(sectionRequest, line, sections);
    }

    private void saveSection(final SectionRequest sectionRequest, final Line line, final List<Section> sections) {
        Subway subway = Subway.of(line, sections);
        Station baseStation = getStationById(sectionRequest.leftStationId());
        Station nextStation = getStationById(sectionRequest.rightStationId());
        Integer distance = sectionRequest.distance();
        Sections updateSections = subway.findUpdateSectionsByAddingSection(new Section(baseStation, nextStation, new Distance(distance)));

        sectionDao.deleteByStationIds(line.getId(), updateSections.getLeftStationId(), updateSections.getRightStationId());
        for (Section section : updateSections.getSections()) {
            sectionDao.insert(line.getId(), section);
        }
    }

    private Line getLineById(final Long lineId) {
        try {
            return lineDao.findById(lineId);
        } catch (DataAccessException exception) {
            throw new SubwayServiceException(INVALID_LINE_MESSAGE);
        }
    }

    private Station getStationById(final Long stationId) {
        try {
            return stationDao.findById(stationId);
        } catch (DataAccessException exception) {
            throw new SubwayServiceException(INVALID_STATION_MESSAGE);
        }
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        Line line = getLineById(lineId);
        Station station = getStationById(stationId);
        List<Section> sections = sectionDao.findByLineId(lineId);
        Subway subway = Subway.of(line, sections);
        Sections updateSections = subway.findUpdateSectionsByDeletingSection(station);

        if (updateSections.isPresent()) {
            Section section = updateSections.getFirstSection();
            sectionDao.insert(lineId, section);
        }
        sectionDao.deleteByStationId(lineId, stationId);
    }
}
