package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionChange;
import subway.dto.response.LineResponse;
import subway.dto.response.SectionResponse;
import subway.dto.response.StationsInLineResponse;
import subway.dto.response.StationsResponse;
import subway.exceptions.customexceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class SectionService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public SectionResponse addStationToLine(Long upBoundStationId, Long downBoundStationId, Integer distance, Long lineId) {
        Station upBoundStation = stationDao.findById(upBoundStationId);
        Station downBoundStation = stationDao.findById(downBoundStationId);
        Line line = lineDao.findById(lineId);

        if (upBoundStationId.equals(downBoundStationId)) {
            throw new InvalidDataException("같은 역을 등록할 수 없습니다.");
        }
        Section newSection = new Section(upBoundStationId, downBoundStationId, lineId, distance);

        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        SectionChange sectionChange = sections.addSection(newSection);

        saveSectionChange(sectionChange);
        return SectionResponse.of(
                sectionDao.findByStationsIdAndLineId(upBoundStationId, downBoundStationId, lineId)
        );
    }

    public void removeStationFromLine(Long stationId, Long lineId) {
        Station deleteStation = stationDao.findById(stationId);

        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        SectionChange sectionChange = sections.removeStation(stationId);

        saveSectionChange(sectionChange);
    }

    public void saveSectionChange(SectionChange sectionChange) {
        for (Section section : sectionChange.getNewSections()) {
            sectionDao.insert(section);
        }
        for (Section section : sectionChange.getUpdatedSections()) {
            sectionDao.update(section);
        }
        for (Section section : sectionChange.getDeletedSections()) {
            sectionDao.deleteById(section.getId());
        }
    }

    @Transactional(readOnly = true)
    public StationsResponse findStationsByLineId(Long lineId) {
        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        List<Station> stations = new ArrayList<>();
        List<Long> stationsId = sections.getStationsWithUpToDownDirection();

        for (Long stationId : stationsId) {
            stations.add(stationDao.findById(stationId));
        }
        return new StationsResponse(stations);
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
