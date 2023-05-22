package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.dto.SectionResponse;
import subway.dto.StationToLineRequest;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionRepository sectionRepositoryImpl;
    private final StationRepository stationRepositoryImpl;

    public SectionService(final SectionRepository sectionRepository, final StationRepository stationRepositoryImpl) {
        this.sectionRepositoryImpl = sectionRepository;
        this.stationRepositoryImpl = stationRepositoryImpl;
    }

    @Transactional
    public SectionResponse connectStation(final Long lineId, final StationToLineRequest request) {
        validateRequestDistance(request);
        validateStationExist(request);
        List<Section> sectionsByUpStation = sectionRepositoryImpl.findSectionByLineIdAndStationId(lineId,
                request.getUpStationId());
        List<Section> sectionsByDownStation = sectionRepositoryImpl.findSectionByLineIdAndStationId(lineId,
                request.getDownStationId());
        validateExist(sectionsByUpStation, sectionsByDownStation);
        validateTwoStationRequest(lineId, sectionsByUpStation, sectionsByDownStation);

        if (doAddTop(sectionsByUpStation) || isNothing(sectionsByUpStation, sectionsByDownStation)) {
            Section newSection = new Section(lineId, request.getUpStationId(), request.getDownStationId(),
                    request.getDistance());
            return SectionResponse.of(sectionRepositoryImpl.insert(newSection));
        }

        if (isInBetween(sectionsByUpStation)) {
            return addBetweenPosition(lineId, request, sectionsByUpStation);
        }
        return addEndPosition(lineId, request);
    }

    private void validateRequestDistance(final StationToLineRequest request) {
        if (request.getDistance() <= 0) {
            throw new IllegalArgumentException("거리는 양수여야합니다.");
        }
    }

    private void validateStationExist(final StationToLineRequest request) {
        Long upStationId = request.getUpStationId();
        Long downStationId = request.getDownStationId();
        stationRepositoryImpl.findById(upStationId);
        stationRepositoryImpl.findById(downStationId);
    }

    private void validateExist(final List<Section> sectionsByUpStation, final List<Section> sectionsByDownStation) {
        if (sectionsByUpStation.size() > 0 && sectionsByDownStation.size() > 0) {
            throw new IllegalArgumentException("해당 호선에 이미 역이 존재합니다.");
        }
    }

    private void validateTwoStationRequest(final Long lineId, final List<Section> sectionsByUpStation,
                                           final List<Section> sectionsByDownStation) {
        if (isNothing(sectionsByUpStation, sectionsByDownStation) && sectionRepositoryImpl.countByLineId(lineId) > 0) {
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
        sectionRepositoryImpl.update(updateSection);
        Section newSection = new Section(lineId, request.getDownStationId(),
                originSection.getDownStationId(), newDistance);
        return SectionResponse.of(sectionRepositoryImpl.insert(newSection));
    }

    private Section findSectionByUpStationId(final List<Section> sections, final Long upStationId) {
        Section section = sections.get(0);
        if (upStationId.equals(section.getUpStationId())) {
            return section;
        }
        return sections.get(1);
    }

    private SectionResponse addEndPosition(final Long lineId, final StationToLineRequest request) {
        Section newSection = new Section(lineId, request.getUpStationId(),
                request.getDownStationId(), request.getDistance());
        return SectionResponse.of(sectionRepositoryImpl.insert(newSection));
    }

    private void validateDistance(final Section originSection, final StationToLineRequest stationToLineRequest) {
        Integer originDistance = originSection.getDistance();
        Integer requestDistance = stationToLineRequest.getDistance();
        if (originDistance <= requestDistance) {
            throw new IllegalArgumentException("기존 역과의 거리가 요청한 거리보다 짧습니다.");
        }
    }

    @Transactional
    public void disconnectStation(Long lineId, Long stationId) {
        int sectionCountByLine = sectionRepositoryImpl.countByLineId(lineId);
        if (sectionCountByLine == 2) {
            sectionRepositoryImpl.deleteAllByLineId(lineId);
            return;
        }
        List<Section> sections = sectionRepositoryImpl.findSectionByLineIdAndStationId(lineId, stationId);
        if (isInBetween(sections)) {
            Section frontSection = findSectionByDownStationId(sections, stationId);
            Section backSection = findSectionByUpStationId(sections, stationId);
            int updateDistance = frontSection.getDistance() + backSection.getDistance();
            Section updateSection = new Section(frontSection.getId(), frontSection.getUpStationId(),
                    backSection.getDownStationId(), updateDistance);
            sectionRepositoryImpl.update(updateSection);
            sectionRepositoryImpl.deleteById(backSection.getId());
            return;
        }
        sectionRepositoryImpl.deleteByLineIdAndStationId(lineId, stationId);
    }

    private Section findSectionByDownStationId(final List<Section> sections, final Long downStationId) {
        Section section = sections.get(0);
        if (downStationId.equals(section.getDownStationId())) {
            return section;
        }
        return sections.get(1);
    }

    @Transactional
    public void deleteAllByLineId(Long lineId) {
        sectionRepositoryImpl.deleteAllByLineId(lineId);
    }
}
