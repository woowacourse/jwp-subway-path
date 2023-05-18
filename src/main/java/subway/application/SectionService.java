package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.SectionResponse;
import subway.dto.StationToLineRequest;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public SectionResponse connectStation(final Long lineId, final StationToLineRequest request) {
        validateRequestDistance(request);
        List<Section> sectionsByUpStation = sectionDao.findSectionByLineIdAndStationId(lineId,
                request.getUpStationId());
        List<Section> sectionsByDownStation = sectionDao.findSectionByLineIdAndStationId(lineId,
                request.getDownStationId());
        validateExist(sectionsByUpStation, sectionsByDownStation);
        validateTwoStationRequest(lineId, sectionsByUpStation, sectionsByDownStation);

        if (doAddTop(sectionsByUpStation) || isNothing(sectionsByUpStation, sectionsByDownStation)) {
            Section newSection = new Section(lineId, request.getUpStationId(), request.getDownStationId(),
                    request.getDistance());
            return SectionResponse.of(sectionDao.insert(newSection));
        }

        if (isInBetween(sectionsByUpStation)) {
            return addBetweenPosition(lineId, request, sectionsByUpStation);
        }
        return addEndPosition(lineId, request, sectionsByUpStation);
    }

    private void validateRequestDistance(final StationToLineRequest request) {
        if (request.getDistance() <= 0) {
            throw new IllegalArgumentException("거리는 양수여야합니다.");
        }
    }

    private void validateExist(final List<Section> sectionsByUpStation, final List<Section> sectionsByDownStation) {
        if (sectionsByUpStation.size() > 0 && sectionsByDownStation.size() > 0) {
            throw new IllegalArgumentException("해당 호선에 이미 역이 존재합니다.");
        }
    }

    private void validateTwoStationRequest(final Long lineId, final List<Section> sectionsByUpStation,
                                           final List<Section> sectionsByDownStation) {
        if (isNothing(sectionsByUpStation, sectionsByDownStation) && sectionDao.countByLineId(lineId) > 0) {
            throw new IllegalArgumentException("노선이 비었을 때를 제외하고 한 번에 두개의 역을 등록할 수 없습니다.");
        }
    }

    private boolean isNothing(final List<Section> sectionsByUpStation, final List<Section> sectionsByDownStation) {
        return sectionsByUpStation.isEmpty() && sectionsByDownStation.isEmpty();
    }

    private boolean doAddTop(final List<Section> sectionsByUpStation) {
        return sectionsByUpStation.isEmpty();
    }

    private boolean isInBetween(final List<Section> sectionsByLineAndStation) {
        return sectionsByLineAndStation.size() == 2;
    }

    private SectionResponse addBetweenPosition(final Long lineId, final StationToLineRequest request,
                                               final List<Section> sectionsByUpStation) {
        Section originSection = findSectionByUpStationId(sectionsByUpStation,
                request.getUpStationId());
        validateDistance(originSection, request);
        Integer originDistance = originSection.getDistance();
        Integer newDistance = request.getDistance();
        Integer updateDistance = originDistance - newDistance;
        Section updateSection = new Section(originSection.getId(), lineId, originSection.getUpStationId(),
                request.getDownStationId(), updateDistance);
        sectionDao.update(updateSection);
        Section newSection = new Section(lineId, request.getDownStationId(),
                originSection.getDownStationId(), newDistance);
        return SectionResponse.of(sectionDao.insert(newSection));
    }

    private Section findSectionByUpStationId(final List<Section> sections, final Long upStationId) {
        Section section = sections.get(0);
        if (upStationId.equals(section.getUpStationId())) {
            return section;
        }
        return sections.get(1);
    }

    private SectionResponse addEndPosition(final Long lineId, final StationToLineRequest request,
                                           final List<Section> sectionsByUpStation) {
        Section section = sectionsByUpStation.get(0);
        validateDistance(section, request);
        Section newSection = new Section(lineId, request.getUpStationId(),
                request.getDownStationId(), request.getDistance());
        return SectionResponse.of(sectionDao.insert(newSection));
    }

    private void validateDistance(final Section originSection, final StationToLineRequest stationToLineRequest) {
        Integer originDistance = originSection.getDistance();
        Integer requestDistance = stationToLineRequest.getDistance();
        if (originDistance <= requestDistance) {
            throw new IllegalArgumentException("기존 역과의 거리가 요청한 거리보다 짧습니다.");
        }
    }
}
