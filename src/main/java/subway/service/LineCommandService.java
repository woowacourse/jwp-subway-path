package subway.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.event.StationChange;
import subway.exception.line.CanNotDuplicatedLineNameException;
import subway.service.dto.RegisterLineRequest;

@Service
@Transactional
public class LineCommandService {

  private final LineDao lineDao;
  private final SectionCommandService sectionCommandService;
  private final LineQueryService lineQueryService;
  private final ApplicationEventPublisher applicationEventPublisher;

  public LineCommandService(
      final LineDao lineDao,
      final SectionCommandService sectionCommandService,
      final LineQueryService lineQueryService,
      final ApplicationEventPublisher applicationEventPublisher) {
    this.lineDao = lineDao;
    this.sectionCommandService = sectionCommandService;
    this.lineQueryService = lineQueryService;
    this.applicationEventPublisher = applicationEventPublisher;
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

    applicationEventPublisher.publishEvent(new StationChange());
  }
}
