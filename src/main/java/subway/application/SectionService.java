package subway.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.InitialSectionAddRequest;
import subway.dto.SectionAddRequest;
import subway.dto.SectionResponse;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public SectionResponse addSection(InitialSectionAddRequest initialSectionAddRequest) {
        Long firstStationId = initialSectionAddRequest.getFirstStationId();
        Long secondStationId = initialSectionAddRequest.getSecondStationId();
        Long lineId = initialSectionAddRequest.getLineId();
        Integer distanceValue = initialSectionAddRequest.getDistance();

        if (hasAnySection(lineId)) {
            throw new DomainException(ExceptionType.LINE_HAS_STATION);
        }

        Station firstStation = stationDao.findById(firstStationId);
        Station secondStation = stationDao.findById(secondStationId);
        Line line = lineDao.findById(lineId);
        Distance distance = new Distance(distanceValue);

        Section section = new Section(null, firstStation, secondStation, line, distance);
        Long id = sectionDao.insert(section);

        return new SectionResponse(id, section);
    }

    private boolean hasAnySection(Long lineId) {
        return !sectionDao.findAllSectionByLineId(lineId).isEmpty();
    }

    @Transactional
    public List<SectionResponse> addSection(SectionAddRequest sectionAddRequest) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Long sourceId = sectionAddRequest.getSourceId();
        Long targetId = sectionAddRequest.getTargetId();
        Integer distance = sectionAddRequest.getDistance();

        if (!hasAnySection(lineId)) {
            throw new DomainException(ExceptionType.LINE_HAS_NO_SECTION);
        }

        Line line = lineDao.findById(lineId);
        Station station = stationDao.findById(stationId);
        List<Section> sections = sectionDao.findAllSectionByLineId(lineId);

        //종점에 추가
        if (targetId == null) {
            Long firstStationId = sectionDao.findfirstStation();
            Long lastStationId = sectionDao.findlastStation();

            Long sectionId;
            if (Objects.equals(sourceId, firstStationId)) {
                sectionId = sectionDao.insert(
                    new Section(null, station, stationDao.findById(firstStationId), line, new Distance(distance)));
                return List.of(new SectionResponse(sectionId, sectionDao.findById(sectionId)));
            }
            if (Objects.equals(sourceId, lastStationId)) {
                sectionId = sectionDao.insert(
                    new Section(null, stationDao.findById(lastStationId), station, line, new Distance(distance)));
                return List.of(new SectionResponse(sectionId, sectionDao.findById(sectionId)));
            }
            throw new DomainException(ExceptionType.NOT_LAST_STOP);
        }

        //사이에 추가
        Section splitTargetSection = sections.stream()
            .filter(section -> section.containsTheseStations(sourceId, targetId))
            .findFirst().orElseThrow(() -> new DomainException(ExceptionType.NON_EXISTENT_SECTION));

        if (splitTargetSection.hasShorterOrSameDistanceThan(distance)) {
            throw new DomainException(ExceptionType.SECTION_CAN_NOT_BE_SPLITED);
        }

        sectionDao.deleteById(splitTargetSection.getId());

        if (splitTargetSection.isSrc(sourceId)) {
            Section firstSection = new Section(null, splitTargetSection.getSrc(), station, line,
                new Distance(distance));
            Section lastSection = new Section(null, station, splitTargetSection.getTar(), line,
                new Distance(splitTargetSection.getDistance().value() - distance));
            Long firstSectionId = sectionDao.insert(firstSection);
            Long lastSectionId = sectionDao.insert(lastSection);
            return List.of(new SectionResponse(firstSectionId, firstSection),
                new SectionResponse(lastSectionId, lastSection));
        }

        Section firstSection = new Section(null, station, splitTargetSection.getTar(), line,
            new Distance(distance));
        Section lastSection = new Section(null, splitTargetSection.getSrc(), station, line,
            new Distance(splitTargetSection.getDistance().value() - distance));
        Long firstSectionId = sectionDao.insert(firstSection);
        Long lastSectionId = sectionDao.insert(lastSection);
        return List.of(new SectionResponse(firstSectionId, firstSection),
            new SectionResponse(lastSectionId, lastSection));
    }
}
