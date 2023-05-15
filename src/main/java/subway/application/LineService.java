package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.LineEntity;
import subway.domain.entity.SectionEntity;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionSaveRequest;
import subway.facade.LineFacade;
import subway.facade.SectionFacade;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineFacade lineFacade;
    private final SectionFacade sectionFacade;

    public LineService(final LineFacade lineFacade, final SectionFacade sectionFacade) {
        this.lineFacade = lineFacade;
        this.sectionFacade = sectionFacade;
    }

    @Transactional
    public Long createLine(final LineRequest request, final Long finalUpStationId, final Long finalDownStationId) {
        Long lineId = lineFacade.insert(LineEntity.of(request.getName(), request.getColor()));
        SectionEntity sectionEntity = SectionEntity.of(lineId, finalUpStationId, finalDownStationId, request.getDistance());
        sectionFacade.saveSection(SectionSaveRequest.of(sectionEntity));
        return lineId;
    }

    @Transactional
    public void registerStation(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        SectionEntity sectionEntity = SectionEntity.of(lineId, upStationId, downStationId, distance);
        sectionFacade.saveSection(SectionSaveRequest.of(sectionEntity));
    }

    public List<LineResponse> findLineResponses() {
        return lineFacade.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        return lineFacade.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        lineFacade.update(id, request);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineFacade.deleteById(id);
    }

}
