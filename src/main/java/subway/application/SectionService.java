package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.strategy.delete.SectionDeleter;
import subway.application.strategy.insert.InsertSection;
import subway.application.strategy.insert.SectionInserter;
import subway.domain.Station;
import subway.domain.section.SingleLineSections;
import subway.dto.SectionRequest;

import java.util.Objects;

@Service
@Transactional
public class SectionService {

    private final SubwayReadService subwayReadService;
    private final SectionInserter sectionInserter;
    private final SectionDeleter sectionDeleter;

    public SectionService(SubwayReadService subwayReadService, SectionInserter sectionInserter, SectionDeleter sectionDeleter) {
        this.subwayReadService = subwayReadService;
        this.sectionInserter = sectionInserter;
        this.sectionDeleter = sectionDeleter;
    }

    public Long insertSection(Long lineId, SectionRequest request) {
        validateInput(request, lineId);

        final Station upStation = subwayReadService.findStationById(request.getUpStationId());
        final Station downStation = subwayReadService.findStationById(request.getDownStationId());
        final SingleLineSections sections = subwayReadService.findSingLineSectionsByLineId(lineId);

        validateInsert(upStation, downStation, sections);

        final InsertSection insertSection = new InsertSection(upStation, downStation, request.getDistance(), lineId);
        return sectionInserter.insert(sections, insertSection);
    }

    private void validateInput(SectionRequest request, Long lineId) {
        if (Objects.equals(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }

        if (!subwayReadService.exists(lineId)) {
            throw new IllegalArgumentException("존재 하지 않는 노선에는 구간을 추가 할 수 없습니다.");
        }
    }

    private void validateInsert(Station upStation, Station downStation, SingleLineSections sortedSections) {
        if (sortedSections.hasSection(upStation, downStation)) {
            throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
        }

        if (sortedSections.canInsert(upStation, downStation)) {
            throw new IllegalArgumentException("역이 존재하지 않으면 추가할 수 없습니다.");
        }
    }

    public void deleteStation(Long lineId, Long targetId) {
        final Station targetStation = subwayReadService.findStationById(targetId);
        final SingleLineSections sections = subwayReadService.findSingLineSectionsByLineId(lineId);

        sectionDeleter.delete(sections, targetStation);
    }
}
