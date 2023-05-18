package subway.line.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineStationResponse;
import subway.line.service.LineService;
import subway.section.presentation.dto.SectionSaveRequest;
import subway.section.service.SectionService;

@Transactional(readOnly = true)
@Component
public class LineFacade {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineFacade(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @Transactional
    public Long createLine(final LineRequest request, final Long finalUpStationId, final Long finalDownStationId) {
        Long lineId = lineService.insert(Line.of(request.getName(), request.getColor()));
        SectionSaveRequest sectionSaveRequest = SectionSaveRequest.of(lineId, finalUpStationId, finalDownStationId, request.getDistance());
        sectionService.saveSection(sectionSaveRequest);
        return lineId;
    }

    @Transactional
    public void registerStation(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        SectionSaveRequest sectionSaveRequest = SectionSaveRequest.of(lineId, upStationId, downStationId, distance);
        sectionService.saveSection(sectionSaveRequest);
    }

    public LineStationResponse getAllStationByLineIdAsc(final Long lineId) {
        return lineService.findAllByIdAsc(lineId);
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest request) {
        lineService.update(id, request);
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineService.deleteById(id);
    }

}
