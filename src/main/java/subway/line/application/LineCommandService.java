package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dao.LineDao;
import subway.line.dao.LineEntity;
import subway.line.exception.CanNotDuplicatedLineNameException;
import subway.line.application.dto.RegisterLineRequest;

@Service
@Transactional
public class LineCommandService {

  private final LineDao lineDao;
  private final SectionCommandService sectionCommandService;
  private final LineQueryService lineQueryService;

  public LineCommandService(
      final LineDao lineDao,
      final SectionCommandService sectionCommandService,
      final LineQueryService lineQueryService) {
    this.lineDao = lineDao;
    this.sectionCommandService = sectionCommandService;
    this.lineQueryService = lineQueryService;
  }

  public void deleteLine(final Long lineId) {
    lineDao.deleteById(lineId);
  }

  public void registerLine(final RegisterLineRequest registerLineRequest) {

    final String lineName = registerLineRequest.getLineName();

    if (lineQueryService.isExistLine(lineName)) {
      throw new CanNotDuplicatedLineNameException("이미 등록되어 있는 노선입니다. 노선 이름은 중복될 수 없습니다.");
    }

    final Long savedId = lineDao.save(new LineEntity(lineName));

    sectionCommandService.registerSection(
        registerLineRequest.getCurrentStationName(),
        registerLineRequest.getNextStationName(),
        registerLineRequest.getDistance(),
        savedId
    );
  }
}
