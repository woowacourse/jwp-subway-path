package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineStationDao;
import subway.dao.StationDao;
import subway.domain.LineStation;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.*;
import subway.exceptions.customexceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class LineStationService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final LineStationDao lineStationDao;

    public LineStationService(LineDao lineDao, StationDao stationDao, LineStationDao lineStationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
    }

    public LineStationResponse addStationToLine(String upBoundStationName, String downBoundStationName, Integer distance, Long lineId) {
        Station upBoundStation = stationDao.findByName(upBoundStationName);
        Station downBoundStation = stationDao.findByName(downBoundStationName);
        if (upBoundStation.equals(downBoundStation)) {
            throw new InvalidDataException("같은 역을 등록할 수 없습니다.");
        }
        Section newSection = new Section(upBoundStation, downBoundStation, distance);

        List<LineStation> lineStations = lineStationDao.findByLine(lineId);
        Sections sections = new Sections(makeSections(lineStations));
        AddResult addResult = sections.addSection(newSection);
        return LineStationResponse.of(updateAddResult(addResult, lineId));
    }

    private List<Section> makeSections(List<LineStation> lineStations) {
        List<Section> sections = new ArrayList<>();
        for (LineStation lineStation : lineStations) {
            Station upBoundStationInLine = stationDao.findById(lineStation.getUpBoundId());
            Station downBoundStationInLine = stationDao.findById(lineStation.getDownBoundId());
            sections.add(new Section(
                    lineStation.getLineId(),
                    upBoundStationInLine,
                    downBoundStationInLine,
                    lineStation.getDistance()));
        }
        return sections;
    }

    private LineStation updateAddResult(AddResult addResult, Long lineId) {
        UpdateSections updateSections = addResult.getUpdateSections();
        for (Section section : updateSections.getSections()) {
            Long id = section.getId();
            Long upBoundId = section.getUpBoundStation().getId();
            Long downBoundId = section.getDownBoundStation().getId();
            Integer distance = section.getDistance();
            lineStationDao.update(new LineStation(id, upBoundId, downBoundId, lineId, distance));
        }
        Section insertSection = addResult.getAddSections().getSections().get(0);
        Long upBoundId = insertSection.getUpBoundStation().getId();
        Long downBoundId = insertSection.getDownBoundStation().getId();
        Integer distance = insertSection.getDistance();
        return lineStationDao.insert(new LineStation(upBoundId, downBoundId, lineId, distance));
    }

    public void removeStationToLine(Long stationId, Long lineId) {
        Station deleteStation = stationDao.findById(stationId);

        List<LineStation> lineStations = lineStationDao.findByLine(lineId);
        Sections sections = new Sections(makeSections(lineStations));
        RemoveResult removeResult = sections.removeStation(deleteStation);
        updateRemoveResult(removeResult, lineId);
    }

    private void updateRemoveResult(RemoveResult removeResult, Long lineId) {
        RemoveIds removeIds = removeResult.getRemoveIds();
        for (Long id : removeIds.getIds()) {
            lineStationDao.deleteById(id);
        }
        UpdateSections updateSections = removeResult.getUpdateSections();
        for (Section section : updateSections.getSections()) {
            Long id = section.getId();
            Long upBoundId = section.getUpBoundStation().getId();
            Long downBoundId = section.getDownBoundStation().getId();
            Integer distance = section.getDistance();
            lineStationDao.update(new LineStation(id, upBoundId, downBoundId, lineId, distance));
        }
    }
}
