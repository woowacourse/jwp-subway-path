package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.domain.LineEntity;
import subway.domain.SectionEntity;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionSaveRequest;
import subway.exception.LineNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(final LineDao lineDao, final SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    @Transactional
    public Long createLine(final LineRequest request, final Long finalUpStationId, final Long finalDownStationId) {
        Long lineId = lineDao.insert(LineEntity.of(request.getName(), request.getColor()));
        SectionEntity sectionEntity = SectionEntity.of(lineId, finalUpStationId, finalDownStationId, request.getDistance());
        sectionService.saveSection(SectionSaveRequest.of(sectionEntity));
        return lineId;
    }

    @Transactional
    public void registerStation(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        SectionEntity sectionEntity = SectionEntity.of(lineId, upStationId, downStationId, distance);
        sectionService.saveSection(SectionSaveRequest.of(sectionEntity));
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> allLineEntities = lineDao.findAll();
        return allLineEntities.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> LineNotFoundException.THROW);
        return LineResponse.of(lineEntity);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow();
        lineEntity.updateInfo(request.getName(), request.getColor());
        lineDao.updateById(id, lineEntity);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
