package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.ui.dto.SectionRequest;
import subway.ui.dto.SectionResponse;
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

    public SectionResponse saveSection(SectionRequest request) {
        List<Section> sectionList = sectionDao.findAllByLineId(request.getLineId());
        Sections sections = new Sections(sectionList);

        Station upStation = getSavedStation(sections, request.getUpStationId());
        Station downStation = getSavedStation(sections, request.getDownStationId());
        Line line = lineDao.findById(request.getLineId());
        int distance = request.getDistance();

        ChangesByAddition changes = sections.findChangesWhenAdd(upStation, downStation, line, distance);
        List<Section> added = sectionDao.insertAll(changes.getAddedSections());
        sectionDao.deleteAll(changes.getDeletedSections());
        return SectionResponse.of(findRequestedSection(added, upStation, downStation));
    }

    private Station getSavedStation(Sections sections, Long stationId) {
        return sections.findStationById(stationId)
            .orElse(stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("stationId = " + stationId + " 인 역이 존재하지 않습니다")));
    }

    private Section findRequestedSection(List<Section> addedSections, Station upStation, Station downStation) {
        Sections sections = new Sections(addedSections);
        return sections.findAnySectionWithGivenStations(upStation, downStation);
    }
}
