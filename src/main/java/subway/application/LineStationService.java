package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineStationDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.AddResult;
import subway.dto.RemoveIds;
import subway.dto.RemoveResult;
import subway.dto.UpdateSections;
import subway.dto.response.*;
import subway.exceptions.customexceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public LineStationResponse addStationToLine(Long upBoundStationId, Long downBoundStationId, Integer distance, Long lineId) {
        Station upBoundStation = stationDao.findById(upBoundStationId);
        Station downBoundStation = stationDao.findById(downBoundStationId);
        if (upBoundStation.equals(downBoundStation)) {
            throw new InvalidDataException("같은 역을 등록할 수 없습니다.");
        }
        Section newSection = new Section(upBoundStation, downBoundStation, distance);

        List<LineStation> lineStations = lineStationDao.findByLine(lineId);
        Sections sections = new Sections(makeSections(lineStations));
        AddResult addResult = sections.addSection(newSection);

        LineStation lineStation = updateAddResult(addResult, lineId);
        Line line = lineDao.findById(lineId);

        return new LineStationResponse(
                lineStation.getId(),
                StationResponse.of(upBoundStation),
                StationResponse.of(downBoundStation),
                LineResponse.of(line),
                distance
        );
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

    @Transactional(readOnly = true)
    public StationsResponse findStationsByLineId(Long lineId) {
        List<LineStation> lineStations = lineStationDao.findByLine(lineId);
        Sections sections = new Sections(makeSections(lineStations));
        return new StationsResponse(sections.getStationsWithUpToDownDirection());
    }

    @Transactional(readOnly = true)
    public List<StationsInLineResponse> findAllLines(List<Line> lines) {
        return lines.stream()
                .map(line -> {
                    StationsResponse stationsResponse = findStationsByLineId(line.getId());
                    return new StationsInLineResponse(LineResponse.of(line), stationsResponse);
                }).collect(Collectors.toList());
    }
}
