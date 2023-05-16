package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.response.LineResponse;
import subway.dto.response.SectionResponse;
import subway.dto.response.StationsInLineResponse;
import subway.dto.response.StationsResponse;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

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
        stationDao.findById(upBoundStationId)
                .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다."));
        stationDao.findById(downBoundStationId)
                .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다."));
        lineDao.findById(lineId)
                .orElseThrow(() -> new NotFoundException("해당하는 라인이 존재하지 않습니다."));

        if (upBoundStationId.equals(downBoundStationId)) {
            throw new InvalidDataException("같은 역을 등록할 수 없습니다.");
        }
        Section newSection = new Section(upBoundStationId, downBoundStationId, lineId, distance);

        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        sections.addSection(newSection);

        saveSectionChange(sections.findSectionChange());
        return SectionResponse.of(
                sectionDao.findByStationsIdAndLineId(upBoundStationId, downBoundStationId, lineId)
        );
    }

    public void removeStationFromLine(Long stationId, Long lineId) {
        stationDao.findById(stationId)
                .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다."));
        lineDao.findById(lineId)
                .orElseThrow(() -> new NotFoundException("해당하는 라인이 존재하지 않습니다."));

        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        sections.removeStation(stationId);

        saveSectionChange(sections.findSectionChange());
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
        lineDao.findById(lineId)
                .orElseThrow(() -> new NotFoundException("해당하는 라인이 존재하지 않습니다."));

        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        List<Station> stations = new ArrayList<>();
        List<Long> stationsId = sections.getStationsWithUpToDownDirection();

        for (Long stationId : stationsId) {
            stations.add(stationDao.findById(stationId)
                    .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다.")));
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
