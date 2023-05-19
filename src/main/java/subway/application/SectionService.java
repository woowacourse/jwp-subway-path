package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SortedSingleLineSections;
import subway.domain.Station;
import subway.domain.dto.ChangesByAddition;
import subway.domain.dto.ChangesByDeletion;
import subway.ui.dto.DeleteSectionRequest;
import subway.ui.dto.PostSectionRequest;
import subway.ui.dto.SectionResponse;

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

    @Transactional
    public SectionResponse saveSection(Long lineId, PostSectionRequest request) {
        List<Section> sectionList = sectionDao.findAllByLineId(lineId);
        SortedSingleLineSections sortedSingleLineSections = new SortedSingleLineSections(sectionList);

        Station upStation = getSavedStation(sortedSingleLineSections, request.getUpStationId());
        Station downStation = getSavedStation(sortedSingleLineSections, request.getDownStationId());
        Line line = lineDao.findById(lineId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 데이터가 없습니다."));
        int distance = request.getDistance();

        ChangesByAddition changes = sortedSingleLineSections.findChangesWhenAdd(upStation, downStation, line, distance);
        List<Section> added = sectionDao.insertAll(changes.getAddedSections());
        sectionDao.deleteGivenSections(changes.getDeletedSections());
        return SectionResponse.of(findRequestedSection(added, upStation, downStation));
    }

    private Station getSavedStation(SortedSingleLineSections sortedSingleLineSections, Long stationId) {
        return sortedSingleLineSections.findStationById(stationId)
            .orElse(stationDao.findById(stationId)
                .orElseThrow(
                    () -> new IllegalArgumentException("해당 노선에 stationId = " + stationId + " 인 역이 존재하지 않습니다")));
    }

    private Section findRequestedSection(List<Section> addedSections, Station upStation, Station downStation) {
        SortedSingleLineSections sortedSingleLineSections = new SortedSingleLineSections(addedSections);
        return sortedSingleLineSections.findAnySectionWithGivenStations(upStation, downStation);
    }

    @Transactional
    public void deleteSection(Long lineId, DeleteSectionRequest request) {
        Long stationId = request.getStationId();

        SortedSingleLineSections sortedSingleLineSections = new SortedSingleLineSections(
            sectionDao.findAllByLineId(lineId));
        deleteLineIfLastSection(sortedSingleLineSections, lineId);
        Station station = sortedSingleLineSections.findStationById(stationId)
            .orElseThrow(() -> new IllegalArgumentException("해당 노선에 stationId = " + stationId + " 인 역이 존재하지 않습니다"));

        ChangesByDeletion changes = sortedSingleLineSections.findChangesWhenDelete(station);
        sectionDao.insertAll(changes.getAddedSections());
        sectionDao.deleteGivenSections(changes.getDeletedSections());
    }

    private void deleteLineIfLastSection(SortedSingleLineSections sortedSingleLineSections, Long lineId) {
        if (sortedSingleLineSections.size() == 1) {
            lineDao.deleteById(lineId);
        }
    }
}
