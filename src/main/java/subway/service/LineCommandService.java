package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.global.exception.line.CanNotDuplicatedLineNameException;
import subway.global.exception.line.CanNotFoundLineException;
import subway.service.dto.RegisterLineRequest;

@Service
@Transactional
public class LineCommandService {

    private final LineDao lineDao;
    private final SectionService sectionService;
    private final LineQueryService lineQueryService;

    public LineCommandService(
            final LineDao lineDao,
            final SectionService sectionService,
            final LineQueryService lineQueryService
    ) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
        this.lineQueryService = lineQueryService;
    }

    public void deleteLine(final Long lineId) {
        lineDao.deleteLineById(lineId);
    }

    public void registerLine(final RegisterLineRequest registerLineRequest) {

        try {
            final String lineName = registerLineRequest.getLineName();
            lineQueryService.findByLineName(lineName);
        } catch (CanNotFoundLineException exception) {
            throw new CanNotDuplicatedLineNameException("이미 등록되어 있는 노선입니다. 노선 이름은 중복될 수 없습니다.");
        }

        final Long savedId = lineDao.save(new LineEntity(registerLineRequest.getLineName()));

        sectionService.registerSection(
                registerLineRequest.getCurrentStationName(),
                registerLineRequest.getNextStationName(),
                registerLineRequest.getDistance(),
                savedId
        );
    }
}
