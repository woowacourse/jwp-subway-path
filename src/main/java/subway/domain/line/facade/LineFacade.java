package subway.domain.line.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.presentation.dto.LineRequest;
import subway.domain.line.presentation.dto.LineResponse;
import subway.domain.line.service.LineService;
import subway.domain.section.domain.entity.SectionEntity;
import subway.domain.section.presentation.dto.SectionSaveRequest;
import subway.domain.section.service.SectionService;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class LineFacade {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineFacade(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @Transactional
    public Long createLine(final LineRequest request, final Long finalUpStationId, final Long finalDownStationId) {
        Long lineId = lineService.insert(request.toEntity());
        SectionEntity sectionEntity = SectionEntity.of(lineId, finalUpStationId, finalDownStationId, request.getDistance());
        sectionService.saveSection(SectionSaveRequest.of(sectionEntity));
        return lineId;
    }

    @Transactional
    public void registerStation(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        SectionEntity sectionEntity = SectionEntity.of(lineId, upStationId, downStationId, distance);
        sectionService.saveSection(SectionSaveRequest.of(sectionEntity));
    }

    public List<LineResponse> getAll() {
        return lineService.findAll();
    }

    public LineResponse getLineResponseById(final Long id) {
        return lineService.findById(id);
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
