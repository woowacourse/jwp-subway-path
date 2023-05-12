package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.controller.dto.PostSectionRequest;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.dto.ChangesByAddition;

@Service
public class SectionService {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public SectionService(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public void saveSection(PostSectionRequest request) {
        List<Section> sectionList = sectionDao.findAllByLineId(request.getLineId());
        Sections sections = new Sections(sectionList);

        String upStationName = request.getUpStationName();
        String downStationName = request.getDownStationName();

        Station upStation = getSavedOrNewStation(sections, upStationName);
        Station downStation = getSavedOrNewStation(sections, downStationName);
        Line line = lineDao.findById(request.getLineId());
        int distance = request.getDistance();

        ChangesByAddition changes = sections.getChangesWhenAdded(upStation, downStation, line, distance);
        sectionDao.insertAll(changes.getAddedSections());
        sectionDao.deleteAll(changes.getDeletedSections());
    }

    private Station getSavedOrNewStation(Sections sections, String stationName) {
        return sections.findStationByName(stationName)
            .orElse(stationDao.insert(new Station(stationName)));
    }
}
